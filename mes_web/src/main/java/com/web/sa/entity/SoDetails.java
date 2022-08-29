package com.web.sa.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.corba.se.impl.encoding.CodeSetConversion;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SO_SODetails")
public class SoDetails {
    @TableId(value = "AutoID", type = IdType.AUTO)
    private Integer autoid;

    private String csocode;

    private String cinvcode;

    private Date dpredate;

    private BigDecimal iquantity;

    private BigDecimal inum;

    private BigDecimal iquotedprice;

    private BigDecimal iunitprice;

    private BigDecimal itaxunitprice;

    private BigDecimal imoney;

    private BigDecimal itax;

    private BigDecimal isum;

    private BigDecimal idiscount;

    private BigDecimal inatunitprice;

    private BigDecimal inatmoney;

    private BigDecimal inattax;

    private BigDecimal inatsum;

    private BigDecimal inatdiscount;

    private BigDecimal ifhnum;

    private BigDecimal ifhquantity;

    private BigDecimal ifhmoney;

    private BigDecimal ikpquantity;

    private BigDecimal ikpnum;

    private BigDecimal ikpmoney;

    private String cmemo;

    private String cfree1;

    private String cfree2;

    private Integer bfh;

    private Integer isosid;

    private BigDecimal kl;

    private BigDecimal kl2;

    private String cinvname;

    private BigDecimal itaxrate;

    private String cdefine22;

    private String cdefine23;

    private String cdefine24;

    private String cdefine25;

    private Double cdefine26;

    private Double cdefine27;

    private String citemcode;

    private String citemClass;

    private String citemname;

    private String citemCname;

    private String cfree3;

    private String cfree4;

    private String cfree5;

    private String cfree6;

    private String cfree7;

    private String cfree8;

    private String cfree9;

    private String cfree10;

    private BigDecimal iinvexchrate;

    private String cunitid;

    private Integer id;

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

    private BigDecimal fpurquan;

    private BigDecimal fsalecost;

    private BigDecimal fsaleprice;

    private String cquocode;

    private Integer iquoid;

    private String cscloser;

    private Date dpremodate;

    private Integer irowno;

    private Integer icusbomid;

    private BigDecimal imoquantity;

    private String ccontractid;

    private String ccontracttagcode;

    private Object ccontractrowguid;

    private Integer ippartseqid;

    private Integer ippartid;

    private BigDecimal ippartqty;

    private String ccusinvcode;

    private String ccusinvname;

    private BigDecimal iprekeepquantity;

    private BigDecimal iprekeepnum;

    private BigDecimal iprekeeptotquantity;

    private BigDecimal iprekeeptotnum;

    private Date dreleasedate;

    private BigDecimal fcusminprice;

    private BigDecimal fimquantity;

    private BigDecimal fomquantity;

    private Boolean ballpurchase;

    private BigDecimal finquantity;

    private BigDecimal icostquantity;

    private Object icostsum;

    private BigDecimal foutquantity;

    private BigDecimal foutnum;

    private Object iexchsum;

    private Object imoneysum;


    private Integer iaoids;

    private String cpreordercode;

    private BigDecimal fretquantity;

    private BigDecimal fretnum;

    private Date dbclosedate;

    private Date dbclosesystime;

    private Boolean borderbom;

    private Byte borderbomover;

    private Short idemandtype;

    private String cdemandcode;

    private String cdemandmemo;

    private BigDecimal fpursum;

    private BigDecimal fpurbillqty;

    private BigDecimal fpurbillsum;

    private Long iimid;

    private String ccorvouchtype;

    private Integer icorrowno;

    private Boolean busecusbom;

    private Object bodyOutid;

    private BigDecimal fveridispqty;

    private Object fveridispsum;

    private Boolean bsaleprice;

    private Boolean bgift;

    private Integer forecastdid;

    private String cdetailsdemandcode;

    private String cdetailsdemandmemo;

    private BigDecimal ftransedqty;

    private String cbsysbarcode;

    private BigDecimal fappqty;

    private String cparentcode;

    private String cchildcode;

    private BigDecimal fchildqty;

    private BigDecimal fchildrate;

    private Byte icalctype;

    private Long icurpartid;

    private String cfactorycode;

    private Integer gcsourceid;

    private Integer gcsourceids;

    public void setDefaultValue(){
        kl=BigDecimal.valueOf(100);
        kl2=BigDecimal.valueOf(100);
        itaxrate=BigDecimal.valueOf(13);
        fsalecost=BigDecimal.ZERO;
        fsaleprice=BigDecimal.ZERO;
        fcusminprice=BigDecimal.ZERO;
        ballpurchase=false;
        borderbom=false;
        borderbomover=Byte.valueOf("0");
        busecusbom=false;
        bgift=false;
        icalctype= Byte.valueOf("0");
    }
}