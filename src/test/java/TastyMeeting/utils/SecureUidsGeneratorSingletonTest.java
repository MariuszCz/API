package TastyMeeting.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by rafal on 5/4/16.
 */
public class SecureUidsGeneratorSingletonTest {

    @Test
    public void shouldGetInstanceReturnSameInstanceWhenCallingItMultipleTimes() {
        SecureUidsGeneratorSingleton secureUidsGeneratorSingleton1 = SecureUidsGeneratorSingleton.getInstance();
        SecureUidsGeneratorSingleton secureUidsGeneratorSingleton2 = SecureUidsGeneratorSingleton.getInstance();

        Assert.assertEquals(true, secureUidsGeneratorSingleton1 == secureUidsGeneratorSingleton2);
    }

    @Test
    public void shouldGenerateUidCorrectly() {
        SecureUidsGeneratorSingleton secureUidsGeneratorSingleton = SecureUidsGeneratorSingleton.getInstance();

        Assert.assertNotNull(secureUidsGeneratorSingleton.generateUID());
    }
}
