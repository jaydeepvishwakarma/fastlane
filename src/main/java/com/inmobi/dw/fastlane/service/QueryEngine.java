/**
 * JDBC call to hsqldb server.
 */
package com.inmobi.dw.fastlane.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import org.apache.log4j.Logger;

/**
 * @author jaydeep.vishwakarma
 * 
 */
public class QueryEngine {
  private static Connection connection = null;
  private static final String DELIM = "\u0001";

  QueryEngine() {
    try {
      Class.forName("org.hsqldb.jdbcDriver");
      connection = DriverManager.getConnection("jdbc:hsqldb:" + CommonPluginProps.get(CommonPluginProps.PropNames.HSQL_DB_URL),
          CommonPluginProps.get(CommonPluginProps.PropNames.HSQL_DB_USER), CommonPluginProps.get(CommonPluginProps.PropNames.HSQL_DB_PASSWORD));
    } catch (Exception exp) {
      exp.printStackTrace();
    }
  }

  /**
   * This method execute the query and return the result based on id
   * @param queryString : hsql query
   * @param key
   * @return Return list of rows delimited by Ctrl A
   */
  public synchronized List<String> getDataByKey(String queryString, String key) {
    Statement stmt;
    ResultSet rs;
    List<String> fieldList = null;
    try {
      fieldList = new ArrayList<String>();
      stmt = connection.createStatement();
      queryString = queryString.replace("?", "'" + key + "'");
      rs = stmt.executeQuery(queryString);
      int columnCount = rs.getMetaData().getColumnCount();
      while (rs.next()) {
        String rowString = rs.getString(1);
        for (int i = 1; i <= columnCount; i++) {
          rowString = rowString + DELIM + rs.getString(i);
        }
        fieldList.add(rowString);
      }
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return fieldList;
  }

  @Override
  protected void finalize() throws Throwable {
    connection.close();
  }

}
