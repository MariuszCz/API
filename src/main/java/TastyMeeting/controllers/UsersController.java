package TastyMeeting.controllers;

import TastyMeeting.Exceptions.*;
import TastyMeeting.data.CulinaryPreference;
import TastyMeeting.data.Interest;
import TastyMeeting.data.User;
import TastyMeeting.repositories.interfaces.CulinaryPreferencesDatabase;
import TastyMeeting.repositories.interfaces.InterestsDatabase;
import TastyMeeting.repositories.interfaces.UsersDatabase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by rafal on 3/20/16.
 */
@Component
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/users", description = "Operations about users")
public class UsersController {
    @Autowired
    UsersDatabase usersDatabase;
    @Autowired
    InterestsDatabase interestsDatabase;
    String FRONT_URL = "localhost:8080/index.html";
    @Autowired
    CulinaryPreferencesDatabase culinaryPreferencesDatabase;


    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get user by id", response = User.class)
    public User getUserById(@PathParam("id") String id) {
        return usersDatabase.getUser(id);
    }

    @GET
    @Path("/email/{email}")
    @ApiOperation(value = "Get user by email", response = User.class)
    public String getUserByEmail(@PathParam("email") String email) {
        return usersDatabase.findByLogin(email).getId();
    }

    @GET
    @ApiOperation(value = "Get users", response = User.class, responseContainer = "LIST")
    public List<User> getAllUsers() {
        return usersDatabase.getUsers();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add user", response = User.class)
    public Response addUser(User user) {
        try {
            usersDatabase.addUser(user);
            usersDatabase.sendConfirmationEmail(user.getEmail());
            return Response.status(201).build();
        } catch (WrongEmailException ex) {
            return Response.status(400).build();
        } catch (DuplicationException ex) {
            return Response.status(409).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(400).build();
        }
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update user by id", response = User.class)
    public Response updateUser(@PathParam("id") String id, User user) {
        if (user.getGender() == null || usersDatabase.checkGender(user.getGender())) {
            usersDatabase.updateUser(id, user);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Delete user", response = User.class)
    public Response deleteUser(@PathParam("id") String Id) {
        try {
            usersDatabase.deleteUser(Id);
            return Response.ok().build();
        } catch (WrongIdException e) {
            return Response.status(404).build();
        }
    }

    @Path("/interests")
    @GET
    @ApiOperation(value = "Get all intersts", response = User.class)
    public List<Interest> getAllInterests() {
        return interestsDatabase.getInterests();
    }

    @GET
    @Path("/interests/{interestId}")
    @ApiOperation(value = "Get interest by id", response = User.class)
    public Interest getInterestById(@PathParam("interestId") String id) {
        return interestsDatabase.getInterest(id);
    }

    @POST
    @Path("/interests")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add interest", response = User.class)
    public Response addInterest(Interest interest) {
        if (!interestsDatabase.checkIfInterestExists(interest.getInterestName())) {
            interestsDatabase.addInterest(interest);
            return Response.status(201).build();
        } else {
            return Response.status(400).build();
        }
    }

    @PUT
    @Path("/interests/{interestId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update interest", response = User.class)
    public Response updateInterest(@PathParam("interestId") String id, Interest interest) {
        interestsDatabase.updateInterest(id, interest);
        return Response.ok().build();
    }

    @DELETE
    @Path("/interests/{interestId}")
    @ApiOperation(value = "Delete interest", response = User.class)
    public Response deleteInterest(@PathParam("interestId") String Id) {
        try {
            interestsDatabase.deleteInterest(Id);
            return Response.ok().build();
        } catch (WrongIdException e) {
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/culinaryPreferences/{culinaryPreferenceId}")
    @ApiOperation(value = "Get culinary preference by id", response = User.class)
    public CulinaryPreference getCulinaryPreferenceById(@PathParam("culinaryPreferenceId") String id) {
        return culinaryPreferencesDatabase.getCulinaryPreference(id);
    }

    @GET
    @Path("/culinaryPreferences")
    @ApiOperation(value = "Get culinary preferences", response = User.class)
    public List<CulinaryPreference> getAllCulinaryPreferences() {

        return culinaryPreferencesDatabase.getCulinaryPreferences();
    }

    @POST
    @Path("/culinaryPreferences")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add culinary preference", response = User.class)
    public Response addCulinaryPreference(CulinaryPreference culinaryPreference) {
        if (!culinaryPreferencesDatabase.checkIfPreferenceExists(culinaryPreference.getCulinaryPreferenceName())) {
            culinaryPreferencesDatabase.addCulinaryPreference(culinaryPreference);
            return Response.status(201).build();
        } else {
            return Response.status(400).build();
        }
    }

    @Path("/culinaryPreferences/{culinaryPreferenceId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update culinary preference", response = User.class)
    public Response updateCulinaryPreference(@PathParam("culinaryPreferenceId") String id, CulinaryPreference culinaryPreference) {
        culinaryPreferencesDatabase.updateCulinaryPreference(id, culinaryPreference);
        return Response.ok().build();
    }

    @DELETE
    @Path("/culinaryPreferences/{culinaryPreferenceId}")
    @ApiOperation(value = "Update culinary preference", response = User.class)
    public Response deleteCulinaryPreference(@PathParam("culinaryPreferenceId") String Id) {
        try {
            culinaryPreferencesDatabase.deleteCulinaryPreference(Id);
            return Response.ok().build();
        } catch (WrongIdException e) {
            return Response.status(404).build();
        }
    }

    @Path("/interest/{userId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add user interest", response = Interest.class)
    public Response addUserInterest(@PathParam("userId") String id, Interest interest) throws InterestAlreadyExistsException {
        usersDatabase.addUserInterest(id, interest);
        return Response.ok().build();
    }

    @Path("/preference/{userId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add user culinary preference", response = CulinaryPreference.class)
    public Response addUserPreference(@PathParam("userId") String id, CulinaryPreference culinaryPreference) throws PreferenceAlreadyExistsException {
        usersDatabase.addUserPreference(id, culinaryPreference);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{userId}/preference/{preferenceId}")
    @ApiOperation(value = "Remove user culinary preference", response = CulinaryPreference.class)
    public Response removePreference(@PathParam("userId") String id, @PathParam("preferenceId") String preferenceId) {
        usersDatabase.removePreference(id, preferenceId);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{userId}/interest/{interestId}")
    @ApiOperation(value = "Remove user interest", response = User.class)
    public Response removeInterest(@PathParam("userId") String id, @PathParam("interestId") String interestId) {
        usersDatabase.removeInterest(id, interestId);
        return Response.ok().build();
    }

    @GET
    @Path("/activate/{token}")
    public Response activateUser(@PathParam("token") String token) {
        try {
            System.out.println("Activating user");
            usersDatabase.activateUser(token);
            return Response.status(301).location(new URI(FRONT_URL)).build();
//            return Response.ok().build();
        } catch (WrongIdException e) {
            //TODO redirect to front?
            return Response.status(404).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @GET
    @Path("/me")
    public Response loggedUserInfo() {
        User loggedUser = usersDatabase.getCurrentlyLoggedUser();
        return Response.ok(loggedUser).build();
    }

}
