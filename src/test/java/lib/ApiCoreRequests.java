package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {

    @Step("Make a GET-request with auth cookie")
    public Response makeGetRequest(String url, String token, String cookie){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid",cookie)
                .get(url)
                .andReturn();
    }


    @Step("Make a POST-request with auth cookie")
    public Response makePostRequest(String url, Map<String, String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a PUT-request without koen and auth cookie")
    public Response makePutRequestWithoutAuth(String url, Map<String,String> editData){
        return given()
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make a PUT-request with koen and auth cookie")
    public Response makePutRequest(String url,String token, String cookie, Map<String,String> editData){
        return given()
                .header("x-csrf-token", token)
                .cookie("auth_sid",cookie)
                .body(editData)
                .put(url)
                .andReturn();
    }
}
