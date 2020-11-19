package com.interview.ratelimit.service;

import com.interview.ratelimit.exception.LimitExceededException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MultiNodeRateLimitServiceTest {

        @Autowired
        private MultiNodeRateLimitService multiNodeRateLimitService;

        @Autowired
        private ClientRateLimitMetaDataService clientRateLimitMetaDataService;

        @BeforeEach
        void setUp() {
                clientRateLimitMetaDataService.setTimeWindow("ZZZZ",String.valueOf(40),"/asd/qwe/zxc");
                clientRateLimitMetaDataService.setCallLimit("ZZZZ",String.valueOf(1),"/asd/qwe/zxc");
        }

        @Test
        void check() {
                multiNodeRateLimitService.check("/asd/qwe/zxc","ZZZZ");
        }

        @Test
        void checkFail() {
                assertThrows(LimitExceededException.class, () -> {
                        multiNodeRateLimitService.check("/asd/qwe/zxc","ZZZZ");
                        multiNodeRateLimitService.check("/asd/qwe/zxc","ZZZZ");
                });
        }
}