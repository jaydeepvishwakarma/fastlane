package com.inmobi.dw.fastlane.service;

import java.util.HashSet;
import java.util.List;
import com.inmobi.dw.fastlane.proto.*;
import com.inmobi.dw.fastlane.proto.Fastlane.*;

public class QueryParser {
  private static final String AND = " AND ";
  private static final String EQUAL = "=";
  private static final String DOT = ".";
  private static final String WHERE = " WHERE ";
  private static final String FROM = " FROM ";
  private static final String FIELDSEPARATOR = " , ";
  private static final String SELECT = "SELECT ";


  public static String queryGenerator(Fastlane.Query query) {

    if (query != null) {
      StringBuilder queryString = new StringBuilder();
      HashSet<String> tables = new HashSet<String>();
      String selectString = generateSelectString(query, tables);
      String conditionString = generateConditionString(query, tables);
      String joinString = generateJoinString(query, tables);
      String fromString = generateFromString(tables);
      queryString.append(SELECT).append(selectString.toString()).append(FROM).append(fromString).append(WHERE).append(joinString)
          .append(conditionString).append(";");
      return queryString.toString();
    }
    return null;
  }

  private static String generateConditionString(Fastlane.Query query, HashSet<String> tables) {
    StringBuilder queryString = new StringBuilder();
    tables.add(query.getKeyNode().getTableName());
    queryString.append(query.getKeyNode().getTableName()).append(DOT).append(query.getKeyNode().getField(0)).append(EQUAL).append(" ? ");
    return queryString.toString();
  }

  private static String generateFromString(HashSet<String> tables) {
    String tableString = tables.toString();
    return tableString.substring(1, tableString.length() - 1);
  }

  private static String generateJoinString(Fastlane.Query query, HashSet<String> tables) {
    StringBuilder queryString = new StringBuilder();
    for (JoinNode joinNode : query.getJoinChainList()) {
      List<String> lhsKeyFields = joinNode.getLhs().getFieldList();
      List<String> rhsKeyFields = joinNode.getRhs().getFieldList();
      for (int index = 0; index < lhsKeyFields.size(); index++) {
        tables.add(joinNode.getLhs().getTableName());
        tables.add(joinNode.getRhs().getTableName());
        queryString.append(joinNode.getLhs().getTableName()).append(DOT).append(lhsKeyFields.get(index)).append(EQUAL);
        queryString.append(joinNode.getRhs().getTableName()).append(DOT).append(rhsKeyFields.get(index)).append(AND);
      }
    }
    return queryString.toString();
  }

  private static String generateSelectString(Fastlane.Query query, HashSet<String> tables) {
    int index = 0, fieldIndex = 0;
    StringBuilder selectString = new StringBuilder();
    for (Node node : query.getSelectList()) {
      index++;
      tables.add(node.getTableName());
      fieldIndex = 0;
      for (String field : node.getFieldList()) {
        fieldIndex++;
        if (index == query.getSelectCount() && fieldIndex == node.getFieldCount()) {
          selectString.append(node.getTableName()).append(DOT).append(field);
        } else {
          selectString.append(node.getTableName()).append(DOT).append(field).append(FIELDSEPARATOR);
        }
      }
    }
    return selectString.toString();
  }

}
