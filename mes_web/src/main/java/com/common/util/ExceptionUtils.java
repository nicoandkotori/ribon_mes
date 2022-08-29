package com.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by vfun02 on 2017/11/16.
 */
public class ExceptionUtils {

    /**
     * 收集异常堆栈信息
     */
    public static String printStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String strs = sw.toString();
        return strs;
    }

}
