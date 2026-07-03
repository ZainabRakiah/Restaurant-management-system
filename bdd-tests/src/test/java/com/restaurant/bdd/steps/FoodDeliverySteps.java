package com.restaurant.bdd.steps;

import com.restaurant.bdd.support.ApiClient;
import com.restaurant.common.dto.DeliveryRequest;
import com.restaurant.common.dto.MenuItemRequest;
import com.restaurant.common.dto.OrderItemRequest;
import com.restaurant.common.dto.OrderRequest;
import com.restaurant.common.dto.RestaurantRequest;
import com.restaurant.common.dto.UserRequest;
import io.restassured.response.Response;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class FoodDeliverySteps {

    private Long customerId;
    private Long driverId;
    private Long restaurantId;
    private Long menuItemId;
    private Long orderId;
    private Long deliveryId;
    private Response lastResponse;

    @Given("the food delivery platform is available")
    public void platformIsAvailable() {
        ApiClient.reset();
        lastResponse = ApiClient.get("/users");
        lastResponse.then().statusCode(200);
    }

    @Given("a customer account exists")
    public void customerExists() {
        UserRequest request = new UserRequest(
                "BDD Customer",
                "bdd.customer@example.com",
                "password123",
                "555-1000",
                "CUSTOMER"
        );
        lastResponse = ApiClient.post("/users", request);
        lastResponse.then().statusCode(201);
        customerId = lastResponse.jsonPath().getLong("id");
    }

    @Given("a driver account exists")
    public void driverExists() {
        UserRequest request = new UserRequest(
                "BDD Driver",
                "bdd.driver@example.com",
                "password123",
                "555-2000",
                "DRIVER"
        );
        lastResponse = ApiClient.post("/users", request);
        lastResponse.then().statusCode(201);
        driverId = lastResponse.jsonPath().getLong("id");
    }

    @Given("a restaurant with menu items is registered")
    public void restaurantWithMenuExists() {
        RestaurantRequest restaurant = new RestaurantRequest(
                "BDD Bistro",
                "Fusion",
                "100 Test Lane",
                4.8
        );
        lastResponse = ApiClient.post("/restaurants", restaurant);
        lastResponse.then().statusCode(201);
        restaurantId = lastResponse.jsonPath().getLong("id");

        MenuItemRequest menuItem = new MenuItemRequest(
                restaurantId,
                "Test Pasta",
                "Integration test dish",
                14.99,
                "Main"
        );
        lastResponse = ApiClient.post("/menu", menuItem);
        lastResponse.then().statusCode(201);
        menuItemId = lastResponse.jsonPath().getLong("id");
    }

    @When("the customer places an order for one menu item")
    public void customerPlacesOrder() {
        OrderRequest order = new OrderRequest(
                customerId,
                restaurantId,
                List.of(new OrderItemRequest(menuItemId, 1)),
                "200 Delivery Road"
        );
        lastResponse = ApiClient.post("/orders", order);
        lastResponse.then().statusCode(201);
        orderId = lastResponse.jsonPath().getLong("id");
    }

    @When("a driver is assigned to the order")
    public void driverAssigned() {
        DeliveryRequest delivery = new DeliveryRequest(orderId, driverId, "BDD assignment");
        lastResponse = ApiClient.post("/deliveries", delivery);
        lastResponse.then().statusCode(201);
        deliveryId = lastResponse.jsonPath().getLong("id");
    }

    @When("the driver completes the delivery")
    public void driverCompletesDelivery() {
        lastResponse = ApiClient.put("/deliveries/{id}/complete", Map.of(), deliveryId);
        lastResponse.then().statusCode(200);
    }

    @Then("the order should be created with status $status")
    public void orderShouldHaveStatus(String status) {
        lastResponse = ApiClient.get("/orders/{id}", orderId);
        lastResponse.then()
                .statusCode(200)
                .body("status", equalTo(status))
                .body("totalAmount", notNullValue());
    }

    @Then("the delivery should be marked as $status")
    public void deliveryShouldHaveStatus(String status) {
        lastResponse = ApiClient.get("/deliveries/{id}", deliveryId);
        lastResponse.then()
                .statusCode(200)
                .body("status", equalTo(status));
    }
}
