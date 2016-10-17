package TastyMeeting.configurations;

import TastyMeeting.repositories.*;
import TastyMeeting.repositories.interfaces.CulinaryPreferencesDatabase;
import TastyMeeting.repositories.interfaces.InterestsDatabase;
import TastyMeeting.repositories.interfaces.MeetingsDatabase;
import TastyMeeting.repositories.interfaces.UsersDatabase;
import TastyMeeting.repositories.mongo.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by rafal on 4/4/16.
 */
@Configuration
public class Beans {
    private final String MONGO_URI = "mongodb://tasty:tasty123@ds015720.mlab.com:15720/tasty_meeting";
    UsersDatabase usersDatabase;
    MongoDatabase mongoDatabase;
    InterestsDatabase interestsDatabase;
    MeetingsDatabase meetingsMongoDatabase;
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    InterestRepository interestRepository;
    @Autowired
    CulinaryPreferenceRepository culinaryPreferenceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;

    @Bean
    public MongoDatabase mongoClient() {
        if (mongoDatabase == null) {
            MongoClient mongoClient = new MongoClient(new MongoClientURI(MONGO_URI));
            mongoDatabase = mongoClient.getDatabase("tasty_meeting");
        }
        return mongoDatabase;
    }

    @Bean
    public UsersDatabase usersDatabase() {
        MongoCollection<Document> users = mongoClient().getCollection("users");
        return new UsersMongoDatabase(confirmationTokenRepository, userRepository);
    }

    @Bean
    public InterestsDatabase interestsDatabase() {
        interestsDatabase = new InterestsMongoDatabase(interestRepository);
        return interestsDatabase;
    }

    @Bean
    public CulinaryPreferencesDatabase culinaryPreferencesDatabase() {
        return new CulinaryPreferencesMongoDatabase(culinaryPreferenceRepository);
    }

    @Bean
    public MeetingsDatabase meetingsDatabase() {
        MongoCollection<Document> meetings = mongoClient().getCollection("meetings");
        meetingsMongoDatabase = new MeetingsMongoDatabase(meetingRepository, meetings, commentRepository, usersDatabase(), culinaryPreferenceRepository);
        return meetingsMongoDatabase;
    }

    UserDetailsService userDetailsService;

    @Bean
    public UserDetailsService userDetailsService() {
        if (userDetailsService == null)
            userDetailsService = new CustomUserDetailsService(usersDatabase());
        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }
}
