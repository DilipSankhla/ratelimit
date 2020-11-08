package com.interview.ratelimit.service;

import com.interview.ratelimit.exception.LimitExceededException;
import com.interview.ratelimit.util.Constent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;

@Service
public class MultiNodeRateLimitService implements RateLimitService{

  @Autowired
  private ClientRateLimitMetaDataService metaDataService;

  @Autowired
  private RedisTemplate redisTemplate;

  @Value("${default.time.window}")
  private int DEFAULT_TIME_WINDOW;

  private static final Logger logger = LoggerFactory.getLogger(MultiNodeRateLimitService.class);

  public void check(final String apiName, final String clientId) throws LimitExceededException {

    logger.info("Checking API limit calls for {} , {}", apiName, clientId);

    String timeWindow =
        Constent.RL + clientId + Constent.UNDERSCORE + apiName + Constent.RATE_LIMIT_WINDOW;

    Map<String, Integer> clientMetaData = metaDataService.getMetaData(apiName, clientId);

    long currentTime = Instant.now().toEpochMilli() / 1000;
    String key = Constent.RL + clientId + Constent.UNDERSCORE + apiName;
    String value = (String) redisTemplate.opsForValue().get(key);

    if (redisTemplate.opsForValue().get(key) == null
        || (currentTime - getTimeElapsed(value)) > clientMetaData.get(timeWindow)) {
      redisTemplate.opsForValue().set(key, prepareValue(currentTime, 1), getExpirationTime(clientMetaData.get(timeWindow)), TimeUnit.SECONDS);
      return;
    }
    String callLimit =
            Constent.RL + clientId + Constent.UNDERSCORE + apiName + Constent.RATE_LIMIT_WINDOW_LIMIT;

    if (currentTime - getTimeElapsed(value) < clientMetaData.get(timeWindow)
        && (getCount(value) < clientMetaData.get(callLimit))) {
      redisTemplate.opsForValue().set(key, prepareValue(currentTime, getCount(value)), getExpirationTime(clientMetaData.get(timeWindow)), TimeUnit.SECONDS);
    } else {
      throw new LimitExceededException(Constent.CALL_LIMIT_HAS_EXCEED_FOR_API + apiName);
    }
  }

  private int getExpirationTime(Integer ApiCallTimeWindow) {
    return OptionalInt.of(ApiCallTimeWindow).orElse(DEFAULT_TIME_WINDOW);
  }

  private String prepareValue(final long currentTime, int count) {
    return currentTime + "#" + (++count);
  }

  private long getTimeElapsed(final String value) {
    return Long.parseLong(value.split("#")[0]);
  }

  private int getCount(final String value) {
    return Integer.parseInt(value.split("#")[1]);
  }
}
