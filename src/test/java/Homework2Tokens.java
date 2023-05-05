import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Homework2Tokens {

    @Test
    public void tokens() throws InterruptedException {
        JsonPath createTaskAnswer = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        int seconds = createTaskAnswer.get("seconds");
        String token = createTaskAnswer.get("token");

        JsonPath notReadyAnswer = RestAssured
                .given()
                .param("token", token)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        Assertions.assertEquals("Job is NOT ready", notReadyAnswer.get("status").toString());

        Thread.sleep(seconds*1000L);

        JsonPath completedTaskAnswer = RestAssured
                .given()
                .param("token", token)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        Assertions.assertEquals("Job is ready", completedTaskAnswer.get("status").toString());
    }
}
