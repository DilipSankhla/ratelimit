package com.interview.ratelimit.model;

import com.interview.ratelimit.util.Constent;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@RedisHash("client_api_call_limit")
public class ClientApiLimit {
    String clientId;
    String apiName;
    int limit;


}
