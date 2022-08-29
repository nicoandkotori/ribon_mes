package com.web.st.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rdrecords01")
public class RdRecords01 {
    @TableId(value = "AutoID")
    private Long autoid;

    private Long id;

    private String cinvcode;

    private BigDecimal inum;

    private BigDecimal iquantity;

    private BigDecimal iunitcost;

    private Object iprice;

    private Object iaprice;

    private BigDecimal ipunitcost;

    private Object ipprice;

    private String cbatch;

    private Long cvouchcode;

    private String cinvouchcode;

    private String cinvouchtype;

    private BigDecimal isoutquantity;

    private BigDecimal isoutnum;

    private String cfree1;

    private String cfree2;

    private Byte iflag;

    private Date dsdate;

    private Object itax;

    private BigDecimal isquantity;

    private BigDecimal isnum;

    private Object imoney;

    private BigDecimal ifnum;

    private BigDecimal ifquantity;

    private Date dvdate;

    private String cposition;

    private String cdefine22;

    private String cdefine23;

    private String cdefine24;

    private String cdefine25;

    private Double cdefine26;

    private Double cdefine27;

    private String citemClass;

    private String citemcode;

    private Long iposid;

    private BigDecimal facost;

    private String cname;

    private String citemcname;

    private String cfree3;

    private String cfree4;

    private String cfree5;

    private String cfree6;

    private String cfree7;

    private String cfree8;

    private String cfree9;

    private String cfree10;

    private String cbarcode;

    private BigDecimal inquantity;

    private BigDecimal innum;

    private String cassunit;

    private Date dmadedate;

    private Integer imassdate;

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

    private Long icheckids;

    private String cbvencode;

    private String chvencode;

    private Boolean bgsp;

    private String cgspstate;

    private Long iarrsid;

    private String ccheckcode;

    private Long icheckidbaks;

    private String crejectcode;

    private Long irejectids;

    private String ccheckpersoncode;

    private Date dcheckdate;

    private BigDecimal ioritaxcost;

    private BigDecimal ioricost;

    private Object iorimoney;

    private Object ioritaxprice;

    private Object iorisum;

    private BigDecimal itaxrate;

    private Object itaxprice;

    private Object isum;

    private Boolean btaxcost;

    private String cpoid;

    private Short cmassunit;

    private Object imaterialfee;

    private BigDecimal iprocesscost;

    private Object iprocessfee;

    private Date dmsdate;

    private Object ismaterialfee;

    private Object isprocessfee;

    private Integer iomodid;

    private String strcontractid;

    private String strcode;

    private Boolean bchecked;

    private Boolean brelated;

    private Long iomomid;

    private Integer imatsettlestate;

    private Integer ibillsettlecount;

    private Boolean blpusefree;

    private Long ioritrackid;

    private String coritracktype;

    private String cbaccounter;

    private Date dbkeepdate;

    private Boolean bcosting;

    private BigDecimal isumbillquantity;

    private Boolean bvmiused;

    private BigDecimal ivmisettlequantity;

    private BigDecimal ivmisettlenum;

    private String cvmivencode;

    private Integer iinvsncount;

    private String cwhpersoncode;

    private String cwhpersonname;

    private BigDecimal impcost;

    private Integer iimosid;

    private Integer iimbsid;

    private String cbarvcode;

    private String dbarvdate;

    private BigDecimal iinvexchrate;

    private String corufts;

    private String comcode;

    private Object strcontractguid;

    private Short iexpiratdatecalcu;

    private String cexpirationdate;

    private Date dexpirationdate;

    private String cciqbookcode;

    private BigDecimal ibondedsumqty;

    private Byte iordertype;

    private Integer iorderdid;

    private String iordercode;

    private Integer iorderseq;

    private String isodid;

    private Byte isotype;

    private String csocode;

    private Integer isoseq;

    private BigDecimal cbatchproperty1;

    private BigDecimal cbatchproperty2;

    private BigDecimal cbatchproperty3;

    private BigDecimal cbatchproperty4;

    private BigDecimal cbatchproperty5;

    private String cbatchproperty6;

    private String cbatchproperty7;

    private String cbatchproperty8;

    private String cbatchproperty9;

    private Date cbatchproperty10;

    private String cbmemo;

    private BigDecimal ifaqty;

    private BigDecimal istax;

    private Integer irowno;

    private Object strowguid;

    private Date rowufts;

    private BigDecimal ipreuseqty;

    private BigDecimal ipreuseinum;

    private Integer idebitids;

    private BigDecimal outcopiedquantity;

    private Integer ioldpartid;

    private BigDecimal foldquantity;

    private String cbsysbarcode;

    private Boolean bmergecheck;

    private Integer imergecheckautoid;

    private Byte bnoitemused;

    private String creworkmocode;

    private Integer ireworkmodetailsid;

    private Integer iproducttype;

    private String cmaininvcode;

    private Integer imainmodetailsid;

    private BigDecimal isharematerialfee;

    private String cplanlotcode;

    private Short bgift;

    private Short iposflag;

    private Integer gcsourceid;

    private Integer gcsourceids;

    private String gcupcardnum;

    private Integer gcupid;

    private Integer gcupids;

    private String csyssourceautoid;

    public void setDefaultValue(){
        iflag = 0;
        isquantity = BigDecimal.ZERO;
        isnum = BigDecimal.ZERO;
        imoney = BigDecimal.ZERO;
        itaxrate = BigDecimal.ZERO;
        btaxcost = true;
        imatsettlestate = 0;
        ibillsettlecount = 0;
        blpusefree = false;
        ioritrackid = Long.valueOf("0");

        iinvexchrate = BigDecimal.ZERO;

        bcosting=true;
        iordertype=0;
        isotype=0;
        iexpiratdatecalcu=0;
        iproducttype=0;
    }
}