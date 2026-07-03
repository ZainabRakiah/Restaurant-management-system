package com.restaurant.bdd.support;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public final class ApiClient {

    private ApiClient() {
    }

    public static String baseUrl() {
        return System.getProperty("bdd.base.url", "http://localhost:8080/api");
    }

    public static void reset() {
        RestAssured.reset();
        RestAssured.baseURI = baseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public static io.restassured.response.Response post(String path, Object body) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(path);
    }

    public static io.restassured.response.Response get(String path, Object... pathParams) {
        return RestAssured.given().get(path, pathParams);
    }

    public static io.restassured.response.Response put(String path, Object body, Object... pathParams) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .put(path, pathParams);
    }
}
