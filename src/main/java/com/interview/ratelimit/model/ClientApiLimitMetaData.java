package com.interview.ratelimit.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientApiLimitMetaData {
        int callLimit;
        int timeWindow;
}