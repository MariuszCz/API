package TastyMeeting.repositories;

import TastyMeeting.Exceptions.*;
import TastyMeeting.data.*;
import TastyMeeting.repositories.interfaces.MeetingsDatabase;
import TastyMeeting.repositories.interfaces.UsersDatabase;
import TastyMeeting.repositories.mongo.CommentRepository;
import TastyMeeting.repositories.mongo.CulinaryPreferenceRepository;
import TastyMeeting.repositories.mongo.MeetingRepository;
import com.google.common.collect.Lists;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.apache.commons.logging.Log;
import org.bson.Document;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rafal on 5/23/16.
 */
public class MeetingsMongoDatabase implements MeetingsDatabase {
    MeetingRepository meetingRepository;
    MongoCollection<Document> meetingsCollection;
    CommentRepository commentRepository;
    UsersDatabase usersDatabase;
    CulinaryPreferenceRepository culinaryPreferenceRepository;
    public MeetingsMongoDatabase(MeetingRepository meetingRepository,
                                 MongoCollection<Document> meetingsCollection,
                                 CommentRepository commentRepository,
                                 UsersDatabase usersDatabase,
                                 CulinaryPreferenceRepository culinaryPreferenceRepository) {
        this.meetingRepository = meetingRepository;
        this.meetingsCollection = meetingsCollection;
        this.commentRepository = commentRepository;
        this.usersDatabase = usersDatabase;
        this.culinaryPreferenceRepository = culinaryPreferenceRepository;
    }

    @Override
    public List<Meeting> getMeetings() {
        return meetingRepository.findAll();
    }

    @Override
    public List<Meeting> findByLocation(Point point, Distance distance) {
        //Point point = new Point(55,16);
       // Distance distance = new Distance(100, Metrics.KILOMETERS);
        List<Meeting> found = meetingRepository.findByLocationNear(point,distance);
        return found;
    }
    @Override
    public void addMeeting(Meeting meeting) {
        meeting.setAuthor(getLoggedUser());
        meetingRepository.save(meeting);
    }

    @Override
    public Meeting getMeeting(String id) {
        return meetingRepository.findOne(id);
    }

    @Override
    public Meeting findByTitle(String title) {
        return meetingRepository.findByTitle(title);
    }

    @Override
    public void addComment(String id, Comment comment) {
        addCommentAuthor(comment);

        comment = addCommentToDatabase(comment);

        Meeting meeting = getMeeting(id);
        ArrayList<Comment> comments = meeting.getComments();
        comments.add(comment);
        meeting.setComments(comments);

        updateMeeting(id, meeting);
    }

    private Comment addCommentToDatabase(Comment comment) {
        return commentRepository.save(comment);
    }

    private void addCommentAuthor(Comment comment) {
        User user = getLoggedUser();
        comment.setAuthor(user);
    }

