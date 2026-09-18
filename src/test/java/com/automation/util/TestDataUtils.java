package com.automation.util;
import java.util.UUID;
public final class TestDataUtils {
 private TestDataUtils(){}
 public static String uniqueEmail(String prefix){return prefix+"-"+UUID.randomUUID()+"@example.com";}
}