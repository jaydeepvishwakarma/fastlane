/**
 * This Layer generate and keep the client side maps for different query sets.
 */
package com.inmobi.dw.fastlane.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.inmobi.dw.fastlane.proto.Fastlane.Query;

/**
 * @author jaydeep.vishwakarma
 * 
 */
public class MetadataService {

  private static Map<String, String> querySet;
  private static Map<String, LinkedHashMap<String, List<String>>> metadataSet;
  private static final int DEFAULT_CAP_OF_MAP = 1000;
  private static QueryEngine queryEngine;

  public MetadataService() throws IOException {
    querySet = new HashMap<String, String>();
    metadataSet = new HashMap<String, LinkedHashMap<String, List<String>>>();
    queryEngine = new QueryEngine();
  }

  /**
   * It takes the hsql query and keep it in set. It keeps default size 1000
   * 
   * @param setName
   * @param query
   *          Hsql Db query
   */
  public void setMetadataQuery(String setName, String query) {
    querySet.put(setName, decorateQuery(query));
    metadataSet.put(setName, new Cache(DEFAULT_CAP_OF_MAP));
  }

  private String decorateQuery(String query) {
    return query + " WHERE " + query.subSequence(query.indexOf(" "), query.indexOf(",")) + "= ?";
  }

  /**
   * It takes the hsql query and keep it in set.
   * 
   * @param setName
   * @param query
   *          hsql db query
   * @param cacheSize
   *          set the cache size according to need
   */
  public void setMetadataQuery(String setName, String query, int cacheSize) {
    querySet.put(setName, decorateQuery(query));
    metadataSet.put(setName, new Cache(cacheSize));
  }

  /**
   * It takes the Query object and convert it to hsql query and keep it to set.
   * 
   * @param setName
   * @param query
   *          Query object
   * @param cacheSize
   *          set the cache size according to need
   */
  public void setMetadataQuery(String setName, Query query, int cacheSize) {
    querySet.put(setName, QueryParser.queryGenerator(query));
    metadataSet.put(setName, new Cache(cacheSize));
  }

  /**
   * It takes the Query object and convert it to hsql query and keep it to set. It keeps default size 1000
   * 
   * @param setName
   * @param query
   *          Query object
   */
  public void setMetadataQuery(String setName, Query query) {
    querySet.put(setName, QueryParser.queryGenerator(query));
    metadataSet.put(setName, new Cache(DEFAULT_CAP_OF_MAP));
  }

  /**
   * This method is used to fetch the list of rows by key
   * 
   * @param setName
   * @param key
   *          To fetch rows by key
   * @return list of Ctrl A seperated rows
   */
  public List<String> getValueByKey(String setName, String key) {
    if (metadataSet.get(setName).get(key) == null) {
      metadataSet.get(setName).put(key, queryEngine.getDataByKey(querySet.get(setName), key));
    }
    return metadataSet.get(setName).get(key);
  }

}
