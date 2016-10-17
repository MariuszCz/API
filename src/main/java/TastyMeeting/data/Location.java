package TastyMeeting.data;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by mariusz on 14.10.16.
 */
@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    public Location(List<String> coordinates, String type) {
        this.coordinates = coordinates;
        this.type = type;
    }
    public Location() {
        this.coordinates = getCoordinates();
        this.type = getType();
    }
    private List<String> coordinates;
    private String type;
    public List<String> getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(List<String> coordinates) {
        this.coordinates = coordinates;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}