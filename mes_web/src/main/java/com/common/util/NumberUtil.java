package com.common.util;

import java.math.BigDecimal;

/**
 * Created by chen on 2021-10-08.
 */
public class NumberUtil {

    /**
     * 判断是否为 0
     *
     * @param obj BigDecimal
     * @return
     */
    public static boolean izZero(BigDecimal obj) {
        return obj == null || obj.compareTo(BigDecimal.ZERO) == 0;
    }
}
