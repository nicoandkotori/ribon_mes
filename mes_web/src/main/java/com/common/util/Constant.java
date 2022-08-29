package com.common.util;

import java.util.UUID;

/**
 * Created by sunyin on 2019/12/24.
 */
public class Constant {
    public static final String JWT_ID = UUID.randomUUID().toString();

    /**
     * 加密密文
     */
    public static final String JWT_SECRET = "3d990d2276917dfac04467df11fff26d";
    public static final int JWT_TTL = 60 * 60 * 1000;  //millisecond
}
