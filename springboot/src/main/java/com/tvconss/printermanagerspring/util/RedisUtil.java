package com.tvconss.printermanagerspring.util;

public class RedisUtil {
    public static String getKeyTokenHashKey(Long userId) {
        return String.format("auth:key_token:%d", userId);
    }
}
