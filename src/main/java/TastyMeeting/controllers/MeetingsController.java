package TastyMeeting.controllers;

import TastyMeeting.Exceptions.*;
import TastyMeeting.data.Comment;
import TastyMeeting.data.CulinaryPreference;
import TastyMeeting.data.Joined;
import TastyMeeting.data.Meeting;
import TastyMeeting.repositories.interfaces.MeetingsDatabase;
import com.mongodb.DBObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

import javax.validation.constraints.AssertFalse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by mariusz on 04.05.16.
 */
@Component
@Path("/meetings")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/meetings", description = "Operations about meetings")
public class MeetingsController {
    @Context
    private UriInfo uriInfo;

    @Autowired
    MeetingsDatabase meetingsDatabase;

    @GET
    @Path("/tomcat-test")
    public String tomcatTest() {
        return "ww";
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get meeting by id", response = Meeting.class)
    public Meeting getMeetingById(@PathParam("id") String id) {
        return meetingsDatabase.getMeeting(id);
    }

    @GET
    @ApiOperation(value = "Get all meetings", response = Meeting.class, responseContainer = "LIST")
    public List<Meeting> getAllMeetings() {
        return meetingsDatabase.getMeetings();
    }

    @Path("/location")
    @GET
    @ApiOperation(value = "Get meetings by user location", response = Meeting.class, responseContainer = "LIST")
    public List<Meeting> getMeetingsByLocation() {
        return meetingsDatabase.findByLocation(new Point(52.4,16.9),new Distance(10000,Metrics.KILOMETERS));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add meeting", response = Meeting.class)
    public Response addMeeting(Meeting meeting) {
        meetingsDatabase.addMeeting(meeting);
        Meeting meetingEntity = meetingsDatabase.findByTitle(meeting.getTitle());
        return Response.created(URI.create(uriInfo.getPath() + "/" + meetingEntity.getId())).entity(meetingEntity).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Delete meeting", response = Meeting.class)
    public Response deleteMeeting(@PathParam("id") String id) {
        try {
            meetingsDatabase.deleteMeeting(id);
            return Response.ok().build();
        } catch (WrongIdException e) {
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/title/{title}")
    @ApiOperation(value = "Get meeting by title", response = Meeting.class)
    public Meeting getByTitle(@PathParam("title") String title) {
        return meetingsDatabase.findByTitle(title);
    }

    @Path("/comment/{meetingId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add comment to meeting", response = Meeting.class)
    public Response addComment(@PathParam("meetingId") String meetingId, Comment comment) {
        meetingsDatabase.addComment(meetingId, comment);
        return Response.ok().build();
    }

    @Path("/member/{meetingId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add member to meeting", response = Meeting.class)
    public Response addMember(@PathParam("meetingId") String id, Meeting meeting) throws MemberAlreadyExistsException {
        try {
            String memberName = meeting.getMemberName();
            meetingsDatabase.addMember(id, memberName);
            return Response.ok().build();
        } catch (WrongEmailException ex) {
            return Response.status(404).build();
        } catch (MemberAlreadyExistsException ex) {
            return Response.status(409).build();
        } catch (PrivilegesMissingException e) {
            return Response.status(403).build();
        }
    }


    @POST
    @Path("/member/{meetingId}/me")
    public Response joinToMeeting(@PathParam("meetingId") String meetingId) {
        try {
            meetingsDatabase.joinToMeeting(meetingId);
            return Response.ok().build();
        } catch (MemberAlreadyExistsException e) {
            return Response.status(409).build();
        } catch (WrongIdException e) {
            return Response.status(404).build();
        }
    }

    @DELETE
    @Path("/member/{meetingId}/me")
    public Response leaveMeeting(@PathParam("meetingId") String meetingId) {
        try {
            meetingsDatabase.leaveMeeting(meetingId);
            return Response.ok().build();
        } catch (MemberDoesntExistException e) {
            e.printStackTrace();
            return Response.status(400).build();
        } catch (WrongIdException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/member/{meetingId}/me")
    public Response checkIfJoined(@PathParam("meetingId") String meetingId) {
        try {
            Joined joined;
            joined = meetingsDatabase.checkIfJoined(meetingId);
            return Response.ok(joined).build();
        } catch (WrongIdException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }

    }


    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update meeting", response = Meeting.class)
    public Response updateMeeting(@PathParam("id") String id, Meeting meeting) {
        meetingsDatabase.updateMeeting(id, meeting);
        return Response.ok().build();
    }

    @Path("/preference/{meetingId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add culinary preference to meeting", response = Meeting.class)
    public Response addMeetingPreference(@PathParam("meetingId") String id, CulinaryPreference culinaryPreference) throws PreferenceAlreadyExistsException {
        meetingsDatabase.addMeetingPreference(id, culinaryPreference);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{meetingId}/preference/{preferenceId}")
    @ApiOperation(value = "Remove culinary preference from meeting", response = Meeting.class)
    public Response removePreference(@PathParam("meetingId") String id, @PathParam("preferenceId") String preferenceId) {
        meetingsDatabase.removePreference(id, preferenceId);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{meetingId}/comment/{commentId}")
    @ApiOperation(value = "Remove comment from meeting", response = Meeting.class)
    public Response removeComment(@PathParam("meetingId") String id, @PathParam("commentId") String commentId) {
        meetingsDatabase.removeComment(id, commentId);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{meetingId}/member/{memberId}")
    @ApiOperation(value = "Remove member from meeting")
    public Response removeMember(@PathParam("meetingId") String id, @PathParam("memberId") String memberId) {
        try {
            meetingsDatabase.removeMember(id, memberId);
            return Response.status(204).build();
        } catch (WrongIdException e) {
            return Response.status(404).build();
        } catch (PrivilegesMissingException e) {
            return Response.status(403).build();
        }
    }

}
