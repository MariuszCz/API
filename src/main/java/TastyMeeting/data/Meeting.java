package TastyMeeting.data;

import io.swagger.annotations.ApiModel;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.omg.PortableInterceptor.LOCATION_FORWARD;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mariusz on 04.05.16.
 */

@ApiModel(value = "Meeting")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meeting {
    @Id
    private String id;
    private String title;
    private String description;
    private User author;
    private ArrayList<User> members = new ArrayList<>();
    private String memberName;
    private String city;
    private String street;
    private String locationNumber;
    private String zipCode;
    private Location location;

    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<CulinaryPreference> culinaryPreferences = new ArrayList<>();
    private String meetingAccess;
    private Date meetingTime;

    @PersistenceConstructor
    public Meeting(String id, String title, String description, User author, ArrayList members, String memberName, String city, String street, String locationNumber, String zipCode,  ArrayList<Comment> comments, ArrayList<CulinaryPreference> culinaryPreferences, String meetingAccess, Date meetingTime, Location location) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.members = members;
        this.memberName = memberName;
        this.city = city;
        this.street = street;
        this.locationNumber = locationNumber;
        this.zipCode = zipCode;

        this.comments = comments;
        this.culinaryPreferences = culinaryPreferences;
        this.meetingAccess = meetingAccess;
        this.meetingTime = meetingTime;
        this.location = location;
    }

    public Meeting() {
        super();
        this.title = getTitle();
        this.description = getDescription();
        this.members = getMembers();
        this.memberName = getMemberName();
        this.comments = getComments();
        this.members = getMembers();
        this.meetingAccess = getMeetingAccess();
        this.meetingAccess = getMeetingAccess();
        this.city = getCity();
        this.street = getStreet();
        this.locationNumber = getLocationNumber();
        this.zipCode = getZipCode();

        this.location = getLocation();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public ArrayList<CulinaryPreference> getCulinaryPreferences() {
        return culinaryPreferences;
    }

    public void setCulinaryPreferences(ArrayList<CulinaryPreference> culinaryPreferences) {
        this.culinaryPreferences = culinaryPreferences;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMeetingAccess() {
        return meetingAccess;
    }

    public void setMeetingAccess(String meetingAccess) {
        this.meetingAccess = meetingAccess;
    }



    public Date getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(Date meetingTime) {
        this.meetingTime = meetingTime;
    }
}
