package com.web.om.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("OM_MOMain")
public class OmMoMain {
    @TableId(value = "MoID")
    private Integer moid;

    private String ccode;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ddate;

    private String cvencode;

    private String cdepcode;

    private String cpersoncode;

    private String cptcode;

    private String carrivalplace;

    private String csccode;

    private String cexchName;

    private BigDecimal nflat;

    private BigDecimal itaxrate;

    private String cpaycode;

    private Object icost;

    private Object ibargain;

    private String cmemo;

    private String cmaker;

    private String cverifier;

    private String ccloser;

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

    private Integer ivtid;


    private String cchanger;

    private String cbustype;

    private String cdefine11;

    private String cdefine12;

    private String cdefine13;

    private String cdefine14;

    private Integer cdefine15;

    private Double cdefine16;

    private String clocker;

    private Byte cstate;

    private Integer ireturncount;

    private Integer iverifystatenew;

    private Integer iswfcontrolled;

    private String cmodifier;

    private Date dcreatetime;

    private Date dmodifydate;

    private Date dmodifytime;

    private Date dverifydate;

    private Date dverifytime;

    private Date daltertime;

    private String cchangeverifier;

    private Date dchangeverifydate;

    private Date dchangeverifytime;

    private String cvenpuomprotocol;

    private Integer iprintcount;

    private Date dclosedate;

    private Date dclosetime;

    private String ccleanver;

    private String ccontactcode;

    private String cvenperson;

    private String cvenbank;

    private String cvenaccount;

    private String csrccode;

    private String csysbarcode;

    private String ccurrentauditor;

    private Byte iordertype;

    private Byte brework;

    @TableField(exist = false)
    private String cvenname;

    @TableField(exist = false)
    private String cvenabbname;
    @TableField(exist = false)
    private String cvenphone;
    //金工默认数据
    public void setDefaultValueForJinGong(){

       cptcode="2";
       nflat=BigDecimal.ONE;
       cpaycode="1";
       ivtid=8157;
       cbustype="委外加工";
       cstate=Byte.valueOf("0");
       ireturncount=0;
       iverifystatenew=0;
       iswfcontrolled=0;
       dcreatetime=new Date();
       iprintcount=0;
       iordertype=Byte.valueOf("1");

    }

    /**
     * 转换mes字段为u8字段
     *
     * @param main 主要
     */
    public void setDataFromMesMain(OmOrderMain main){
        setMoid(main.getU8Id());
        setCcode(main.getVouchCode());
        setDdate(main.getVouchDate());
        setCdefine2(main.getContractOm());
        setCvenname(main.getVenName());
        setCvencode(main.getVenCode());
        setCdefine1(main.getContractSale());
        setCdepcode(main.getDepCode());
        setCpersoncode(main.getPersonCode());
        setCdefine14(main.getTransportWay());
        setCmemo(main.getRemark());
    }
    

}