package com.interview.ratelimit.controller;

import com.interview.ratelimit.exception.LimitExceededException;
import com.interview.ratelimit.service.RateLimitServiceFactory;
import com.interview.ratelimit.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@Validated
public class ApiRateLimitController {

  @Autowired
  private RateLimitServiceFactory rateLimitServiceFactory;

  @GetMapping("/ratelimit")
  public ResponseEntity check(@RequestParam(Constant.APINAME) @NotNull @NotBlank String apiName,
          @RequestParam(Constant.CLIENTID) @NotNull @NotBlank String clientId) {
    try {
      rateLimitServiceFactory.getRateLimitService().check(apiName, clientId);
    } catch (LimitExceededException e) {
      return new ResponseEntity<>(Constant.CALL_LIMIT_HAS_EXCEED_FOR_API + apiName,
              HttpStatus.TOO_MANY_REQUESTS);
    }
    return new ResponseEntity<>(Constant.CALL_ALLOWED, HttpStatus.OK);
  }
}