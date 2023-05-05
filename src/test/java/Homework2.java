import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Homework2 {

    @Test
    public void parsingJSON() {

        JsonPath jsonPath = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        List<Object> messages = jsonPath.get("messages");
        System.out.println(messages.get(1));
    }
}
