package com.interview.ratelimit.controller;

import com.interview.ratelimit.model.ClientApiLimitMetaData;
import com.interview.ratelimit.service.ClientRateLimitMetaDataService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientRateLimitMetaDataControllerTest {

        @Autowired
        private ClientRateLimitMetaDataService clientRateLimitMetaDataService;

        @Autowired
        private TestRestTemplate testRestTemplate;

        @Test
        void setCallLimit() {
                ResponseEntity<String> stringResponseEntity = testRestTemplate.postForEntity("/clientmetadata/calllimit?apiname=/this/get/something&clientid=xxxx&calllimit=70", null, String.class);
                assertEquals(200, stringResponseEntity.getStatusCodeValue());
        }

        @Test
        void setTimeWindow() {
                ResponseEntity<String> stringResponseEntity = testRestTemplate.postForEntity("/clientmetadata/timewindow?apiname=/this/get/something&clientid=xxxx&timewindow=57", null, String.class);
                assertEquals(200, stringResponseEntity.getStatusCodeValue());
        }

        @Test
        void getMetaData() {
                testRestTemplate.postForEntity("/clientmetadata/calllimit?apiname=/this/get/something&clientid=xxxx&calllimit=7", null, String.class);
                testRestTemplate.postForEntity("/clientmetadata/timewindow?apiname=/this/get/something&clientid=xxxx&timewindow=57", null, String.class);

                ResponseEntity<ClientApiLimitMetaData> forEntity = testRestTemplate.getForEntity("/clientmetadata?apiname=/this/get/something&clientid=xxxx", ClientApiLimitMetaData.class);

                assertEquals(200, forEntity.getStatusCodeValue());
                assertEquals(57, forEntity.getBody().getTimeWindow());
                assertEquals(7, forEntity.getBody().getCallLimit());
        }

        @AfterAll
        public void cleanUp()
        {


        }
}