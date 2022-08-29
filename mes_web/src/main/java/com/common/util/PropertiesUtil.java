package com.common.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by sunyin on 2018/2/24.
 */
public class PropertiesUtil {
    public static final Properties properties = new Properties();
    static {
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
