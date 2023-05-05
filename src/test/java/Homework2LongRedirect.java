import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Homework2LongRedirect {

    @Test
    public void longRedirect() {

        int redirectCount = 0;
        boolean isRedirect = true;
        String currentUrl = "https://playground.learnqa.ru/api/long_redirect";

        while (isRedirect) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(currentUrl);
            if (response.getStatusCode() == 301) {
                redirectCount++;
                currentUrl = response.header("location");
            } else {
                isRedirect = false;
            }
        }

        System.out.println("Количество редиректов: " + redirectCount);
        System.out.println("Итоговый адрес: " + currentUrl);
    }
}
