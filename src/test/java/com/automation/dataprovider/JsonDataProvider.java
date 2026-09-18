package com.automation.dataprovider;
import com.automation.util.JsonDataReader;
import org.testng.annotations.DataProvider;
import java.lang.reflect.Method;
public final class JsonDataProvider {
 private JsonDataProvider(){}
 @DataProvider(name="userData")
 public static Object[][] getUserData(Method method){return JsonDataReader.getTestData("data/user-test-data.json",method.getName());}
}