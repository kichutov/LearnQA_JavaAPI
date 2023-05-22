package module_3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortPhraseTest {

    private final static String LONG_STRING = "THIS_STRING_IS_LONGER_THAN_15_CHARACTERS";

    @Test
    void shortPhraseTest() {
        assertTrue(LONG_STRING.length() > 15, "String should be longer than 15 characters");
    }
}
