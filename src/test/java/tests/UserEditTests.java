package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTests extends BaseTestCase {

    public static final String ANOTHER_USER_ID = "71084";
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Негативный Тест Попытка изменить данные пользователя, будучи неавторизованными")
    @DisplayName("Негативный Тест Попытка изменить данные пользователя, будучи неавторизованными")
    public void testEditUserDataNotAuth() {

        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();

        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequestWithoutAuth("https://playground.learnqa.ru/api/user/2", editData);

        Assetions.assertResponseCodeEquals(responseEditUser, 400);
        Assetions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");
    }

    @Test
    @Description("Негативный Тест Попытка изменить данные пользователя,будучи авторизованными другим пользователем")
    @DisplayName("Негативный Тест Попытка изменить данные пользователя, будучи авторизованными другим пользователем")
    public void testEditAnotherUserDataNotAuth() {

        Map<String, String> authData = new HashMap<>();

        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseAuth, "x-csrf-token");
        String cookie = this.getCookie(responseAuth, "auth_sid");

        String newName = "Changed Name123";
        Map<String, String> editData = new HashMap<>();
        editData.put("username", newName);


        Response responseAnotherUserData = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/" + ANOTHER_USER_ID
                , header, cookie, editData);


        //Проверяем что мы авторизовались
        Assetions.assertResponseCodeEquals(responseAuth, 200);

        //Проверяем что наш id и id учетки, которые хотим изменить - разные люди
        Assetions.assertCheckDifferentValue(responseAuth, ANOTHER_USER_ID, "user_id");

        //Проверяем что мы не смогли изменить данные
        Assetions.assertResponseCodeEquals(responseAnotherUserData, 400);
    }


    @Test
    @Description("Попытаемся изменить email пользователя, будучи авторизованными тем же пользователем, на новый email без символа @ ")
    @DisplayName("Попытаемся изменить email пользователя, будучи авторизованными тем же пользователем, на новый email без символа @ ")
    public void testEditUserDataUncorrectEmailAuth() {

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

        //Записываем полученный id для того, чтобы в дальнейшем обратиться при изменении почты
        String id = response.jsonPath().getString("id");


        //Логинимся
        Map<String, String> authData = new HashMap<>();

        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));


        Response responseAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);


        String header = this.getHeader(responseAuth, "x-csrf-token");
        String cookie = this.getCookie(responseAuth, "auth_sid");

        //Создаем некорректную почту
        String uncorrectEmail = DataGererator.getUncorrectRandomEmail();

        Map<String, String> editData = new HashMap<>();
        editData.put("email", uncorrectEmail);


        //пытаемся изменить почту у нашей учетки
        Response responseEditSelfData = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/" + id
                , header, cookie, editData);


        //Проверка создания учетки
        Assetions.assertResponseCodeEquals(response, 200);

        //Проверка входа в нее
        Assetions.assertResponseCodeEquals(responseAuth, 200);


        //Проверка ошибки при изменении
        Assetions.assertResponseCodeEquals(responseEditSelfData, 400);
        Assetions.assertResponseTextEquals(responseEditSelfData, "Invalid email format");


    }


    @Test
    @Description("Попытаемся изменить firstName пользователя, будучи авторизованными тем же пользователем, на очень короткое значение в один символ")
    @DisplayName("Попытаемся изменить firstName пользователя, будучи авторизованными тем же пользователем, на очень короткое значение в один символ")
    public void testEditUserDataUncorrectFirstNameAuth() {

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

        //Записываем полученный id для того, чтобы в дальнейшем обратиться при изменении почты
        String id = response.jsonPath().getString("id");


        //Логинимся
        Map<String, String> authData = new HashMap<>();

        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));


        Response responseAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login", authData);


        String header = this.getHeader(responseAuth, "x-csrf-token");
        String cookie = this.getCookie(responseAuth, "auth_sid");

        //Создаем некорректное имя
        String uncorrectName = NameGenerator.getShortName();

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", uncorrectName);


        //пытаемся изменить почту у нашей учетки
        Response responseEditSelfData = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/" + id
                , header, cookie, editData);


        //Проверка создания учетки
        Assetions.assertResponseCodeEquals(response, 200);

        //Проверка входа в нее
        Assetions.assertResponseCodeEquals(responseAuth, 200);


        //Проверка ошибки при изменении
        Assetions.assertResponseCodeEquals(responseEditSelfData, 400);
        Assetions.assertJsonHasField(responseEditSelfData, "error");
        Assetions.assertCheckSameValue(responseEditSelfData, "Too short value for field firstName", "error");

    }
}
