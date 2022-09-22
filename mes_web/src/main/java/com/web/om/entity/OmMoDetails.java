package com.web.om.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("OM_MODetails")
public class OmMoDetails {
    @TableId(value = "MoDetailsID")
    private Integer modetailsid;

    //订单主表ID
    private Integer moid;

    private String cinvcode;

    private BigDecimal iquantity;

    private BigDecimal inum;

    private BigDecimal iquotedprice;

    private BigDecimal iunitprice;

    private BigDecimal imoney;

    private BigDecimal itax;

    private BigDecimal isum;

    private Object idiscount;

    private BigDecimal inatunitprice;

    private BigDecimal inatmoney;

    private BigDecimal inattax;

    private BigDecimal inatsum;

    private Object inatdiscount;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dstartdate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date darrivedate;

    private BigDecimal ireceivedqty;

    private BigDecimal ireceivednum;

    private Object ireceivedmoney;

    private BigDecimal iinvqty;

    private BigDecimal iinvnum;

    private Object iinvmoney;

    private String cfree1;

    private String cfree2;

    private Object inatinvmoney;

    private Object ioritotal;

    private Object itotal;

    private BigDecimal ipertaxrate;

    private String cdefine22;

    private String cdefine23;

    private String cdefine24;

    private String cdefine25;

    private BigDecimal cdefine26;

    private BigDecimal cdefine27;

    private Byte iflag;

    private String citemcode;

    private String citemClass;

    private String citemname;

    //mes中对应记录的ID
    private String cfree3;

    private String cfree4;

    private String cfree5;

    private String cfree6;

    private String cfree7;

    private String cfree8;

    private String cfree9;

    private String cfree10;

    private Byte bgsp;

    private String cunitid;

    private BigDecimal itaxprice;

    private BigDecimal iarrqty;

    private BigDecimal iarrnum;

    private Object iarrmoney;

    private Object inatarrmoney;

    private String cdefine28;

    private String cdefine29;

    private String cdefine30;

    private String cdefine31;

    private String cdefine32;

    private String cdefine33;

    private String cdefine34;

    private String cdefine35;

    private Date cdefine36;

    private Date cdefine37;

    private Boolean btaxcost;

    private String csource;

    private String cbcloser;

    private Byte sotype;

    private String sodid;

    private Integer bomid;

    private Integer mrpdetailsid;

    private BigDecimal fparentscrp;

    private BigDecimal imaterialsendqty;

    private BigDecimal tbaseqtyd;



    private Integer ivtids;

    private String cupsocode;

    private Integer cupsoids;

    private Integer isosid;

    private String cdemandcode;

    private String cdetailsdemandmemo;

    private Date dbclosedate;

    private Date dbclosetime;

    private Integer ivouchrowno;

    private BigDecimal freceivedqty;

    private BigDecimal freceivednum;

    private String cbmemo;

    private Integer icusbomid;

    private String cbsysbarcode;

    private String csocode;

    private Integer irowno;

    private Byte bomtype;

    private String isourcemocode;

    private Integer isourcemodetailsid;

    private BigDecimal imrpqty;

    private BigDecimal freworkquantity;

    private BigDecimal freworknum;

    private Byte iproducttype;

    private Integer imainmodetailsid;

    private Integer imainmomaterialsid;

    private String cmaininvcode;

    private Byte isoordertype;

    private Integer iorderdid;

    private String csoordercode;

    private Integer iorderseq;

    private String cplanlotnumber;

    private String cfactorycode;

    //金工默认数据
    public void setDefaultValueForJinGong(){

      bgsp=Byte.valueOf("0");
      btaxcost=true;
      fparentscrp=BigDecimal.ZERO;
//        ivtids=8159;
    }

    /**
     * 转换mes字段为u8字段
     */
    public void setDataFromMesProduct(OmOrderDetail product){
        setCinvcode(product.getProductInvCode());
        setIquantity(product.getProductQty());
        setCdefine26(product.getMaterialPrice());
        setCdefine27(product.getMaterialAmount());
        setItaxprice(product.getWorkPrice());
        setInatsum(product.getTotalWorkAmount());
        setDstartdate(product.getPlanStartDate());
        setDarrivedate(product.getPlanEndDate());
    }
}