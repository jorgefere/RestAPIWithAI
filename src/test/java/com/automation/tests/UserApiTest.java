package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.dataprovider.JsonDataProvider;
import com.automation.model.request.CreateUserRequest;
import com.automation.model.request.UpdateUserRequest;
import com.automation.model.response.UserResponse;
import com.automation.util.TestDataUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Epic("GoREST API")
@Feature("User Management")
public class UserApiTest extends BaseTest {

 @Test(dataProvider="userData",dataProviderClass=JsonDataProvider.class)
 @Severity(SeverityLevel.CRITICAL)
 public void createUserPositive(Map<String,Object> data){
   CreateUserRequest request=createRequest(data);
   Response response=userApiClient.createUser(request);
   response.then().statusCode(201).body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
   UserResponse user=response.as(UserResponse.class);
   Assert.assertNotNull(user.getId());
   Assert.assertEquals(user.getName(),request.getName());
   Assert.assertEquals(user.getEmail(),request.getEmail());
   Assert.assertEquals(user.getGender(),request.getGender());
   Assert.assertEquals(user.getStatus(),request.getStatus());
   userApiClient.deleteUser(user.getId()).then().statusCode(204);
 }

 @Test(dataProvider="userData",dataProviderClass=JsonDataProvider.class)
 public void createUserNegative(Map<String,Object> data){
   CreateUserRequest request=createRequest(data);
   Response response=userApiClient.createUser(request);
   response.then().statusCode(422).body(matchesJsonSchemaInClasspath("schemas/validation-error-schema.json"));
   Assert.assertEquals(response.jsonPath().getString("[0].field"),"gender");
 }

 @Test(dataProvider="userData",dataProviderClass=JsonDataProvider.class)
 public void getUserPositive(Map<String,Object> data){
   UserResponse user=createPrerequisiteUser(data);
   try{
     UserResponse returned=userApiClient.getUser(user.getId()).then().statusCode(200)
       .body(matchesJsonSchemaInClasspath("schemas/user-schema.json")).extract().as(UserResponse.class);
     Assert.assertEquals(returned.getId(),user.getId());
     Assert.assertEquals(returned.getEmail(),user.getEmail());
   } finally { deleteSilently(user.getId()); }
 }

 @Test(dataProvider="userData",dataProviderClass=JsonDataProvider.class)
 public void getUserNegative(Map<String,Object> data){
   userApiClient.getUser(Long.parseLong(data.get("userId").toString())).then().statusCode(404);
 }

 @Test(dataProvider="userData",dataProviderClass=JsonDataProvider.class)
 public void updateUserPositive(Map<String,Object> data){
   CreateUserRequest prerequisite=new CreateUserRequest("Original User","male",TestDataUtils.uniqueEmail("update-original"),"active");
   UserResponse user=userApiClient.createUser(prerequisite).then().statusCode(201).extract().as(UserResponse.class);
   try{
     UpdateUserRequest request=new UpdateUserRequest(data.get("name").toString(),TestDataUtils.uniqueEmail(data.get("emailPrefix").toString()),data.get("status").toString());
     UserResponse updated=userApiClient.updateUser(user.getId(),request).then().statusCode(200)
       .body(matchesJsonSchemaInClasspath("schemas/user-schema.json")).extract().as(UserResponse.class);
     Assert.assertEquals(updated.getId(),user.getId());
     Assert.assertEquals(updated.getName(),request.getName());
     Assert.assertEquals(updated.getEmail(),request.getEmail());
     Assert.assertEquals(updated.getStatus(),request.getStatus());
   } finally { deleteSilently(user.getId()); }
 }

 @Test(dataProvider="userData",dataProviderClass=JsonDataProvider.class)
 public void updateUserNegative(Map<String,Object> data){
   CreateUserRequest prerequisite=new CreateUserRequest("Negative Update User","male",TestDataUtils.uniqueEmail("negative-update-original"),"active");
   UserResponse user=userApiClient.createUser(prerequisite).then().statusCode(201).extract().as(UserResponse.class);
   try{
     UpdateUserRequest request=new UpdateUserRequest(data.get("name").toString(),TestDataUtils.uniqueEmail(data.get("emailPrefix").toString()),data.get("status").toString());
     userApiClient.updateUser(user.getId(),request).then().statusCode(422)
       .body(matchesJsonSchemaInClasspath("schemas/validation-error-schema.json"));
   } finally { deleteSilently(user.getId()); }
 }

 @Test(dataProvider="userData",dataProviderClass=JsonDataProvider.class)
 public void deleteUserPositive(Map<String,Object> data){
   UserResponse user=createPrerequisiteUser(data);
   userApiClient.deleteUser(user.getId()).then().statusCode(204);
   userApiClient.getUser(user.getId()).then().statusCode(404);
 }

 @Test(dataProvider="userData",dataProviderClass=JsonDataProvider.class)
 public void deleteUserNegative(Map<String,Object> data){
   userApiClient.deleteUser(Long.parseLong(data.get("userId").toString())).then().statusCode(404);
 }

 private CreateUserRequest createRequest(Map<String,Object> data){
   return new CreateUserRequest(data.get("name").toString(),data.get("gender").toString(),
     TestDataUtils.uniqueEmail(data.get("emailPrefix").toString()),data.get("status").toString());
 }
 private UserResponse createPrerequisiteUser(Map<String,Object> data){
   return userApiClient.createUser(createRequest(data)).then().statusCode(201).extract().as(UserResponse.class);
 }
 private void deleteSilently(Long id){
   try{Response response=userApiClient.deleteUser(id);log.info("Cleanup for user {} returned {}",id,response.statusCode());}
   catch(Exception e){log.warn("Cleanup failed for user {}",id,e);}
 }
}