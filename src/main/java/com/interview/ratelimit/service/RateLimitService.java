package com.interview.ratelimit.service;

import com.interview.ratelimit.exception.LimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

//  @Autowired
//  private SingleNodeRateLimitStrategy singleNodeRateLimitStrategy;

  @Autowired
  private MultiNodeRateLimitService multiNodeRateLimitService;

  public void check(final String api, final String clientId) throws LimitExceededException {
    multiNodeRateLimitService.check(api, clientId);
  }
}
