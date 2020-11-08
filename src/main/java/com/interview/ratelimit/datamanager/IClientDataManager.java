package com.interview.ratelimit.datamanager;

public interface IClientDataManager {

    void put(String key, String value);

    String get(String key);

}
