package com.interview.ratelimit.service;

import com.interview.ratelimit.exception.LimitExceededException;
import com.interview.ratelimit.util.Constent;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class SingleNodeRateLimitStrategy {

  @Autowired
  private ClientRateLimitMetaDataService metaDataService;

  private static final ConcurrentHashMap<String, Long[]> CLIENT_CALL_COUNT = new ConcurrentHashMap<>();

  public void check(final String api, final String clientId) throws LimitExceededException {

    String callLimit = Constent.RL + clientId + Constent.UNDERSCORE + api + Constent.RATE_LIMIT_WINDOW_LIMIT;
    String timeWindow = Constent.RL + clientId + Constent.UNDERSCORE + api + Constent.RATE_LIMIT_WINDOW;

    Map<String, Integer> clientMetaData = metaDataService.getMetaData(api, clientId);

    long currentTime = Instant.now().toEpochMilli() / 1000;
    String key = Constent.RL + clientId + Constent.UNDERSCORE + api;

    if (CLIENT_CALL_COUNT.get(key) == null
        || (currentTime - (CLIENT_CALL_COUNT.get(key))[0]) > clientMetaData.get(timeWindow)) {
      CLIENT_CALL_COUNT.put(key, new Long[] {currentTime, 1L});
      return;
    }

    if (currentTime - CLIENT_CALL_COUNT.get(key)[0] < clientMetaData.get(timeWindow)
        && (CLIENT_CALL_COUNT.get(key))[1] <= clientMetaData.get(callLimit)) {
      CLIENT_CALL_COUNT.computeIfPresent(key, (k, v) -> new Long[] {currentTime, ++v[1]});
    } else {
      throw new LimitExceededException();
    }
  }
}