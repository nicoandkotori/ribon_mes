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
@TableName("Customer")
public class Customer {
    @TableId(value = "cCusCode")
    private String ccuscode;

    private String ccusname;

    private String ccusabbname;

    private String ccccode;

    private String cdccode;

    private String ctrade;

    private String ccusaddress;

    private String ccuspostcode;

    private String ccusregcode;

    private String ccusbank;

    private String ccusaccount;

    private Date dcusdevdate;

    private String ccuslperson;

    private String ccusemail;

    private String ccusperson;

    private String ccusphone;

    private String ccusfax;

    private String ccusbp;

    private String ccushand;

    private String ccuspperson;

    private BigDecimal icusdisrate;

    private String ccuscregrade;

    private BigDecimal icuscreline;

    private Integer icuscredate;

    private String ccuspaycond;

    private String ccusoaddress;

    private String ccusotype;

    private String ccusheadcode;

    private String ccuswhcode;

    private String ccusdepart;

    private Double iarmoney;

    private Date dlastdate;

    private Double ilastmoney;

    private Date dlrdate;

    private Double ilrmoney;

    private Date denddate;

    private Integer ifrequency;

    private String ccusdefine1;

    private String ccusdefine2;

    private String ccusdefine3;

    private Short icostgrade;

    private String ccreateperson;

    private String cmodifyperson;

    private Date dmodifydate;

    private String crelvendor;

    private String iid;

    private String cpricegroup;

    private String coffergrade;

    private Double iofferrate;

    private String ccusdefine4;

    private String ccusdefine5;

    private String ccusdefine6;

    private String ccusdefine7;

    private String ccusdefine8;

    private String ccusdefine9;

    private String ccusdefine10;

    private Integer ccusdefine11;

    private Integer ccusdefine12;

    private Double ccusdefine13;

    private Double ccusdefine14;

    private Date ccusdefine15;

    private Date ccusdefine16;

//    private Date pubufts;

    private String cinvoicecompany;

    private Boolean bcredit;

    private Boolean bcreditdate;

    private Boolean bcreditbyhead;

    private Boolean blicencedate;

    private Date dlicencesdate;

    private Date dlicenceedate;

    private Integer ilicenceadays;

    private Boolean bbusinessdate;

    private Date dbusinesssdate;

    private Date dbusinessedate;

    private Integer ibusinessadays;

    private Boolean bproxy;

    private Date dproxysdate;

    private Date dproxyedate;

    private Integer iproxyadays;

    private String cmemo;

    private Boolean blimitsale;

    private Object ccuscontactcode;

    private String ccuscountrycode;

    private String ccusenname;

    private String ccusenaddr1;

    private String ccusenaddr2;

    private String ccusenaddr3;

    private String ccusenaddr4;

    private String ccusportcode;

    private String cprimaryven;

    private Double fcommisionrate;

    private Double finsuerate;

    private Boolean bhomebranch;

    private String cbranchaddr;

    private String cbranchphone;

    private String cbranchperson;

    private String ccustradeccode;

    private String customerkcode;

    private Boolean bcusstate;

    private Short bshop;

    private String ccusbankcode;

    private String ccusexchName;

    private Short icusgsptype;

    private Short icusgspauth;

    private String ccusgspauthno;

    private String ccusbusinessno;

    private String ccuslicenceno;

    private Boolean bcusdomestic;

    private Boolean bcusoverseas;

    private String ccuscreditcompany;

    private String ccussaprotocol;

    private String ccusexprotocol;

    private String ccusotherprotocol;

    private Double fcusdiscountrate;

    private String ccussscode;

    private String ccuscmprotocol;

    private Date dcuscreatedatetime;

    private String ccusappdocno;

    private String ccusmnemcode;

    private Double fadvancepaymentratio;

    private Boolean bserviceattribute;

    private Boolean brequestsign;

    private Boolean bongpinstore;

    private String ccusmngtypecode;

    private BigDecimal accountType;

    private String ccusimagentprotocol;

    private Short isourcetype;

    private String isourceid;

    private Double fexpense;

    private Double fapprovedexpense;

    private Date dtouchedtime;

    private Date drecentlyinvoicetime;

    private Date drecentlyquotetime;

    private Date drecentlyactivitytime;

    private Date drecentlychancetime;

    private Date drecentlycontracttime;

    private String cltccustomercode;

    private Boolean btransflag;

    private String cltcperson;

    private Date dltcdate;

    private String clocationsite;

    private Double icustaxrate;

    private Date alloctDeptTime;

    private Date allotUserTime;

    private Date recyleDeptTime;

    private Date recylePubTime;

    private String clicenceno;

    private String clicencerange;

    private String ccusbusinessrange;

    private String ccusgspauthrange;

    private Date dcusgspedate;

    private Date dcusgspsdate;

    private Integer icusgspadays;

    private Boolean biscusattachfile;

    private Date drecentcontractdate;

    private Date drecentdeliverydate;

    private Date drecentoutbounddate;

    private String cprovince;

    private String ccity;

    private String ccounty;

    private Object ccusaddressguid;

    private String caddcode;

    private String ccreditaddcode;

    private String cregcash;

    private Date ddepbegindate;

    private Integer iemployeenum;

    private String curl;

    private Object pictureguid;


    public void setDefaultValue(){
         icusdisrate=BigDecimal.ZERO;
         icuscreline=BigDecimal.ZERO;
         icuscredate=0;
         ifrequency=0;
         icostgrade=Short.valueOf("0");
         dmodifydate=new Date();
         bcredit=false;
         bcreditdate=false;
         bcreditbyhead=false;
         blicencedate=false;
         bbusinessdate=false;
         bproxy=false;
         blimitsale=false;
         bhomebranch=false;
         bcusstate=false;
         bshop=Short.valueOf("0");
         ccusexchName="人民币";
         icusgsptype=Short.valueOf("0");
         bcusdomestic=false;
         bcusoverseas=false;
         dcuscreatedatetime=new Date();
         bserviceattribute=false;
         brequestsign=false;
         bongpinstore=false;
         ccusmngtypecode="999";
         accountType=BigDecimal.valueOf(26);
    }
}