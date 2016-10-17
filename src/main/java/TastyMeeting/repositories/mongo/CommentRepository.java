package TastyMeeting.repositories.mongo;

import TastyMeeting.data.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by rafal on 5/26/16.
 */
public interface CommentRepository extends MongoRepository<Comment, String> {
}
