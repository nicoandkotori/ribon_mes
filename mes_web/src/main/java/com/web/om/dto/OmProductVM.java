package com.web.om.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.om.entity.OmOrderDetail;
import com.web.om.entity.OmOrderMaterial;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OmProductVM {
    private Date dateStart;  //---
    private Date dateEnd;   //---

    //表头
    private Integer moid;    //---
    private String ccode;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ddate;
    private String cdefine1;
    private String cdefine2;
    private String cdefine14;
    private String cdepcode;
    private String cdepname;
    private String cpersoncode;
    private String cpersonname;
    private String cvencode;
    private String cvenname;
    private String cmemo;
    private Byte[] cstate;    //---
    private Integer iverifystatenew;
    private String cverifier;
    private Date dverifydate;

    private Date dverifytime;

    //子表
    private Integer modetailsid;   //---
    private String cinvcode;
    private String cinvname;
    private String cinvstd;
    private String ccomunitname;
    private BigDecimal iquantity;    //---
    private BigDecimal cdefine26;//材料单价
    private BigDecimal cdefine27;//单件材料费


    private BigDecimal itaxprice;//单件加工费   taxmakeprice
    private BigDecimal inatsum ;//加工费合计  taxmakemny
    private BigDecimal tolpic;//单件价格
    private BigDecimal tolnum;//合计
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dstartdate;//计划开工日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date darrivedate;//计划完工日期    //---


    //明细
    private Integer momaterialsid;   //---
    private String cinvcodes;//材料编码
    private String cinvnames;//材料名称
    private String cinvstds;//材料规格

    private String cdefine28;//厚度
    private String cdefine29;//长
    private String cdefine30;//宽
    private String cdefine31;//外径
    private String cdefine32;//内径
    private String cinvdefine2;//密度   //---
    private String cdefine22;//下料尺寸
    private BigDecimal fbaseqtyn;//单耗     //---
    private BigDecimal fqtys;//总量      //---

    private Integer rowNo;
    private String recordId;//标识

    /**
     * 转换mes字段为u8字段
     */
    public void setDataFromMesMaterial(OmOrderMaterial material){
        setRecordId(material.getRecordId());
        setCinvcodes(material.getInvCode());
        setCinvname(material.getInvName());
        setCinvstds(material.getInvStd());
        setCcomunitname(material.getInvUnit());
        setCdefine22(material.getInvSize());
        setCdefine26(material.getUnitMaterialPrice());
        setCdefine27(material.getUnitMaterialAmount());
        setCdefine28(material.getInvLand());
        setCdefine29(material.getInvLen());
        setCdefine30(material.getInvWidth());
        setCdefine31(material.getInvExternalDiameter());
        setCdefine32(material.getInvInternalDiameter());
        setFbaseqtyn(material.getIqty());
        setFqtys(material.getTqty());
        setIquantity(new BigDecimal(material.getProductQty()));

    }

    /**
     * 转换mes字段为u8字段
     */
    public void setDataFromMesProduct(OmOrderDetail product){
        setRecordId(product.getRecordId());
        setCinvcode(product.getProductInvCode());
        setCinvname(product.getProductInvName());
        setCinvstd(product.getProductInvStd());
        setCcomunitname(product.getProductInvUnit());
        setIquantity(product.getProductQty());
        setCdefine26(product.getMaterialPrice());
        setCdefine27(product.getMaterialAmount());
        setItaxprice(product.getWorkPrice());
        setInatsum(product.getTotalWorkAmount());
        setTolpic(product.getPrice());
        setTolnum(product.getAmount());
        setDstartdate(product.getPlanStartDate());
        setDarrivedate(product.getPlanEndDate());
    }
}