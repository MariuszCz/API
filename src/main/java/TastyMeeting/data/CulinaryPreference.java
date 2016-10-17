package TastyMeeting.data;

import io.swagger.annotations.ApiModel;

/**
 * Created by mariusz on 03.05.16.
 */
@ApiModel(value = "CulinaryPreference")
public class CulinaryPreference {
    @org.springframework.data.annotation.Id
    private String Id;

    private String culinaryPreferenceName;

    public CulinaryPreference(String Id, String culinaryPreferenceName) {
        this.Id = Id;
        this.culinaryPreferenceName = culinaryPreferenceName;
    }

    public CulinaryPreference() {
    }

    public CulinaryPreference(CulinaryPreference culinaryPreference) {
        this.Id = culinaryPreference.getId();
        this.culinaryPreferenceName = culinaryPreference.getCulinaryPreferenceName();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCulinaryPreferenceName() {
        return culinaryPreferenceName;
    }

    public void setCulinaryPreferenceName(String culinaryPreferenceName) {
        this.culinaryPreferenceName = culinaryPreferenceName;
    }


}
