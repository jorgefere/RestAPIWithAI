package com.automation.client;
import com.automation.constants.ApiRoutes; import com.automation.model.request.*; import com.automation.spec.RequestSpecFactory; import io.qameta.allure.Step; import io.restassured.response.Response; import static io.restassured.RestAssured.given;
public class UserApiClient {
 @Step("Create user") public Response createUser(CreateUserRequest r){return given().spec(RequestSpecFactory.authenticatedSpec()).body(r).when().post(ApiRoutes.USERS);}
 @Step("Get user {userId}") public Response getUser(long userId){return given().spec(RequestSpecFactory.defaultSpec()).pathParam("userId",userId).when().get(ApiRoutes.USER_BY_ID);}
 @Step("Update user {userId}") public Response updateUser(long userId,UpdateUserRequest r){return given().spec(RequestSpecFactory.authenticatedSpec()).pathParam("userId",userId).body(r).when().patch(ApiRoutes.USER_BY_ID);}
 @Step("Delete user {userId}") public Response deleteUser(long userId){return given().spec(RequestSpecFactory.authenticatedSpec()).pathParam("userId",userId).when().delete(ApiRoutes.USER_BY_ID);}
}