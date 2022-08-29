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
@TableName("RdRecord01")
public class RdRecord01 {
    @TableId(value = "ID")
    private Long id;

    private Byte brdflag;

    private String cvouchtype;

    private String cbustype;

    private String csource;

    private String cbuscode;

    private String cwhcode;

    private Date ddate;

    private String ccode;

    private String crdcode;

    private String cdepcode;

    private String cpersoncode;

    private String cptcode;

    private String cstcode;

    private String ccuscode;

    private String cvencode;

    private String cordercode;

    private String carvcode;

    private Long cbillcode;

    private Long cdlcode;

    private String cprobatch;

    private String chandler;

    private String cmemo;

    private Boolean btransflag;

    private String caccounter;

    private String cmaker;

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

    private String dkeepdate;

    private Date dveridate;

    private Boolean bpufirst;

    private Boolean biafirst;

    private Double imquantity;

    private Date darvdate;

    private String cchkcode;

    private Date dchkdate;

    private String cchkperson;

    private Integer vtId;

    private Boolean bisstqc;

    private String cdefine11;

    private String cdefine12;

    private String cdefine13;

    private String cdefine14;

    private Integer cdefine15;

    private Double cdefine16;

    private String gspcheck;

    private Long ipurorderid;

    private Long ipurarriveid;

    private String iarriveid;

    private String isalebillid;

    private Date ufts;

    private BigDecimal itaxrate;

    private Double iexchrate;

    private String cexchName;

    private Boolean bomfirst;

    private Boolean bfrompreyear;

    private Boolean bislsquery;

    private Short biscomplement;

    private Byte idiscounttaxtype;

    private Integer ireturncount;

    private Integer iverifystate;

    private Integer iswfcontrolled;

    private String cmodifyperson;

    private Date dmodifydate;

    private Date dnmaketime;

    private Date dnmodifytime;

    private Date dnverifytime;

    private Integer bredvouch;

    private String cvenpuomprotocol;

    private Date dcreditstart;

    private Integer icreditperiod;

    private Date dgatheringdate;

    private Byte bcredit;

    private Integer iflowid;

    private String cpzid;

    private String csourcels;

    private String csourcecodels;

    private Integer iprintcount;

    private String csysbarcode;

    private String ccurrentauditor;

    private String cchecksignflag;

    private String cgcroutecode;

    private String csyssource;

    private String csyssourceid;

    public void setDefaultValueForWw(){
        brdflag = 1;
        cvouchtype = "01";
        cbustype = "委外加工";
        csource = "委外订单";
        crdcode = "12";
        btransflag = false;
        bpufirst = false;
        biafirst = false;
        bisstqc = false;
        bomfirst = false;
        bfrompreyear = false;
        biscomplement = 0;
        idiscounttaxtype = 0;
        ireturncount = 0;
        iverifystate = 0;
        iswfcontrolled = 0;
        bredvouch = 0;
        iprintcount=0;
        bcredit = 0;
        dnmaketime= new Date();
        vtId = 27;
    }
}