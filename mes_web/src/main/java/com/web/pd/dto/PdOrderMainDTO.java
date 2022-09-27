package com.web.pd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.pd.entity.PdOrderMain;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 生产订单主表dto
 *
 * @author mijiahao
 * @date 2022/09/27
 */
@Data
@ToString
public class PdOrderMainDTO extends PdOrderMain {


    private String dateStart ;

    private String dateEnd ;
}
