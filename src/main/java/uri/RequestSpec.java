package uri;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpec {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL) // Set the base URL
                .setContentType(io.restassured.http.ContentType.JSON)
                .addHeader("Accept", "application/json")
                .build();
    }

    public static RequestSpecification setAuth(RequestSpecification requestSpec, String token) {
        return requestSpec.header("Authorization", token);
    }
}