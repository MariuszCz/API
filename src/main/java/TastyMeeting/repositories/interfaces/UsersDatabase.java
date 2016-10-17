package TastyMeeting.repositories.interfaces;

import TastyMeeting.Exceptions.DuplicationException;
import TastyMeeting.Exceptions.InterestAlreadyExistsException;
import TastyMeeting.Exceptions.PreferenceAlreadyExistsException;
import TastyMeeting.Exceptions.WrongIdException;
import TastyMeeting.data.BearerToken;
import TastyMeeting.data.CulinaryPreference;
import TastyMeeting.data.Interest;
import TastyMeeting.data.User;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafal on 4/4/16.
 */
public interface UsersDatabase {

    List<User> getUsers();

    void addUser(User user) throws DuplicationException;

    void updateUser(String id, User user);

    void deleteUser(String id) throws WrongIdException;

    User getUser(String id);

    User findByLogin(String email);

    boolean checkIfUserExists(String email);

    boolean checkUserInterests(ArrayList interestNames);

    boolean checkGender(String gender);

    void addUserInterest(String id, Interest interest) throws InterestAlreadyExistsException;

    void addUserPreference(String id, CulinaryPreference culinaryPreference) throws PreferenceAlreadyExistsException;

    void removePreference(String id, String preferenceId);

    void removeInterest(String id, String interestId);

    BearerToken facebookLogin(Connection<?> connection, Facebook facebook) throws IOException;

    void sendConfirmationEmail(String email);

    void activateUser(String token) throws WrongIdException;

    User getCurrentlyLoggedUser();
}