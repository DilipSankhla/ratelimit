package com.interview.ratelimit.service;

import com.interview.ratelimit.exception.LimitExceededException;
import com.interview.ratelimit.util.Constent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class SingleNodeApiRateLimitService implements RateLimitService {

  private static final Logger logger = LoggerFactory.getLogger(SingleNodeApiRateLimitService.class);

  @Autowired
  private ClientRateLimitMetaDataService metaDataService;

  private static final ConcurrentHashMap<String, Long[]> CLIENT_CALL_DETAILS = new ConcurrentHashMap<>();

  public void check(final String apiName, final String clientId) throws LimitExceededException {

    logger.info("Checking API limit calls for {} , {}", apiName, clientId);

    String callLimit = Constent.RL + clientId + Constent.UNDERSCORE + apiName + Constent.RATE_LIMIT_WINDOW_LIMIT;
    String timeWindow = Constent.RL + clientId + Constent.UNDERSCORE + apiName + Constent.RATE_LIMIT_WINDOW;

    Map<String, Integer> clientMetaData = metaDataService.getMetaData(apiName, clientId);

    long currentTime = Instant.now().toEpochMilli() / 1000;
    String key = Constent.RL + clientId + Constent.UNDERSCORE + apiName;

    if (CLIENT_CALL_DETAILS.get(key) == null
        || (currentTime - (CLIENT_CALL_DETAILS.get(key))[0]) > clientMetaData.get(timeWindow)) {
      CLIENT_CALL_DETAILS.put(key, new Long[] {currentTime, 1L});
      return;
    }

    if (currentTime - CLIENT_CALL_DETAILS.get(key)[0] < clientMetaData.get(timeWindow)
        && (CLIENT_CALL_DETAILS.get(key))[1] <= clientMetaData.get(callLimit)) {
      CLIENT_CALL_DETAILS.computeIfPresent(key, (k, v) -> new Long[] {currentTime, ++v[1]});
    } else {
      throw new LimitExceededException(Constent.CALL_LIMIT_HAS_EXCEED_FOR_API +apiName);
    }
  }


  @Scheduled(fixedRate = 1000 * 60 * 60)
  private void cleanup()
  {
    logger.info(" Running Cleanup Cron @ {}", Instant.now());
    long currentTime = Instant.now().toEpochMilli() / 1000;
    CLIENT_CALL_DETAILS.forEachEntry(10, (entry) -> {
      if(currentTime - entry.getValue()[0] > 3600) CLIENT_CALL_DETAILS.remove(entry.getKey());});
  }
}