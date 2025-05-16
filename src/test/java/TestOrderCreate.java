import api.UserApiClient;
import data.Data;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import json.OrderRequest;
import json.UserRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestOrderCreate {

    private String accessToken;
    private final UserApiClient userApiClient = new UserApiClient();


    @Before
    public void setUp() {
        String randomEmail = Data.generateRandomEmail();
        String randomPassword = Data.generateRandomPassword();

        UserRequest userRequest = new UserRequest(
                randomEmail,
                randomPassword,
                "Username"
        );

        Response response = userApiClient.createUser(userRequest);

        userApiClient.assertResponse(response, 200);

        this.accessToken = userApiClient.extractAccessToken(response);
    }


    @Test
    @DisplayName("Test creating an order with authorization and valid ingredients")
    @Description("Verify that a user can create an order with valid ingredients when authorized")
    public void testCreateOrderWithAuthorizationAndValidIngredients() {
        OrderRequest orderRequest = new OrderRequest(
                Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa6f") // Valid ingredients
        );

        Response response = userApiClient.createOrder(orderRequest, this.accessToken);

        userApiClient.assertResponse(response, 200);

        userApiClient.assertResponseMessage(response, "success", "true");
    }


    @Test
    @DisplayName("Test creating an order without authorization but with valid ingredients")
    @Description("Verify that a user can create an order with valid ingredients even without authorization")
    public void testCreateOrderWithoutAuthorizationButWithValidIngredients() {
        OrderRequest orderRequest = new OrderRequest(
                Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa6f") // Valid ingredients
        );

        Response response = userApiClient.createOrder(orderRequest, null);

        userApiClient.assertResponse(response, 200);

        userApiClient.assertResponseMessage(response, "success", "true");
    }


    @Test
    @DisplayName("Test creating an order with authorization but without ingredients")
    @Description("Verify that a user cannot create an order without ingredients even when authorized")
    public void testCreateOrderWithAuthorizationButWithoutIngredients() {
        OrderRequest orderRequest = new OrderRequest(
                List.of()
        );

        Response response = userApiClient.createOrder(orderRequest, this.accessToken);

        userApiClient.assertResponse(response, 400);

        userApiClient.assertResponseMessage(response, "message", "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Test creating an order with authorization and invalid ingredients")
    @Description("Verify that a user cannot create an order with invalid ingredients even when authorized")
    public void testCreateOrderWithAuthorizationAndInvalidIngredients() {
        OrderRequest orderRequest = new OrderRequest(
                Arrays.asList("invalidIngredient1", "invalidIngredient2")
        );

        Response response = userApiClient.createOrder(orderRequest, this.accessToken);

        userApiClient.assertResponse(response, 500);

        userApiClient.assertResponseMessageHTML(response, "html.body.pre", "Internal Server Error");
    }

    @After
    public void tearDown() {
        if (this.accessToken != null) {
            Response response = userApiClient.deleteUser(this.accessToken);

            userApiClient.assertResponse(response, 202);
        }
    }
}