package module_3;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Headers {

    private static final String URL = "https://playground.learnqa.ru/api/homework_header";

    @Test
    void headers() {
        Response response = RestAssured
                .get(URL)
                .andReturn();
        assertTrue(response.getHeaders().hasHeaderWithName("x-secret-homework-header"), "Ответ не содержит header 'x-secret-homework-header'");
        assertEquals("Some secret value", response.getHeader("x-secret-homework-header"), "Header 'x-secret-homework-header' не равен 'Some secret value'");
    }
}
