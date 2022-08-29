package com.web.mo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bom_bom")
public class Bom {
    @TableId(value = "BomID")
    private Integer bomid;

    private Byte bomtype;

    private Integer version;

    private String versiondesc;

    private Date versioneffdate;

    private Date versionenddate;

    private String identcode;

    private String identdesc;

    private Integer applydid;

    private Date createdate;

    private String createuser;

    private Date modifydate;

    private String modifyuser;

    private Integer updcount;



    private String define1;

    private String define2;

    private String define3;

    private Date define4;

    private Integer define5;

    private Date define6;

    private Double define7;

    private String define8;

    private String define9;

    private String define10;

    private String define11;

    private String define12;

    private String define13;

    private String define14;

    private Integer define15;

    private Double define16;

    private Integer vtid;

    private Byte status;

    private Byte orgstatus;

    private Date relsdate;

    private String relsuser;

    private Date relstime;

    private Date createtime;

    private Date modifytime;

    private Date closedate;

    private String closeuser;

    private Date closetime;

    private Integer iprintcount;

    private Byte iswfcontrolled;

    private Integer iverifystate;

    private Integer ireturncount;

    private Integer auditstatus;

    private String ccurrentauditor;

    private Boolean bprocessproduct;

    private Boolean bprocessmaterial;

    public void setDefValue() throws Exception{
        versioneffdate= DateUtil.parseStrToDate("2000-01-01","yyyy-MM-dd");
        versionenddate=DateUtil.parseStrToDate("2099-12-31","yyyy-MM-dd");
        createdate=DateUtil.parseStrToDate(DateUtil.getDateStr(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd");
        updcount=1;
        status=3;
        orgstatus=1;
        createtime=new Date();
        iprintcount=0;
        iswfcontrolled=0;
        iverifystate=0;
        ireturncount=0;
        auditstatus=1;
        bprocessmaterial=false;
        bprocessproduct=false;
        relsdate=DateUtil.parseStrToDate(DateUtil.getDateStr(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd");
        relstime=new Date();
    }
}