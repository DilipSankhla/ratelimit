package com.interview.ratelimit.service;

import com.interview.ratelimit.exception.LimitExceededException;
import com.interview.ratelimit.model.ClientApiLimitMetaData;
import com.interview.ratelimit.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;

@Service
public class MultiNodeRateLimitService implements RateLimitService{

  @Autowired
  private ClientRateLimitMetaDataService metaDataService;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Value("${default.time.window}")
  private int DEFAULT_TIME_WINDOW;

  private static final Logger logger = LoggerFactory.getLogger(MultiNodeRateLimitService.class);

  public void check(final String apiName, final String clientId) {

    logger.info("Checking API limit calls for {} , {}", apiName, clientId);


    ClientApiLimitMetaData metaData = metaDataService.getMetaData(apiName, clientId);

    long currentTime = Instant.now().getEpochSecond();
    String lookupKey = Constant.RL + clientId + Constant.UNDERSCORE + apiName;
    String value = redisTemplate.opsForValue().get(lookupKey);


    if (value == null || (currentTime - getTimeElapsed(value)) > metaData.getTimeWindow()) {
      redisTemplate.opsForValue().set(lookupKey, prepareValue(currentTime, 1), getExpirationTime(metaData.getTimeWindow()), TimeUnit.SECONDS);
      return;
    }
    if (currentTime - getTimeElapsed(value) < metaData.getTimeWindow()
        && (getCount(value) < metaData.getCallLimit())) {
      redisTemplate.opsForValue().set(lookupKey, prepareValue(currentTime, getCount(value)), getExpirationTime(metaData.getTimeWindow()), TimeUnit.SECONDS);
    } else {
      throw new LimitExceededException(Constant.CALL_LIMIT_HAS_EXCEED_FOR_API + apiName);
    }
  }

  private int getExpirationTime(Integer apiCallTimeWindow) {
    return OptionalInt.of(apiCallTimeWindow).orElse(DEFAULT_TIME_WINDOW);
  }

  private String prepareValue(final long currentTime, int count) {
    return currentTime + Constant.SEPARATOR_HASH + (++count);
  }

  private long getTimeElapsed(final String value) {
    return Long.parseLong(value.split(Constant.SEPARATOR_HASH)[0]);
  }

  private int getCount(final String value) {
    return Integer.parseInt(value.split(Constant.SEPARATOR_HASH)[1]);
  }
}
