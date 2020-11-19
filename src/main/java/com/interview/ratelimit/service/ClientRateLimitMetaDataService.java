package com.interview.ratelimit.service;

import com.interview.ratelimit.datamanager.IClientDataManager;
import com.interview.ratelimit.model.ClientApiLimitMetaData;
import com.interview.ratelimit.util.Constant;
import com.interview.ratelimit.util.RateLimitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

@Service
public class ClientRateLimitMetaDataService {

  @Value("${default.call.limit}")
  private int DEFAULT_LIMIT;

  @Value("${default.time.window}")
  private int DEFAULT_TIME_WINDOW;

  @Autowired
  private IClientDataManager redisClientDataManager;

  private static final Logger logger = LoggerFactory.getLogger(ClientRateLimitMetaDataService.class);

  //@Cacheable
  public ClientApiLimitMetaData getMetaData(final String apiName, final String clientId) {

    logger.info("Getting meta data for {} , {}", apiName, clientId);

    String _tempCallLimitValue =
            getClientMetaData(apiName, clientId, Constant.RATE_LIMIT_WINDOW_LIMIT);
    String _tempTimeWindowValue =
            getClientMetaData(apiName, clientId, Constant.RATE_LIMIT_WINDOW);

    int callLimitValue =
        _tempCallLimitValue != null ? Integer.parseInt(_tempCallLimitValue) : DEFAULT_LIMIT;
    int timeWindowValue =
        _tempTimeWindowValue != null ? Integer.parseInt(_tempTimeWindowValue) : DEFAULT_TIME_WINDOW;

    return new ClientApiLimitMetaData(callLimitValue, timeWindowValue);
  }

  private String getClientMetaData(final String apiName, final String clientId, final String keyType) {
    return redisClientDataManager.get(RateLimitUtil.prepareClientMetaDataKey(apiName, clientId, keyType));
  }

  public void setCallLimit(final String clientId, final String callLimit, final String apiName) {
    redisClientDataManager.put(RateLimitUtil.prepareClientMetaDataKey(apiName, clientId, Constant.RATE_LIMIT_WINDOW_LIMIT), callLimit);
   }

  public void setTimeWindow(final String clientId, final String timeWindow, final String apiName) {
    redisClientDataManager.put(RateLimitUtil.prepareClientMetaDataKey(apiName, clientId, Constant.RATE_LIMIT_WINDOW), timeWindow);
  }
}