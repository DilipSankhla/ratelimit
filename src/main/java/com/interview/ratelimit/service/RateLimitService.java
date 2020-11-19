package com.interview.ratelimit.service;

import com.interview.ratelimit.exception.LimitExceededException;
import org.springframework.stereotype.Component;

public interface RateLimitService {
  void check(final String api, final String clientId);
}
