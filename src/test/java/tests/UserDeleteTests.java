package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assetions;
import lib.BaseTestCase;
import lib.DataGererator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("User Data delete cases")
@Feature("User Data delete")
public class UserDeleteTests extends BaseTestCase {

    public static final String ANOTHER_USER_ID = "71084";
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Негативный Тест попытку удалить пользователя по ID 2. Его данные для авторизации")
    @DisplayName("Негативный Тест попытку удалить пользователя по ID 2. Его данные для авторизации")
    public void testDeleteUserDataAuth() {


        Map<String, String> authData = new HashMap<>();

        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseAuth, "x-csrf-token");
        String cookie = this.getCookie(responseAuth, "auth_sid");


        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/2",header,cookie);


        //Тестируем попытку авторизации
        Assetions.assertResponseCodeEquals(responseAuth, 200);

        //Тестируем попытку удаления
        Assetions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assetions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description(" Создать пользователя, авторизоваться из-под него, удалить, затем попробовать получить его данные по ID и убедиться, что пользователь действительно удален")
    @DisplayName(" Создать пользователя, авторизоваться из-под него, удалить, затем попробовать получить его данные по ID и убедиться, что пользователь действительно удален")
    public void testDeleteSelfUserDataAuth() {
        //Создаем данные для учетки
        String email = DataGererator.getRandomEmail();

        Map<String, String> userData = new HashMap<>();

        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        //Заводим учетку
        Response response = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        //Записываем полученный id для того, чтобы в дальнейшем обратиться
        String id = response.jsonPath().getString("id");


        //Логинимся
        Map<String, String> authData = new HashMap<>();

        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));


        Response responseAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);


        String header = this.getHeader(responseAuth, "x-csrf-token");
        String cookie = this.getCookie(responseAuth, "auth_sid");


        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/2",header,cookie);


        Response responseUserData = RestAssured.
                get("https://playground.learnqa.ru/api/user/" + id)
                .andReturn();

        //Тестируем какой ответ пришел при запросе на удаление
        Assetions.assertResponseCodeEquals(responseDeleteUser, 200);

        //Тестируем что действительно удалили (пытаемся обратиться)
        Assetions.assertResponseCodeEquals(responseUserData, 400);
        Assetions.assertResponseTextEquals(responseUserData, "User not found");

    }

    @Test
    @Description(" негативный, попробовать удалить пользователя, будучи авторизованными другим пользователем.")
    @DisplayName(" негативный, попробовать удалить пользователя, будучи авторизованными другим пользователем.")
    public void testDeleteAnotherUserDataAuth() {
        //Создаем данные для учетки
        String email = DataGererator.getRandomEmail();

        Map<String, String> userData = new HashMap<>();

        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        //Заводим учетку
        Response response = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        //Записываем полученный id для того, чтобы в дальнейшем обратиться
        String oneUserId = response.jsonPath().getString("id");


        //Создаем данные для второй учетки
        String emailTwo = DataGererator.getRandomEmail();

        Map<String, String> userData2 = new HashMap<>();

        userData2.put("email", emailTwo);
        userData2.put("password", "321");
        userData2.put("username", "learnqa2");
        userData2.put("firstName", "learnqa2");
        userData2.put("lastName", "learnqa2");

        //Заводим  вторую учетку
        Response responseTwo = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData2);

        //Записываем полученный id второй учетки для того, чтобы в дальнейшем обратиться
        String twoUserId = responseTwo.jsonPath().getString("id");


        //Логинимся под первой учеткой
        Map<String, String> authData = new HashMap<>();

        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));


        Response responseAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);


        String header = this.getHeader(responseAuth, "x-csrf-token");
        String cookie = this.getCookie(responseAuth, "auth_sid");


        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + twoUserId,header,cookie);

        responseDeleteUser.prettyPrint();


        Response responseUserDataTwo = RestAssured.
                get("https://playground.learnqa.ru/api/user/" + twoUserId)
                .andReturn();


        //Тестируем что действительно удалили (пытаемся обратиться)
        Assetions.assertResponseCodeEquals(responseUserDataTwo, 200);
        Assetions.assertJsonHasField(responseUserDataTwo, "username");

    }
}
