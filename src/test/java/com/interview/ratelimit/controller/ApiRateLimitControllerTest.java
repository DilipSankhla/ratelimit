package com.interview.ratelimit.controller;

import com.interview.ratelimit.service.RateLimitServiceFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiRateLimitControllerTest {

        @Autowired
        private RateLimitServiceFactory rateLimitServiceFactory;

        @Autowired
        private TestRestTemplate testRestTemplate;


        @Test
        void check() {
                ResponseEntity<String> successResponse = testRestTemplate.getForEntity("/ratelimit?apiname=/this/get/something&clientid=qqqq", String.class);
                assertEquals(200, successResponse.getStatusCodeValue());
                testRestTemplate.getForEntity("/ratelimit?apiname=/this/get/something&clientid=xxxx", String.class);
                testRestTemplate.getForEntity("/ratelimit?apiname=/this/get/something&clientid=xxxx", String.class);
                testRestTemplate.getForEntity("/ratelimit?apiname=/this/get/something&clientid=xxxx", String.class);
                testRestTemplate.getForEntity("/ratelimit?apiname=/this/get/something&clientid=xxxx", String.class);
                testRestTemplate.getForEntity("/ratelimit?apiname=/this/get/something&clientid=xxxx", String.class);
                ResponseEntity<String> failedResponse = testRestTemplate.getForEntity("/ratelimit?apiname=/this/get/something&clientid=qqqqqq", String.class);
                assertEquals(429, failedResponse.getStatusCodeValue());
        }
}