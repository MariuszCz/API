package TastyMeeting.repositories.mongo;

import TastyMeeting.data.Meeting;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by rafal on 5/23/16.
 */
public interface MeetingRepository extends MongoRepository<Meeting, String> {
    Meeting findByTitle(String title);
    List<Meeting> findByLocationNear(Point location, Distance distance);
   // GeoResults<Meeting> findByLocationNear(Point location,Distance distance);
}
