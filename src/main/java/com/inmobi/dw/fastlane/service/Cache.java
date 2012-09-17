/**
 * Implementing LRU Cache
 */
package com.inmobi.dw.fastlane.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author jaydeep.vishwakarma
 * 
 */
public class Cache extends LinkedHashMap<String, List<String>> {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private int capacity = 0;
  private long accessCount = 0;
  private long hitCount = 0;

  public Cache(int capacity) {
    super(capacity + 1, 1.1f, true);
    this.capacity = capacity;
  }

  @Override
  public List<String> get(Object key) {
    accessCount++;
    if (containsKey(key)) {
      hitCount++;
    }
    
    return super.get(key);
  }

  @Override
  protected boolean removeEldestEntry(Entry arg0) {
    return size() > capacity;
  }

  public long getAccessCount() {
    return accessCount;
  }

  public long getHitCount() {
    return hitCount;
  }
}