package com.interview.ratelimit.service;

import com.interview.ratelimit.datamanager.IClientDataManager;
import com.interview.ratelimit.util.Constent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ClientRateLimitMetaDataService {

  @Value("${default.call.limit}")
  private int DEFAULT_LIMIT;

  @Value("${default.time.window}")
  private int DEFAULT_TIME_WINDOW;

  @Autowired
  private IClientDataManager redisClientDataManager;

  @Cacheable
  public Map<String, Integer> getMetaData(final String apiName, final String clientId) {

    Map<String, Integer> clientMetaData = new HashMap<>();

    String _tempCallLimitValue =
        redisClientDataManager.get(
                constructKey(apiName, clientId, Constent.RATE_LIMIT_WINDOW_LIMIT));
    String _tempTimeWindowValue =
        redisClientDataManager.get(constructKey(apiName, clientId, Constent.RATE_LIMIT_WINDOW));

    int callLimitValue =
        _tempCallLimitValue != null ? Integer.parseInt(_tempCallLimitValue) : DEFAULT_LIMIT;
    int timeWindowValue =
        _tempTimeWindowValue != null ? Integer.parseInt(_tempTimeWindowValue) : DEFAULT_TIME_WINDOW;

    clientMetaData.put(constructKey(apiName, clientId, Constent.RATE_LIMIT_WINDOW_LIMIT), callLimitValue);
    clientMetaData.put(constructKey(apiName, clientId, Constent.RATE_LIMIT_WINDOW), timeWindowValue);

    return clientMetaData;
  }

  private String constructKey(String apiName, String clientId, String keyType) {
    return Constent.RL + clientId + Constent.UNDERSCORE + apiName + keyType;
  }

  public void setCallLimit(String clientId, String callLimit, String apiName) {
    redisClientDataManager.put(constructKey(apiName, clientId, Constent.RATE_LIMIT_WINDOW_LIMIT), callLimit);
   }

  public void setTimeWindow(String clientId, String timeWindow, String apiName) {
    redisClientDataManager.put(constructKey(apiName, clientId, Constent.RATE_LIMIT_WINDOW), timeWindow);
  }
}