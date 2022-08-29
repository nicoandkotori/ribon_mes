package com.web.sa.entity;

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
@TableName("SO_SOMain")
public class SoMain {
    @TableId(value = "ID")
    private Integer id;

    private String cstcode;

    private Date ddate;

    private String csocode;

    private String ccuscode;

    private String cdepcode;

    private String cpersoncode;

    private String csccode;

    private String ccusoaddress;

    private String cpaycode;

    private String cexchName;

    private BigDecimal iexchrate;

    private BigDecimal itaxrate;

    private Object imoney;

    private String cmemo;

    private Byte istatus;

    private String cmaker;

    private String cverifier;

    private String ccloser;

    private Boolean bdisflag;

    private String cdefine1;

    private String cdefine2;

    private String cdefine3;

    private Date cdefine4;

    private Integer cdefine5;

    private Date cdefine6;

    private Double cdefine7;

    private String cdefine8;

    private String cdefine9;

    private String cdefine10;

    private Boolean breturnflag;

    private String ccusname;

    private Boolean border;

    private Integer ivtid;



    private String cchanger;

    private String cbustype;

    private String ccrechpname;

    private String cdefine11;

    private String cdefine12;

    private String cdefine13;

    private String cdefine14;

    private Integer cdefine15;

    private Double cdefine16;

    private String coppcode;

    private String clocker;

    private Date dpremodatebt;

    private Date dpredatebt;

    private String cgatheringplan;

    private String caddcode;

    private Integer iverifystate;

    private Byte ireturncount;

    private Byte iswfcontrolled;

    private String icreditstate;

    private String cmodifier;

    private Date dmoddate;

    private Date dverifydate;

    private String ccusperson;

    private Date dcreatesystime;

    private Date dverifysystime;

    private Date dmodifysystime;

    private Integer iflowid;

    private Boolean bcashsale;

    private String cgathingcode;

    private String cchangeverifier;

    private Date dchangeverifydate;

    private Date dchangeverifytime;

    private Object outid;

    private String ccuspersoncode;

    private Date dclosedate;

    private Date dclosesystime;

    private Double iprintcount;

    private Double fbookratio;

    private Boolean bmustbook;

    private Object fbooksum;

    private Object fbooknatsum;

    private Object fgbooksum;

    private Object fgbooknatsum;

    private String csvouchtype;

    private String ccrmpersoncode;

    private String ccrmpersonname;

    private String cmainpersoncode;

    private String csysbarcode;

    private Integer ioppid;

    private String optntyName;

    private String ccurrentauditor;

    private Short contractStatus;

    private String csscode;

    private String cinvoicecompany;

    private String cattachment;

    private String cebtrnumber;

    private String cebbuyer;

    private String cebbuyernote;

    private String ccontactname;

    private String cebprovince;

    private String cebcity;

    private String cebdistrict;

    private String cmobilephone;

    private String cinvoicecusname;

    private String cgcroutecode;

    private String cbcode;


    public void setDefaultValue(){
        cstcode="01";
        cexchName="人民币";
        iexchrate=BigDecimal.ONE;
        itaxrate=BigDecimal.valueOf(13);
        bdisflag=false;
        breturnflag=false;
        border=false;
        ivtid=95;
        cbustype="普通销售";
        caddcode="0001";
        istatus=Byte.valueOf("1");
        iverifystate=2;
        iswfcontrolled=Byte.valueOf("1");
        dcreatesystime=new Date();
        bcashsale=false;
        bmustbook=false;
        contractStatus=Short.valueOf("1");
//        cbcode="";
    }
}