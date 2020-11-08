package com.interview.ratelimit.service;

import com.interview.ratelimit.exception.LimitExceededException;
import com.interview.ratelimit.util.Constent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class MultiNodeRateLimitService {

  @Autowired
  private ClientRateLimitMetaDataService metaDataService;

  @Autowired
  private RedisTemplate redisTemplate;

  @Value("${default.time.window}")
  private int DEFAULT_TIME_WINDOW;

  public void check(final String apiName, final String clientId) throws LimitExceededException {
    String callLimit =
        (Constent.RL + clientId + Constent.UNDERSCORE + apiName + Constent.RATE_LIMIT_WINDOW_LIMIT)
            .intern();
    String timeWindow =
        (Constent.RL + clientId + Constent.UNDERSCORE + apiName + Constent.RATE_LIMIT_WINDOW)
            .intern();

    Map<String, Integer> clientMetaData = metaDataService.getMetaData(apiName, clientId);

    long currentTime = Instant.now().toEpochMilli() / 1000;
    String key = Constent.RL + clientId + Constent.UNDERSCORE + apiName;
    String value = (String) redisTemplate.opsForValue().get(key);

    if (redisTemplate.opsForValue().get(key) == null
        || (currentTime - getTimeElapsed(value)) > clientMetaData.get(timeWindow)) {
      redisTemplate.opsForValue()
          .set(key, prepareValue(currentTime, 0), DEFAULT_TIME_WINDOW, TimeUnit.SECONDS);
      return;
    }
    if (currentTime - getTimeElapsed(value) < clientMetaData.get(timeWindow)
        && (getCount(value) <= clientMetaData.get(callLimit))) {
      redisTemplate.opsForValue()
          .set(key, prepareValue(currentTime, getCount(value)), DEFAULT_TIME_WINDOW, TimeUnit.SECONDS);
    } else {
      throw new LimitExceededException();
    }

    System.out.println("value = " + value);
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
