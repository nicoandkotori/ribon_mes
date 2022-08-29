package com.web.basicinfo.entity;

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
@TableName("bas_part")
public class BasPart {
    @TableId(value = "PartID")
    private Integer partid;

    private String invcode;

    private String free1;

    private String free2;

    private String free3;

    private String free4;

    private String free5;

    private String free6;

    private String free7;

    private String free8;

    private String free9;

    private String free10;

    private BigDecimal safeqty;

    private BigDecimal minqty;

    private BigDecimal mulqty;

    private BigDecimal fixqty;

    private Boolean bvirtual;

    private String drawcode;

//    private Date ufts;

    private Integer llc;

    private String cbasengineerfigno;

    private Double fbasmaxsupply;

    private Short isurenesstype;

    private Short idatetype;

    private Integer idatesum;

    private Short idynamicsurenesstype;

    private Double ibestrowsum;

    private Double ipercentumsum;

    private BigDecimal roundingfactor;

    private Boolean freestockflag;

    private Boolean bfreestop;

    public void setDefaultValue(){
        free1="";
        free2="";
        free3="";
        free4="";
        free5="";
        free6="";
        free7="";
        free8="";
        free9="";
        free10="";
        minqty=BigDecimal.ZERO;
        mulqty=BigDecimal.ZERO;
        fixqty=BigDecimal.ZERO;
        bvirtual=true;
        roundingfactor=BigDecimal.ZERO;
        freestockflag=false;
        freestockflag=false;
    }
}