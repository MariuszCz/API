package TastyMeeting.data;

import org.springframework.data.annotation.Id;

/**
 * Created by rafal on 5/21/16.
 */
public class ConfirmationToken {
    @Id
    String id;
    String userId;
    String token;
    boolean enabled = false;

    public ConfirmationToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
