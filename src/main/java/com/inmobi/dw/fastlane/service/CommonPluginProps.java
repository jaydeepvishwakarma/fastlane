/**
 * Managing common properties by this class.
 */
package com.inmobi.dw.fastlane.service;

import java.util.Hashtable;
import java.util.Properties;

/**
 * @author jaydeep
 */
public class CommonPluginProps {
  public static enum PropNames {
    HSQL_DB_URL,
    HSQL_DB_USER,
    HSQL_DB_PASSWORD,
  }
  private static Hashtable<String, String> propMap = new Hashtable<String, String>();

  static {
    try {
      Properties p = new Properties();
      p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("fastlane.properties"));
      propMap.put(PropNames.HSQL_DB_URL.name(), p.get("hsqldb.url").toString());
      propMap.put(PropNames.HSQL_DB_USER.name(), p.get("hsqldb.user").toString());
      propMap.put(PropNames.HSQL_DB_PASSWORD.name(), p.get("hsqldb.password").toString());
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error while initializing CommonPluginProps: " + e.getMessage());
    }
  }


  public static boolean set(String key, String value) {
    try {
      propMap.put(PropNames.valueOf(key).name(), value);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static String get(PropNames name) {
    return propMap.get(name.name());
  }

  public static String getUnchecked(String name) {
    return propMap.get(name);
  }

  public static String setUnchecked(String name, String value) {
    return propMap.put(name, value);
  }
}