package TastyMeeting.utils;

import TastyMeeting.Exceptions.WrongEmailException;
import org.junit.Test;

/**
 * Created by rafal on 5/21/16.
 */
public class EmailHandlerTest {

    @Test(expected = WrongEmailException.class)
    public void shouldThrowExceptionIfDestinationEmailIsNotValid() {
        EmailHandler emailHandler = new EmailHandler();
        emailHandler.sendFromGmail("user", "pass", "notValidEmail", "someSubject", "someBody");
    }

}