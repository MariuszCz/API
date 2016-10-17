package TastyMeeting.configurations;

import TastyMeeting.controllers.MeetingsController;
import TastyMeeting.controllers.SocialController;
import TastyMeeting.controllers.UsersController;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.wadl.internal.WadlResource;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        registerEndpoints();
        configureSwagger();
    }


    //TODO
    // It's probably an old way to configure swagger, lets find out what's new one
    private void configureSwagger() {
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("TastyMeeting.controllers");
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
    }

    private void registerEndpoints() {
        register(WadlResource.class);
        register(UsersController.class);
        register(MeetingsController.class);
        register(SocialController.class);
    }
}