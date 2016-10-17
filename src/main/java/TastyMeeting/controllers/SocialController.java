package TastyMeeting.controllers;

import TastyMeeting.data.BearerToken;
import TastyMeeting.data.FacebookToken;
import TastyMeeting.repositories.interfaces.UsersDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by rafal on 4/24/16.
 */
@Component
@Path("/social")
@Produces(MediaType.APPLICATION_JSON)
public class SocialController {
    @Autowired
    UsersDatabase usersDatabase;
    @Autowired
    ConnectionFactoryLocator factoryLocator;
    @Autowired
    ConnectionRepository connectionRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login/facebook")
    public Response facebookLogin(FacebookToken facebookToken) {
        String token = facebookToken.token;
        Facebook facebook = new FacebookTemplate(token);
        OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) factoryLocator.getConnectionFactory("facebook");
        Connection<?> connection = connectionFactory.createConnection(new AccessGrant(token));
        try {
            BearerToken bearerToken = usersDatabase.facebookLogin(connection, facebook);
            return Response.ok().entity(bearerToken).build();
        } catch (IOException e) {
            //TODO
            return Response.status(401).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }
}