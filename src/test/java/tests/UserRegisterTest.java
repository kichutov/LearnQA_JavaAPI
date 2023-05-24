package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Authorization cases")
@Feature("Authorization")
public class UserRegisterTest extends BaseTestCase {

    private ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


    @Test
    public void testNegativeCreateUserWithExistingEmail() {

        String email = "vinkotov@example.com";

        Map<String, String> authData = new HashMap<>();

        authData.put("email", email);
        authData.put("password", "123");
        authData.put("username", "learnqa");
        authData.put("firstName", "learnqa");
        authData.put("lastName", "learnqa");

        Response response = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", authData);


        Assetions.assertResponseCodeEquals(response, 400);
        Assetions.assertResponseTextEquals(response, "Users with email '" + email + "' already exists");
    }

    @Test
    @Description("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    public void createUserSuccessfully() {

        String email = DataGererator.getRandomEmail();

        Map<String, String> userData = new HashMap<>();

        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response response = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assetions.assertResponseCodeEquals(response, 200);
        Assetions.assertJsonHasKey(response, "id");
    }


    @Test
    @Description("Негативный тест создание пользователя с некорректным email - без символа @")
    @DisplayName("Test negative auth user without @")
    public void createUncorrectEmailSymbol() {


        String UncorrectEmail = DataGererator.getUncorrectRandomEmail();

        Map<String, String> userData = new HashMap<>();

        userData.put("email", UncorrectEmail);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response response = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assetions.assertResponseCodeEquals(response, 400);
        Assetions.assertResponseTextEquals(response, "Invalid email format");
    }


    @Description("Негативный тест создание пользователя без указания одного из полей ")
    @DisplayName("Test negative auth user without field")
    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void createWithoutOneParametr(String nameField) {


        String email = DataGererator.getRandomEmail();

        Map<String, String> userData = new HashMap<>();

        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");
        userData.remove(nameField);
        Response response = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assetions.assertResponseTextEquals(response, "The following required params are missed: " + nameField);
    }


    @Test
    @Description("Негативный тест создание пользователя с очень коротким именем в один символ")
    @DisplayName("Test negative auth user with short name")

    public void createUserByShortName() {
        String shortName = NameGenerator.getShortName();
        String email = DataGererator.getRandomEmail();

        Map<String, String> userData = new HashMap<>();

        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", shortName);
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");
        Response response = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assetions.assertResponseTextEquals(response, "The value of 'username' field is too short");
    }

    @Test
    @Description("Негативный тест создание пользователя с именем длиннее 250 символов")
    @DisplayName("Test negative auth user with long name")

    public void createUserByLongName() {
        String longName = NameGenerator.getLongName();
        String email = DataGererator.getRandomEmail();

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", longName);
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");
        Response response = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assetions.assertResponseTextEquals(response, "The value of 'username' field is too long");
    }

}
