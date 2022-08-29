package com.web.po.entity;

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
@TableName("PO_Podetails")
public class PoDetails {
    @TableId(value = "ID")
    private Integer id;

    private String cpoid;

    private String cinvcode;

    private BigDecimal iquantity;

    private BigDecimal inum;

    private BigDecimal iquotedprice;

    private BigDecimal iunitprice;

    private Object imoney;

    private Object itax;

    private Object isum;

    private Object idiscount;

    private BigDecimal inatunitprice;

    private Object inatmoney;

    private Object inattax;

    private Object inatsum;

    private Object inatdiscount;

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

    private Double cdefine26;

    private Double cdefine27;

    private Byte iflag;

    private String citemcode;

    private String citemClass;

    private Integer ppcids;

    private String citemname;

    private String cfree3;

    private String cfree4;

    private String cfree5;

    private String cfree6;

    private String cfree7;

    private String cfree8;

    private String cfree9;

    private String cfree10;

    private Byte bgsp;

    private Integer poid;

    private String cunitid;

    private BigDecimal itaxprice;

    private BigDecimal iarrqty;

    private BigDecimal iarrnum;

    private Object iarrmoney;

    private Object inatarrmoney;

    private Integer iappids;

    private String cdefine28;

    private String cdefine29;

    private String cdefine30;

    private String cdefine31;

    private String cdefine32;

    private String cdefine33;

    private Integer cdefine34;

    private Integer cdefine35;

    private Date cdefine36;

    private Date cdefine37;

    private Integer isosid;

    private Boolean btaxcost;

    private String csource;

    private String cbcloser;

    private Integer ippartid;

    private BigDecimal ipquantity;

    private Integer iptoseq;

    private Byte sotype;

    private String sodid;

    private Object contractrowguid;

    private String cupsocode;

    private BigDecimal iinvmpcost;

    private String contractcode;

    private String contractrowno;

    private BigDecimal fpovalidquantity;

    private BigDecimal fpovalidnum;

    private BigDecimal fpoarrquantity;

    private BigDecimal fpoarrnum;

    private BigDecimal fporetquantity;

    private BigDecimal fporetnum;

    private BigDecimal fporefusequantity;

    private BigDecimal fporefusenum;

    private Date dufts;

    private Integer iorderdid;

    private Byte iordertype;

    private String csoordercode;

    private Integer iorderseq;

    private Date cbclosetime;

    private Date cbclosedate;

    private String cbgItemcode;

    private String cbgItemname;

    private String cbgCaliberkey1;

    private String cbgCaliberkeyname1;

    private String cbgCaliberkey2;

    private String cbgCaliberkeyname2;

    private String cbgCaliberkey3;

    private String cbgCaliberkeyname3;

    private String cbgCalibercode1;

    private String cbgCalibername1;

    private String cbgCalibercode2;

    private String cbgCalibername2;

    private String cbgCalibercode3;

    private String cbgCalibername3;

    private Byte ibgCtrl;

    private String cbgAuditopinion;

    private BigDecimal fexquantity;

    private BigDecimal fexnum;

    private Integer ivouchrowno;

    private String cbgCaliberkey4;

    private String cbgCaliberkeyname4;

    private String cbgCaliberkey5;

    private String cbgCaliberkeyname5;

    private String cbgCaliberkey6;

    private String cbgCaliberkeyname6;

    private String cbgCalibercode4;

    private String cbgCalibername4;

    private String cbgCalibercode5;

    private String cbgCalibername5;

    private String cbgCalibercode6;

    private String cbgCalibername6;

    private String csocode;

    private Integer irowno;

    private BigDecimal freceivedqty;

    private BigDecimal freceivednum;

    private String cxjspdids;

    private String cbmemo;

    private String cbsysbarcode;

    private String planlotnumber;

    private Short bgift;

    private String cfactorycode;

    private Integer gcsourceid;

    private Integer gcsourceids;

    private String gcupcardnum;

    private Integer gcupid;

    private Integer gcupids;

    private String yycInvname;

    private String clsbwhcode;

    private String csyssourceautoid;
}