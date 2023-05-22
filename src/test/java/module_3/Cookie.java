package module_3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Cookie {

    private static final String URL = "https://playground.learnqa.ru/api/homework_cookie";

    @Test
    void cookie() {
        Response response = RestAssured
                .get(URL)
                .andReturn();
        assertTrue(response.getCookies().containsKey("HomeWork"), "Ответ не содержит cookie 'HomeWork'");
        assertEquals("hw_value", response.getCookie("HomeWork"), "Cookie 'HomeWork' не равна 'hw_value'");
    }
}
