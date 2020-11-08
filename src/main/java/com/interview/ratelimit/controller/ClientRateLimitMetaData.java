package com.interview.ratelimit.controller;

import com.interview.ratelimit.datamanager.IClientDataManager;
import com.interview.ratelimit.service.ClientRateLimitMetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController()
@Validated
public class ClientRateLimitMetaData {

    @Autowired
    private IClientDataManager redisClientDataManager;

    @Autowired
    private ClientRateLimitMetaDataService clientRateLimitMetaDataService;

    @PostMapping("/clientmetadata/calllimit")
    public ResponseEntity setCallLimit(@RequestParam("clientid") @NotNull @NotBlank  String clientId,
                                      @RequestParam("calllimit") @Min(0) String callLimit,
                                      @RequestParam("apiname") @NotNull @NotBlank  String apiName)
    {
        clientRateLimitMetaDataService.setCallLimit(clientId, callLimit, apiName);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/clientmetadata/timewindow")
    public ResponseEntity setTimeWindow(@RequestParam("clientid") @NotNull @NotBlank String clientId,
                                        @RequestParam("timewindow" ) @Min(0) String timeWindow,
                                        @RequestParam("apiname")  @NotNull @NotBlank String apiName)
    {
        clientRateLimitMetaDataService.setTimeWindow(clientId, timeWindow, apiName);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/clientmetadata")
    public ResponseEntity getMetaData(@RequestParam("clientid") @NotNull @NotBlank String clientId,
                                      @RequestParam("apiname") @NotNull @NotBlank String apiName)
    {
        Map<String, Integer> metaData = clientRateLimitMetaDataService.getMetaData(clientId, apiName);

        return null;

    }

}
