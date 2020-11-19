package com.interview.ratelimit.service;

import com.interview.ratelimit.exception.LimitExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SingleNodeApiRateLimitServiceTest {

        @Autowired
        private SingleNodeApiRateLimitService singleNodeApiRateLimitService;

        @Autowired
        private ClientRateLimitMetaDataService clientRateLimitMetaDataService;

        @BeforeEach
        void setUp() {
                clientRateLimitMetaDataService.setTimeWindow("ZZZZ",String.valueOf(40),"/asd/qwe/zxc");
                clientRateLimitMetaDataService.setCallLimit("ZZZZ",String.valueOf(1),"/asd/qwe/zxc");
        }

        @Test
        void check() {
                singleNodeApiRateLimitService.check("/asd/qwe/zxc","ZZZZ");
        }

        @Test
        void checkFail() {
                assertThrows(LimitExceededException.class, () -> {
                        singleNodeApiRateLimitService.check("/asd/qwe/zxc","ZZZZ");
                        singleNodeApiRateLimitService.check("/asd/qwe/zxc","ZZZZ");
                });
        }
}