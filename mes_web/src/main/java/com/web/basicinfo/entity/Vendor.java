package com.web.basicinfo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Vendor")
public class Vendor {
    @TableId(value = "cvencode")
    private String cvencode;

    private String cvenname;

    private String cvenabbname;

    private String cvccode;

    private String cdccode;

    private String ctrade;

    private String cvenaddress;

    private String cvenpostcode;

    private String cvenregcode;

    private String cvenbank;

    private String cvenaccount;

    private Date dvendevdate;

    private String cvenlperson;

    private String cvenphone;

    private String cvenfax;

    private String cvenemail;

    private String cvenperson;

    private String cvenbp;

    private String cvenhand;

    private String cvenpperson;

    private Double ivendisrate;

    private String ivencregrade;

    private Double ivencreline;

    private Integer ivencredate;

    private String cvenpaycond;

    private String cveniaddress;

    private String cvenitype;

    private String cvenheadcode;

    private String cvenwhcode;

    private String cvendepart;

    private Double iapmoney;

    private Date dlastdate;

    private Double ilastmoney;

    private Date dlrdate;

    private Double ilrmoney;

    private Date denddate;

    private Integer ifrequency;

    private Boolean bventax;

    private String cvendefine1;

    private String cvendefine2;

    private String cvendefine3;

    private String ccreateperson;

    private String cmodifyperson;

    private Date dmodifydate;

    private String crelcustomer;

    private Integer iid;

    private String cbarcode;

    private String cvendefine4;

    private String cvendefine5;

    private String cvendefine6;

    private String cvendefine7;

    private String cvendefine8;

    private String cvendefine9;

    private String cvendefine10;

    private Integer cvendefine11;

    private Integer cvendefine12;

    private Double cvendefine13;

    private Double cvendefine14;

    private Date cvendefine15;

    private Date cvendefine16;



    private Double fregistfund;

    private Integer iemployeenum;

    private Short igradeabc;

    private String cmemo;

    private Boolean blicencedate;

    private Date dlicencesdate;

    private Date dlicenceedate;

    private Integer ilicenceadays;

    private Boolean bbusinessdate;

    private Date dbusinesssdate;

    private Date dbusinessedate;

    private Integer ibusinessadays;

    private Boolean bproxydate;

    private Date dproxysdate;

    private Date dproxyedate;

    private Integer iproxyadays;

    private Boolean bpassgmp;

    private Boolean bvencargo;

    private Boolean bproxyforeign;

    private Boolean bvenservice;

    private String cventradeccode;

    private String cvenbankcode;

    private String cvenexchName;

    private Short ivengsptype;

    private Short ivengspauth;

    private String cvengspauthno;

    private String cvenbusinessno;

    private String cvenlicenceno;

    private Boolean bvenoverseas;

    private Boolean bvenaccperiodmng;

    private String cvenpuomprotocol;

    private String cvenotherprotocol;

    private String cvencountrycode;

    private String cvenenname;

    private String cvenenaddr1;

    private String cvenenaddr2;

    private String cvenenaddr3;

    private String cvenenaddr4;

    private String cvenportcode;

    private String cvenprimaryven;

    private Double fvencommisionrate;

    private Double fveninsuerate;

    private Boolean bvenhomebranch;

    private String cvenbranchaddr;

    private String cvenbranchphone;

    private String cvenbranchperson;

    private String cvensscode;

    private String comwhcode;

    private String cvencmprotocol;

    private String cvenimprotocol;

    private Double iventaxrate;

    private Date dvencreatedatetime;

    private String cvenmnemcode;

    private Object cvencontactcode;

    private String cvenbankall;

    private Boolean bisvenattachfile;

    private String clicencerange;

    private String cbusinessrange;

    private String cvengsprange;

    private Date dvengspedate;

    private Date dvengspsdate;

    private Integer ivengspadays;

    private Boolean bretail;
}