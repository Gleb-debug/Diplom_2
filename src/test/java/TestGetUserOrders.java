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

public class TestGetUserOrders {

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

        OrderRequest orderRequest = new OrderRequest(
                Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa6f") // Valid ingredients
        );

        Response orderResponse = userApiClient.createOrder(orderRequest, this.accessToken);

        userApiClient.assertResponse(orderResponse, 200);
    }


    @Test
    @DisplayName("Test retrieving user orders with authorization")
    @Description("Verify that a user can retrieve their orders when authorized")
    public void testGetUserOrdersWithAuthorization() {
        Response response = userApiClient.getUserOrders(this.accessToken);

        userApiClient.assertResponse(response, 200);

        userApiClient.assertResponseMessage(response, "success", "true");
    }


    @Test
    @DisplayName("Test retrieving user orders without authorization")
    @Description("Verify that a user cannot retrieve their orders without authorization")
    public void testGetUserOrdersWithoutAuthorization() {
        Response response = userApiClient.getUserOrders(null);

        userApiClient.assertResponse(response, 401);

        userApiClient.assertResponseMessage(response, "message", "You should be authorised");
    }

    @After
    public void tearDown() {
        if (this.accessToken != null) {
            Response response = userApiClient.deleteUser(this.accessToken);

            userApiClient.assertResponse(response, 202);
        }
    }
}