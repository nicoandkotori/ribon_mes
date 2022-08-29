package com.web.event;

import com.modules.data.mybatis.DBTypeEnum;
import com.web.event.util.EnumEventTypeUtils;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FineEventDTO<T> {

    private DBTypeEnum dbTypeEnum;
    /**
     * 操作类型   枚举
     */
    private EnumEventTypeUtils.OPER_TYPE operType;
    private  EnumEventTypeUtils.EVENT_TYPE eventType;
    private BigDecimal updateQty;
    private T bizContent;
}
