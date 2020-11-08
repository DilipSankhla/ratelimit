package com.interview.ratelimit.controller;

import com.interview.ratelimit.exception.LimitExceededException;
import com.interview.ratelimit.service.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController()
public class RateLimit {

  @Autowired
  private RateLimitService rateLimitService;

  @GetMapping("/ratelimit")
  public ResponseEntity check(
          @RequestParam("apiName") @NotNull String apiName, @RequestParam("clientId") @NotNull String clientId) {
    try {
      rateLimitService.check(apiName, clientId);
    } catch (LimitExceededException e) {
      return new ResponseEntity(null, HttpStatus.TOO_MANY_REQUESTS);
    }
    return new ResponseEntity(null, HttpStatus.OK);
  }
}