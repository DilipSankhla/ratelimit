package com.interview.ratelimit.service;

import com.interview.ratelimit.util.Constent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimitServiceFactory {

  @Value("${serviceMode}")
  private String serviceMode;

  @Autowired
  private MultiNodeRateLimitService multiNodeRateLimitService;

  @Autowired
  private SingleNodeApiRateLimitService singleNodeApiRateLimitService;

  private static final Map<String, RateLimitService> rateLimitServiceMap = new HashMap<>();

  public RateLimitService getRateLimitService() {
    return rateLimitServiceMap.get(serviceMode);
  }

  @PostConstruct
  private void setServices() {
    rateLimitServiceMap.put(Constent.SINGLE, singleNodeApiRateLimitService);
    rateLimitServiceMap.put(Constent.MULTI, multiNodeRateLimitService);
  }
}
