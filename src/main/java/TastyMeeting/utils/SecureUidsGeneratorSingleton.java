package TastyMeeting.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by rafal on 5/4/16.
 */
public final class SecureUidsGeneratorSingleton {
    private static SecureUidsGeneratorSingleton secureUidsGeneratorSingletonSingleton = new SecureUidsGeneratorSingleton();
    private SecureRandom random = new SecureRandom();

    private SecureUidsGeneratorSingleton() {
    }

    public static SecureUidsGeneratorSingleton getInstance() {
        return secureUidsGeneratorSingletonSingleton;
    }

    /**
     * This works by choosing 130 bits from a cryptographically secure random bit generator,
     * and encoding them in base-32.
     * 128 bits is considered to be cryptographically strong,
     * but each digit in a base 32 number can encode 5 bits,
     * so 128 is rounded up to the next multiple of 5.
     * This encoding is compact and efficient, with 5 random bits per character.
     * Compare this to a random UUID, which only has 3.4 bits per character in standard layout,
     * and only 122 random bits in total.
     *
     * @return random, strong, solid String UID
     * @see http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
     */
    public String generateUID() {
        return new BigInteger(130, random).toString(32);
    }
}
