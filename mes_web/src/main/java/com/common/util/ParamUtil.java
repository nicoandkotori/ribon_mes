package com.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by sunyin on 2019/12/24.
 */
public class ParamUtil {

    //读取配置文件中的值
    public static Object getParam(String key){
        InputStream is=ParamUtil.class.getClassLoader().getResourceAsStream("param.properties");
        BufferedReader br= new BufferedReader(new InputStreamReader(is));
        Properties props = new Properties();
        try {
            props.load(br);
            return props.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
