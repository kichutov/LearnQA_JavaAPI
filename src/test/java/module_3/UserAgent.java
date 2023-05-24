package module_3;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAgent {

    private static final String URL = "https://playground.learnqa.ru/ajax/api/user_agent_check";

    private static final String USER_AGENT_1 = "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
    private static final String USER_AGENT_2 = "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1";
    private static final String USER_AGENT_3 = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    private static final String USER_AGENT_4 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0";
    private static final String USER_AGENT_5 = "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1";

    private static final Map<String, Map<String, String>> map = new HashMap<>();

    static {
        map.put(USER_AGENT_1, Map.of("platform", "Mobile", "browser", "No", "device", "Android"));
        map.put(USER_AGENT_2, Map.of("platform", "Mobile", "browser", "Chrome", "device", "iOS"));
        map.put(USER_AGENT_3, Map.of("platform", "Googlebot", "browser", "Unknown", "device", "Unknown"));
        map.put(USER_AGENT_4, Map.of("platform", "Web", "browser", "Chrome", "device", "No"));
        map.put(USER_AGENT_5, Map.of("platform", "Mobile", "browser", "No", "device", "iPhone"));
    }

    @ParameterizedTest
    @ValueSource(strings = {USER_AGENT_1, USER_AGENT_2, USER_AGENT_3, USER_AGENT_4, USER_AGENT_5})
    void userAgent(String userAgent) {
        JsonPath response = RestAssured
                .given()
                .header("User-Agent", userAgent)
                .get(URL)
                .jsonPath();

        Map<String, String> userAgentParamsMap = map.get(response.get("user_agent"));

        assertTrue(response.get("platform").equals(userAgentParamsMap.get("platform")),"Incorrect platform" );
        assertTrue(response.get("browser").equals(userAgentParamsMap.get("browser")),"Incorrect browser" );
        assertTrue(response.get("device").equals(userAgentParamsMap.get("device")),"Incorrect device" );
    }
}
