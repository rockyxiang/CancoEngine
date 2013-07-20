package com.canco.util;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;

public class KeyUtils {
    /**
     * 获取UUID
     *
     * @return
     */
    public static String nextUUID() {
        return StringUtils.replace(String.valueOf(UUID.randomUUID().toString()).toUpperCase(), "-", "");
    }

    public static void main(String[] args) {
        System.out.println(nextUUID());
    }
}
