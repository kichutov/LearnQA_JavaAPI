package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assetions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("User Data get cases")
@Feature("User Data get")
public class UserGetTest extends BaseTestCase {

    public static final String ANOTHER_USER_ID = "1";
    public static final String MY_ID = "2";

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Тест из урока получение данных")
    @DisplayName("Тест из урока получение данных")
    public void testGetAnotherUserDataNotAuth(){
        Response responseUserData = RestAssured.
                get("https://playground.learnqa.ru/api/user/" + MY_ID)
                .andReturn();

        Assetions.assertJsonHasField(responseUserData,"username");
        Assetions.assertJsonNotField(responseUserData,"firstName");
        Assetions.assertJsonNotField(responseUserData,"lastName");
        Assetions.assertJsonNotField(responseUserData,"email");
    }

    @Test
    @Description("Тест Запрос данных другого пользователя")
    @DisplayName("Test Запрос данных другого пользователя")

    public void testGetAnotherUserDataAuth(){

        Map<String, String> authData = new HashMap<>();

        authData.put("email","vinkotov@example.com");
        authData.put("password", "1234");


        Response responseAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        String header = this.getHeader(responseAuth, "x-csrf-token");
        String cookie = this.getCookie(responseAuth,"auth_sid");

        Response responseMyUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + MY_ID,
                header,cookie);

        String myUsername = responseMyUserData.jsonPath().getString("username");

        Response responseAnotherUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + ANOTHER_USER_ID
                ,header,cookie);

        String[] unExpectedFields = {"firstName", "lastName", "email"};

        Assetions.assertJsonNotFields(responseAnotherUserData,unExpectedFields);
        Assetions.assertJsonHasField(responseAnotherUserData,"username");

        Assetions.assertCheckDifferentValue(responseAnotherUserData,myUsername, "username");

    }
}