    private User getLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean isUsernameValidEmail(String email) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(regex);
    }

    @Override
    public void addMember(String id, String memberName) throws MemberAlreadyExistsException, WrongEmailException, PrivilegesMissingException {
        Meeting currentMeeting = getMeeting(id);
        User member = usersDatabase.findByLogin(memberName);

        User loggedUser = getLoggedUser();

        if (!loggedUser.equals(currentMeeting.getAuthor()) && !loggedUser.getRoles().contains("ROLE_ADMIN"))
            throw new PrivilegesMissingException();

        if (member == null)
            throw new WrongEmailException();

        if (userExistsInMeeting(currentMeeting, member))
            throw new MemberAlreadyExistsException();

        ArrayList<User> members = currentMeeting.getMembers();
        members.add(member);
        currentMeeting.setMembers(members);
        updateMeeting(id, currentMeeting);
    }

    private boolean userExistsInMeeting(Meeting meeting, User loggedUser) {
        return meeting.getMembers() != null && meeting.getMembers().contains(loggedUser);
    }

    @Override
    public void joinToMeeting(String id) throws MemberAlreadyExistsException, WrongIdException {
        Meeting currentMeeting = getMeeting(id);
        User loggedUser = getLoggedUser();

        if (currentMeeting == null)
            throw new WrongIdException();

        if (userExistsInMeeting(currentMeeting, loggedUser))
            throw new MemberAlreadyExistsException();


        ArrayList<User> members = currentMeeting.getMembers();
        members.add(loggedUser);
        currentMeeting.setMembers(members);

        updateMeeting(id, currentMeeting);
    }

    @Override
    public void leaveMeeting(String meetingId) throws MemberDoesntExistException, WrongIdException {
        Meeting meeting = getMeeting(meetingId);
        User loggedUser = getLoggedUser();

        if (meeting == null)
            throw new WrongIdException();

        if (!userExistsInMeeting(meeting, loggedUser))
            throw new MemberDoesntExistException();

        ArrayList<User> members = meeting.getMembers();
        members.removeIf(user -> user.equals(loggedUser));
        meeting.setMembers(members);

        updateMeeting(meetingId, meeting);
    }

    @Override
    public Joined checkIfJoined(String meetingId) throws WrongIdException {
        Meeting meeting = getMeeting(meetingId);
        User loggedUser = getLoggedUser();

        if (meeting == null)
            throw new WrongIdException();

        Joined joined = new Joined();
        joined.setJoined(userExistsInMeeting(meeting, loggedUser));

        return joined;
    }

    @Override
    public void updateMeeting(String id, Meeting meeting) {
//        if (!isUsernameValidEmail(meeting.getAuthor()) || !isUsernameValidEmail(meeting.getInitiator())) {
//            throw new WrongEmailException();
//        }
        meeting.setId(id);
        meeting.setAuthor(getLoggedUser());
        meetingRepository.save(meeting);
    }

    @Override
    public void addMeetingPreference(String id, CulinaryPreference culinaryPreference) throws PreferenceAlreadyExistsException {
        Meeting currentMeeting = getMeeting(id);

        CulinaryPreference culinaryPreferenceToAdd = culinaryPreferenceRepository.findByCulinaryPreferenceName(culinaryPreference.getCulinaryPreferenceName());

        if (culinaryPreferenceToAdd == null)
            culinaryPreferenceToAdd = addPreferenceToDatabase(culinaryPreference);


        addPreferenceToDatabase(culinaryPreference);
        ArrayList<CulinaryPreference> currentCulinaryPreferences = currentMeeting.getCulinaryPreferences();
        currentCulinaryPreferences.add(culinaryPreference);
        currentMeeting.setCulinaryPreferences(currentCulinaryPreferences);

        updateMeeting(id, currentMeeting);
    }

    private CulinaryPreference addPreferenceToDatabase(CulinaryPreference culinaryPreference) {
        return culinaryPreferenceRepository.save(culinaryPreference);
    }

    @Override
    public void removePreference(String id, String preferenceId) {
        Meeting meeting = getMeeting(id);
        ArrayList<CulinaryPreference> culinaryPreferences = meeting.getCulinaryPreferences();
        culinaryPreferences.removeIf(preference -> preference.getId() != null && preference.getId().equals(preferenceId));
        meeting.setCulinaryPreferences(culinaryPreferences);
        meetingRepository.save(meeting);
    }

    @Override
    public void removeComment(String id, String commentId) {
        Meeting meeting = getMeeting(id);
        ArrayList<Comment> comments = meeting.getComments();

        List<Comment> commentsToDelete = new ArrayList<>();

        comments.stream().forEach(comment -> {
            if (comment.getId().equals(commentId))
                commentsToDelete.add(comment);
        });

        comments.removeAll(commentsToDelete);
        meeting.setComments(comments);
        meetingRepository.save(meeting);
    }

    @Override
    public void removeMember(String id, String memberId) throws WrongIdException, PrivilegesMissingException {
        Meeting meeting = getMeeting(id);

        if (meeting == null)
            throw new WrongIdException();

        if (!meeting.getAuthor().equals(getLoggedUser()))
            throw new PrivilegesMissingException();

        ArrayList<User> members = meeting.getMembers();
        members.removeIf(user -> user.getId().equals(memberId));
        meeting.setMembers(members);
        updateMeeting(id, meeting);
    }

    @Override
    public void deleteMeeting(String id) throws WrongIdException {
        meetingRepository.delete(id);
    }
}