package com.util;

import java.math.BigDecimal;

/**
 * Created by Li on 2020/11/11.
 */
public class DecimalUtil {
    public static BigDecimal parseNull(BigDecimal decimal) {
        if(decimal == null){
            return BigDecimal.ZERO;
        }
        return decimal;
    }
}
