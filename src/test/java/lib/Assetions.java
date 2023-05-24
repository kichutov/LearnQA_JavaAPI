package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Assetions {

    public static void assertJsonByName(Response Response, String name, int expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertResponseTextEquals(Response Response, String expectedAnswer) {

        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is not as expected"
        );
    }

    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                Response.statusCode(),
                "Response status code is not as expected"
        );
    }

    public static void assertJsonHasField(Response Response, String expectedFieldName) {
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonHasFields(Response Response, String [] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            Assetions.assertJsonHasField(Response, expectedFieldName);
        }
    }

    public static void assertJsonNotField(Response Response, String unexpectedFieldName) {
        Response.then().assertThat().body("$", not(unexpectedFieldName));
    }

    public static void assertJsonNotFields(Response Response, String [] unexpectedFieldNames) {
        for (String unexpectedFieldName:unexpectedFieldNames){
            Assetions.assertJsonNotField(Response,unexpectedFieldName);
        }
    }

    public static void assertCheckSameValue(Response Response, String expectedValue, String nameField) {
        assertEquals(
                expectedValue,
                Response.jsonPath().getString(nameField),
                "Value  is not as expected"
        );
    }

    public static void assertCheckDifferentValue(Response Response, String expectedValue, String nameField) {
        assertNotEquals(
                expectedValue,
                Response.jsonPath().getString(nameField),
                "Value  is not as expected"
        );
    }
}
