package com.interview.ratelimit.service;

import com.interview.ratelimit.datamanager.IClientDataManager;
import com.interview.ratelimit.model.ClientApiLimitMetaData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientRateLimitMetaDataServiceTest {

        @Autowired
        private ClientRateLimitMetaDataService clientRateLimitMetaDataService;

        @MockBean
        private IClientDataManager redisClientDataManager;

        @Test
        void check() {
                assertNotNull(clientRateLimitMetaDataService);
        }

        @Test
        void getMetaData() {

                Mockito.when(redisClientDataManager.get("")).thenReturn("5");

                ClientApiLimitMetaData clientApiLimitMetaData = clientRateLimitMetaDataService.getMetaData("/asd/qwe/zxc", "ZSZSZ");

                clientApiLimitMetaData.getCallLimit();
                clientApiLimitMetaData.getTimeWindow();

        }

        @Test
        void setCallLimit() {
                clientRateLimitMetaDataService.setCallLimit("ZZZZ",String.valueOf(4),"/asd/qwe/zxc");

        }

        @Test
        void setTimeWindow() {
                clientRateLimitMetaDataService.setTimeWindow("ZZZZ",String.valueOf(40),"/asd/qwe/zxc");
        }

        @AfterAll
        static void  cleanUp()
        {

        }
}