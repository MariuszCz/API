package TastyMeeting.data;

import io.swagger.annotations.ApiModel;

/**
 * Created by mariusz on 03.05.16.
 */
@ApiModel(value = "Interest")
public class Interest {

    private String Id;

    private String interestName;

    public Interest(String Id, String interestName) {
        this.Id = Id;
        this.interestName = interestName;
    }

    public Interest() {
    }

    public Interest(Interest interest) {
        super();
        this.Id = interest.getId();
        this.interestName = interest.getInterestName();
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
