package com.interview.ratelimit.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RateLimitUtil {
        public static String prepareClientMetaDataKey(String apiName, String clientId, String keyType) {
                return Constant.RL + clientId + Constant.UNDERSCORE + apiName + keyType;
        }
}
