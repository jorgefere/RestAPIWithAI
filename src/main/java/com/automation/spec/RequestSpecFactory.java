package com.automation.spec;
import com.automation.config.ConfigManager; import com.automation.util.TokenManager; import io.qameta.allure.restassured.AllureRestAssured; import io.restassured.builder.RequestSpecBuilder; import io.restassured.http.ContentType; import io.restassured.specification.RequestSpecification;
public final class RequestSpecFactory {
 private RequestSpecFactory(){}
 public static RequestSpecification defaultSpec(){return new RequestSpecBuilder().setBaseUri(ConfigManager.get("base.url")).setBasePath(ConfigManager.get("base.path")).setAccept(ContentType.JSON).setContentType(ContentType.JSON).addFilter(new AllureRestAssured()).build();}
 public static RequestSpecification authenticatedSpec(){return new RequestSpecBuilder().addRequestSpecification(defaultSpec()).addHeader("Authorization","Bearer "+TokenManager.getToken()).build();}
}