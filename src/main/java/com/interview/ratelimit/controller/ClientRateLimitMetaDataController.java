package com.interview.ratelimit.controller;

import com.interview.ratelimit.model.ClientApiLimitMetaData;
import com.interview.ratelimit.service.ClientRateLimitMetaDataService;
import com.interview.ratelimit.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@Validated
public class ClientRateLimitMetaDataController {

    @Autowired
    private ClientRateLimitMetaDataService clientRateLimitMetaDataService;

    @PostMapping("/clientmetadata/calllimit")
    public ResponseEntity setCallLimit(@RequestParam(Constant.CLIENTID) @NotNull @NotBlank  String clientId,
                                      @RequestParam(Constant.CALLLIMIT) @Min(0) String callLimit,
                                      @RequestParam(Constant.APINAME) @NotNull @NotBlank  String apiName)
    {
        clientRateLimitMetaDataService.setCallLimit(clientId, callLimit, apiName);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/clientmetadata/timewindow")
    public ResponseEntity setTimeWindow(@RequestParam(Constant.CLIENTID) @NotNull @NotBlank String clientId,
                                        @RequestParam(Constant.TIMEWINDOW) @Min(0) String timeWindow,
                                        @RequestParam(Constant.APINAME)  @NotNull @NotBlank String apiName)
    {
        clientRateLimitMetaDataService.setTimeWindow(clientId, timeWindow, apiName);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/clientmetadata")
    public ResponseEntity getMetaData(@RequestParam(Constant.CLIENTID) @NotNull @NotBlank String clientId,
                                      @RequestParam(Constant.APINAME) @NotNull @NotBlank String apiName)
    {
        ClientApiLimitMetaData metaData = clientRateLimitMetaDataService.getMetaData(apiName, clientId);
        return new ResponseEntity<>(metaData, HttpStatus.OK);
    }
}