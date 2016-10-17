package TastyMeeting.configurations;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "TastyMeeting.repositories")
class SpringMongoConfig extends AbstractMongoConfiguration {
    private final String MONGO_URI = "mongodb://tasty:tasty123@ds015720.mlab.com:15720/tasty_meeting";

    @Override
    protected String getDatabaseName() {
        return "tasty_meeting";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(new MongoClientURI(MONGO_URI));
    }

    @Override
    protected String getMappingBasePackage() {
        return "TastyMeeting.repositories";
    }
}