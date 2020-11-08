package com.interview.ratelimit.datamanager;

import com.interview.ratelimit.model.ClientApiLimit;

public interface IClientDataManager<k,V> {

    void put(String key, String value);

    String get(String key);

}
