package TastyMeeting.repositories.mongo;

import TastyMeeting.data.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by rafal on 5/23/16.
 */
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);

    User findByFacebookUID(String UID);
}
