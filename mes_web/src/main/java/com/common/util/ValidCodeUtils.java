package com.common.util;

import java.util.Random;

import static java.lang.Integer.parseInt;

/**
 * Created by Yao on 2018-7-26.
 */
public class ValidCodeUtils {
    public static String getValidCode(int codeLength)
    {
        String n="";
        for(int i=1;i<codeLength;i++){
            n+="0";
        }
        n="1"+n;
        int w=parseInt(n);
        return String.valueOf((new Random().nextInt(899999) + w));
    }
}
