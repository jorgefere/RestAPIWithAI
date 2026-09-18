package com.automation.base;
import com.automation.client.UserApiClient;
import org.apache.logging.log4j.*;
import org.testng.annotations.BeforeClass;
public abstract class BaseTest {
 protected Logger log;
 protected UserApiClient userApiClient;
 @BeforeClass(alwaysRun=true)
 public void setUp(){log=LogManager.getLogger(getClass());userApiClient=new UserApiClient();log.info("Initializing test class: {}",getClass().getSimpleName());}
}