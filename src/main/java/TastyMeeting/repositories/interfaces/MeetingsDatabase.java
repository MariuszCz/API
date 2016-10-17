package TastyMeeting.repositories.interfaces;

import TastyMeeting.Exceptions.*;
import TastyMeeting.data.Comment;
import TastyMeeting.data.CulinaryPreference;
import TastyMeeting.data.Joined;
import TastyMeeting.data.Meeting;
import com.mongodb.DBObject;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mariusz on 04.05.16.
 */
public interface MeetingsDatabase {

    List<Meeting> getMeetings();

    void addMeeting(Meeting meeting);

    Meeting getMeeting(String id);

    List<Meeting> findByLocation(Point point, Distance distance);

    Meeting findByTitle(String title);

    void addComment(String id, Comment comment);

    void addMember(String id, String memberName) throws MemberAlreadyExistsException, PrivilegesMissingException;

    void joinToMeeting(String id) throws MemberAlreadyExistsException, WrongIdException;

    void leaveMeeting(String meetingId) throws MemberDoesntExistException, WrongIdException;

    void updateMeeting(String id, Meeting meeting);

    void addMeetingPreference(String id, CulinaryPreference culinaryPreference) throws PreferenceAlreadyExistsException;

    void removePreference(String id, String preferenceId);

    void removeComment(String id, String commentId);

    void removeMember(String id, String memberId) throws WrongIdException, PrivilegesMissingException;

    void deleteMeeting(String id) throws WrongIdException;

    Joined checkIfJoined(String meetingId) throws WrongIdException;
}
