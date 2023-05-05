import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.api.Test;

public class Homework2Redirect {

    @Test
    public void redirect() {
        Headers headers = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .headers();

        Header location = headers.get("location");
        System.out.println(location.getValue());
    }
}
