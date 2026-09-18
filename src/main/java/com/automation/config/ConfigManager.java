package com.automation.config;
import java.io.*; import java.util.Properties;
public final class ConfigManager {
 private static final Properties P=new Properties();
 static { try(InputStream in=ConfigManager.class.getClassLoader().getResourceAsStream("config/config.properties")){ if(in==null) throw new IllegalStateException("config/config.properties not found"); P.load(in);} catch(IOException e){throw new ExceptionInInitializerError(e);} }
 private ConfigManager(){}
 public static String get(String key){String s=System.getProperty(key); if(s!=null&&!s.isBlank()) return s; String v=P.getProperty(key); if(v==null||v.isBlank()) throw new IllegalArgumentException("Configuration property not found: "+key); return v;}
}