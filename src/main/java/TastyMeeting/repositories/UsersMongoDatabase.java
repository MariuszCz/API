package TastyMeeting.repositories;

import TastyMeeting.Exceptions.*;
import TastyMeeting.data.*;
import TastyMeeting.repositories.interfaces.UsersDatabase;
import TastyMeeting.repositories.mongo.ConfirmationTokenRepository;
import TastyMeeting.repositories.mongo.UserRepository;
import TastyMeeting.utils.EmailHandler;
import TastyMeeting.utils.OauthTokenHandlerSingleton;
import TastyMeeting.utils.SecureUidsGeneratorSingleton;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafal on 5/23/16.
 */
public class UsersMongoDatabase implements UsersDatabase {
    UserRepository userRepository;
    ConfirmationTokenRepository confirmationTokenRepository;
    //TODO change for enum?
    private String MALE = "mężczyzna";
    private String FEMALE = "kobieta";

    String SERVER_BASE_API_ADDRESS = "http://localhost:8080/api";
    String EMAIL_FROM = "tastymeeting@pydyniak.pl";
    //    String EMAIL_FROM = "tastymeeting123"; //gmail.com
    String EMAIL_PASSWORD = "Tasty123";
    String SMTP_HOST = "mail.pydyniak.pl";
    String SMTP_PORT = "587";
    String CONFIRMATION_EMAIL_SUBJECT = "Aktywacja konta w TastyMeeting";
    String CONFIRMATION_EMAIL_BODY = "Aby aktywować swoje konto wejdź na adres " + SERVER_BASE_API_ADDRESS + "/users/activate/";

    public UsersMongoDatabase(ConfirmationTokenRepository confirmationTokenRepository, UserRepository userRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void addUser(User user) throws DuplicationException, WrongEmailException, IllegalArgumentException {
        if (!isUsernameValidEmail(user.getEmail()))
            throw new WrongEmailException();
        if (user.getGender() != null && checkGender(user.getGender()))
            throw new IllegalArgumentException();
        if (checkIfUserExists(user.getEmail()))
            throw new DuplicationException();

        user.addRole(new Role(0, "ROLE_USER"));
        userRepository.save(user);
    }

    private boolean isUsernameValidEmail(String email) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(regex);
    }

    @Override
    public void updateUser(String id, User user) {
        if (!isUsernameValidEmail(user.getEmail())) {
            throw new WrongEmailException();
        }

        user.setId(id);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String id) throws WrongIdException {
        try {
            userRepository.delete(id);
            //thrown if user with given id doesn't exists
        } catch (IllegalArgumentException ex) {
            throw new WrongIdException();
        }
    }

    @Override
    public User getUser(String id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findByLogin(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean checkIfUserExists(String email) {
        return findByLogin(email) != null;
    }

    @Override
    public boolean checkUserInterests(ArrayList interestNames) {
        //TODO implement when needed
        return true;
    }

    @Override
    public boolean checkGender(String gender) {
        return MALE.compareToIgnoreCase(gender) == 0 || FEMALE.compareToIgnoreCase(gender) == 0
                || gender == null;
    }

    @Override
    public void addUserInterest(String id, Interest interest) throws InterestAlreadyExistsException {
        User user = getUser(id);
        ArrayList<Interest> interests = user.getInterests();
        interests.add(interest);
        user.setInterests(interests);
        updateUser(id, user);
    }

    @Override
    public void addUserPreference(String id, CulinaryPreference culinaryPreference) throws PreferenceAlreadyExistsException {
        User user = getUser(id);
        ArrayList<CulinaryPreference> culinaryPreferences = user.getCulinaryPreferences();
        culinaryPreferences.add(culinaryPreference);
        user.setCulinaryPreferences(culinaryPreferences);
        updateUser(id, user);
    }

    @Override
    public void removePreference(String id, String preferenceId) {
        User user = getUser(id);
        ArrayList<CulinaryPreference> culinaryPreferences = user.getCulinaryPreferences();
        culinaryPreferences.removeIf(preference -> preference.getId().equals(preferenceId));
        user.setCulinaryPreferences(culinaryPreferences);
        updateUser(id, user);
    }

    @Override
    public void removeInterest(String id, String interestId) {
        User user = getUser(id);
        ArrayList<Interest> interests = user.getInterests();
        interests.removeIf(interest -> interest.getId().equals(interestId));
        user.setInterests(interests);
        updateUser(id, user);
    }

    @Override
    public BearerToken facebookLogin(Connection<?> connection, Facebook facebook) throws IOException {
        User user = findUserByFacebookUID(connection);
        if (user == null) {
            System.out.println("Creating an account");
            try {
                user = createUser(connection, facebook);
            } catch (DuplicationException e) {
                //TODO handle in future
                e.printStackTrace();
            }
        }
        System.out.println("Has an account!");
        return getBearerToken(user);
    }

    private User findUserByFacebookUID(Connection<?> connection) {
        String facebookUID = connection.getKey().getProviderUserId();

        return userRepository.findByFacebookUID(facebookUID);
    }

    private User createUser(Connection<?> connection, Facebook facebook) throws DuplicationException {
        User user = new User();
        user.setEmail(facebook.userOperations().getUserProfile().getEmail());
        user.setPassword(generateRandomPassword());
        user.setFacebookUID(connection.getKey().getProviderUserId());
        addUser(user);
        return user;
    }

    private String generateRandomPassword() {
        return SecureUidsGeneratorSingleton.getInstance().generateUID();
    }

    private BearerToken getBearerToken(User user) throws IOException {
        return OauthTokenHandlerSingleton.getInstance().getBearerTokenForUser(user);
    }

    @Override
    public void sendConfirmationEmail(String email) {
        EmailHandler emailHandler = new EmailHandler();
        String token = SecureUidsGeneratorSingleton.getInstance().generateUID();
        User user = findByLogin(email);
        ConfirmationToken confirmationToken = new ConfirmationToken(user.getId(), token);
        confirmationTokenRepository.save(confirmationToken);

        String body = CONFIRMATION_EMAIL_BODY + token;

        emailHandler.sendFromSmtp(EMAIL_FROM, EMAIL_PASSWORD, email, CONFIRMATION_EMAIL_SUBJECT, body, SMTP_HOST, SMTP_PORT, false);
    }

    @Override
    public void activateUser(String token) throws WrongIdException {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token);

        if (confirmationToken == null)
            throw new WrongIdException();

        User user = getUser(confirmationToken.getUserId());
        user.setActive(true);
        updateUser(user.getId(), user);
    }

    @Override
    public User getCurrentlyLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
