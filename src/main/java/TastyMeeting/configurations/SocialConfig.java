package TastyMeeting.configurations;

import TastyMeeting.repositories.MongoConnectionTransformers;
import TastyMeeting.repositories.MongoUsersConnectionRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
@Import(Beans.class)
public class SocialConfig {
    private final String MONGO_URI = "mongodb://tasty:tasty123@ds015720.mlab.com:15720/tasty_meeting";

    @Autowired
    TextEncryptor textEncryptor;

    String clientId = "196774894033798";
    String clientSecret = "81522ec512b039be33d3a03099630fd4";

    @Bean //doesnt work with connectionFactoryLocator name
    public ConnectionFactoryLocator factoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new FacebookConnectionFactory(
                clientId,
                clientSecret));
        return registry;
    }


    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public ConnectionRepository connectionRepository() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
        }
        return usersConnectionRepository().createConnectionRepository(authentication.getName());
    }

    @Bean
    public MongoConnectionTransformers mongoConnectionTransformers() {
        return new MongoConnectionTransformers(factoryLocator(), textEncryptor);
    }

    @Bean
    public MongoDbFactory mongoDbFactory() {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(MONGO_URI));
        return new SimpleMongoDbFactory(mongoClient, "tasty_meeting");
    }

    @Bean
    public MongoOperations mongoOperations() {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        return new MongoUsersConnectionRepository(mongoOperations(), factoryLocator(), mongoConnectionTransformers());
    }


//    @Bean
//    public UsersConnectionRepository usersConnectionRepository() {
//        JpaUsersConnectionRepository usersConnectionRepository = new
//                JpaUsersConnectionRepository(socialUserRepository, userRepository,
//                connectionFactoryLocator(), textEncryptor);
//
//        return usersConnectionRepository;
//    }

}