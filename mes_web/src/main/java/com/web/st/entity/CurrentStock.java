package com.web.st.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("CurrentStock")
public class CurrentStock {
    @TableId(value = "AutoID")
    private Integer autoid;

    private String cwhcode;

    private String cinvcode;

    private Integer itemid;

    private String cbatch;

    private String cvmivencode;

    private Integer isotype;

    private String isodid;

    private BigDecimal iquantity;

    private Object inum;

    private String cfree1;

    private String cfree2;

    private Object foutquantity;

    private Object foutnum;

    private Object finquantity;

    private Object finnum;

    private String cfree3;

    private String cfree4;

    private String cfree5;

    private String cfree6;

    private String cfree7;

    private String cfree8;

    private String cfree9;

    private String cfree10;

    private Date dvdate;

    private Boolean bstopflag;

    private Object ftransinquantity;

    private Date dmdate;

    private Object ftransinnum;

    private Object ftransoutquantity;

    private Object ftransoutnum;

    private Object fplanquantity;

    private Object fplannum;

    private Object fdisablequantity;

    private Object fdisablenum;

    private Object favaquantity;

    private Object favanum;

    private Date ufts;

    private Integer imassdate;

    private Boolean bgspstop;

    private Short cmassunit;

    private Object fstopquantity;

    private Object fstopnum;

    private Date dlastcheckdate;

    private String ccheckstate;

    private Date dlastyearcheckdate;

    private Short iexpiratdatecalcu;

    private String cexpirationdate;

    private Date dexpirationdate;

    private BigDecimal ipeqty;

    private BigDecimal ipenum;
}