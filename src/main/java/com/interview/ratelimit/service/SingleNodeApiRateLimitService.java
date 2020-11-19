package com.interview.ratelimit.service;

import com.interview.ratelimit.exception.LimitExceededException;
import com.interview.ratelimit.model.ClientApiLimitMetaData;
import com.interview.ratelimit.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
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

  public void check(final String apiName, final String clientId) {

    logger.info("Checking API limit calls for {} , {}", apiName, clientId);

    ClientApiLimitMetaData metaData = metaDataService.getMetaData(apiName, clientId);

    long currentTime = Instant.now().getEpochSecond();
    String lookupKey = Constant.RL + clientId + Constant.UNDERSCORE + apiName;

    if (CLIENT_CALL_DETAILS.get(lookupKey) == null
        || (currentTime - (CLIENT_CALL_DETAILS.get(lookupKey))[0]) > metaData.getTimeWindow()) {
      CLIENT_CALL_DETAILS.put(lookupKey, new Long[] {currentTime, 1L});
      return;
    }

    if (currentTime - CLIENT_CALL_DETAILS.get(lookupKey)[0] < metaData.getTimeWindow()
        && (CLIENT_CALL_DETAILS.get(lookupKey))[1] <= metaData.getCallLimit()) {
      CLIENT_CALL_DETAILS.computeIfPresent(lookupKey, (k, v) -> new Long[] {currentTime, ++v[1]});
    } else {
      throw new LimitExceededException(Constant.CALL_LIMIT_HAS_EXCEED_FOR_API +apiName);
    }
  }


  @Scheduled(fixedRate = 1000 * 60 * 60)
  private void cleanup()
  {
    Instant now = Instant.now();
    logger.info(" Running Cleanup Cron @ {}", now);
    CLIENT_CALL_DETAILS.forEachEntry(10, (entry) -> {
      if(now.getEpochSecond() - entry.getValue()[0] > 3600) CLIENT_CALL_DETAILS.remove(entry.getKey());});
  }
}