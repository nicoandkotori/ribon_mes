package com.web.pd.dto;

import com.web.pd.entity.PdOrderDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 生产订单产品表join材料表DTO
 *
 * @author mijiahao
 * @date 2022/09/27
 */
@Data
@ToString
@EqualsAndHashCode
public class PdOrderDetailDTO extends PdOrderDetail {

    private String partId;

    private String invCode;

    private String invName;

    private String invStd;

    private String invUnit;

    private BigDecimal qty;

    private BigDecimal unitMaterialPrice;

    private BigDecimal unitMaterialAmount;

    private String invLand;

    private String invWidth;

    private String invLen;

    private String invExternalDiameter;

    private String invInternalDiameter;

    private String invDensity;

    private String invSize;

    private BigDecimal iqty;

    private BigDecimal tqty;


    private String itype;

    private Integer u8MoMaterialId;
}
