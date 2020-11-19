package com.interview.ratelimit.datamanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisClientDataManager implements IClientDataManager {

    @Autowired
    private RedisTemplate<String, String > redisTemplate;

    @Override
    public void put(final String key, final String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String get(final String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }
}