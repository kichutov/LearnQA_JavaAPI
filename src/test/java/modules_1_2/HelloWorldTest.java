package modules_1_2;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    @Test
    public void testHelloWorld() {
        System.out.println("Hello from Vladislav");
    }

    @Test
    public void testGet() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }
}
