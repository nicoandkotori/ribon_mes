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
@TableName("rdrecords09")
public class RdRecords09 {
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

    private Long coutvouchid;

    private String coutvouchtype;

    private BigDecimal isredoutquantity;

    private BigDecimal isredoutnum;

    private String cfree1;

    private String cfree2;

    private Byte iflag;

    private BigDecimal ifnum;

    private BigDecimal ifquantity;

    private Date dvdate;

    private Long itrids;

    private String cposition;

    private String cdefine22;

    private String cdefine23;

    private String cdefine24;

    private String cdefine25;

    private Double cdefine26;

    private Double cdefine27;

    private String citemClass;

    private String citemcode;

    private Long idlsid;

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

    private Boolean bgsp;

    private String cgspstate;

    private String ccheckcode;

    private Long icheckidbaks;

    private String crejectcode;

    private Long irejectids;

    private String ccheckpersoncode;

    private Date dcheckdate;

    private Short cmassunit;

    private Boolean bchecked;

    private Integer ieqdid;

    private Boolean blpusefree;

    private Integer irsrowno;

    private Long ioritrackid;

    private String coritracktype;

    private String cbaccounter;

    private Date dbkeepdate;

    private Boolean bcosting;

    private Boolean bvmiused;

    private BigDecimal ivmisettlequantity;

    private BigDecimal ivmisettlenum;

    private String cvmivencode;

    private Integer iinvsncount;

    private String cwhpersoncode;

    private String cwhpersonname;

    private String cserviceoid;

    private String cbserviceoid;

    private BigDecimal iinvexchrate;

    private String cbdlcode;

    private String corufts;

    private Object strcontractguid;

    private Short iexpiratdatecalcu;

    private String cexpirationdate;

    private Date dexpirationdate;

    private String cciqbookcode;

    private BigDecimal ibondedsumqty;

    private String ccusinvcode;

    private String ccusinvname;

    private Integer iorderdid;

    private Byte iordertype;

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

    private Integer irowno;

    private Object strowguid;

    private Date rowufts;

    private BigDecimal ipreuseqty;

    private BigDecimal ipreuseinum;

    private String cbsourcecodels;

    private Integer igroupno;

    private Integer idebitids;

    private Integer idebitchildids;

    private Integer iimosid;

    private String cpoid;

    private String strcontractid;

    private String strcode;

    private Integer cinvoucherlineid;

    private String cinvouchercode;

    private String cinvouchertype;

    private String cbsysbarcode;

    private BigDecimal ipickedquantity;

    private BigDecimal ipickednum;

    private Integer icrmvouchids;

    private String ccrmvouchcode;

    private Short iposflag;

    private Integer isrcvouchids;

    private String csyssourceautoid;

    public void setDefault() {
        iflag = 0;
        blpusefree = false;
        ioritrackid = 0L;
        bcosting = true;
        bvmiused = false;
        iexpiratdatecalcu = 0;
        iordertype = 0;
        isotype = 0;

    }
}