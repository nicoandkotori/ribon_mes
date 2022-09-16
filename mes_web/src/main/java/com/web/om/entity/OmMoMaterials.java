package com.web.om.entity;

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
@TableName("OM_MOMaterials")
public class OmMoMaterials {
    @TableId(value = "MoMaterialsID")
    private Integer momaterialsid;

    private Integer modetailsid;

    private Integer moid;

    private String cinvcode;

    private BigDecimal iquantity;

    private Date drequireddate;

    private BigDecimal isendqty;

    private String cfree1;

    private String cfree2;

    private String cfree3;

    private String cfree4;

    private String cfree5;

    private String cfree6;

    private String cfree7;

    private String cfree8;

    private String cfree9;

    private String cfree10;

    private BigDecimal fbaseqtyn;

    private BigDecimal fbaseqtyd;

    private BigDecimal fcompscrp;

    private Byte bfvqty;

    private Byte iwiptype;

    private String cwhcode;

    private BigDecimal iunitquantity;

    private String cbatch;

    private Integer opcomponentid;

    private Byte subflag;

    private String cdefine22;

    private String cdefine23;

    private String cdefine24;

    private String cdefine25;

    private BigDecimal cdefine26;

    private BigDecimal cdefine27;

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

    private BigDecimal fbasenumn;

    private BigDecimal iunitnum;

    private BigDecimal inum;

    private BigDecimal isendnum;

    private String cunitid;

    private BigDecimal icomplementqty;

    private BigDecimal icomplementnum;

    private BigDecimal ftransqty;



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

    private Short sotype;

    private String sodid;

    private String csocode;

    private Integer irowno;

    private String cdemandmemo;

    private String cdetailsdemandcode;

    private String cdetailsdemandmemo;

    private String csendtype;

    private BigDecimal fsendapplyqty;

    private BigDecimal fsendapplynum;

    private BigDecimal fapplyqty;

    private BigDecimal fapplynum;

    private String cbmemo;

    private String csubsysbarcode;

    private BigDecimal ipickqty;

    private BigDecimal ipicknum;

    private Byte iproducttype;

    private String cfactorycode;

    //金工默认数据
    public void setDefaultValueForJinGong(){

       bfvqty=Byte.valueOf("0");
       fcompscrp=BigDecimal.ZERO;
       iwiptype=Byte.valueOf("3");
       subflag=Byte.valueOf("0");
        csendtype="0";

    }

    /**
     * 转换mes字段为u8字段
     */
    public void setDataFromMesProduct(OmOrderMaterial material){
        setCinvcode(material.getInvCode());
        setIquantity(material.getPartQty());
    }
}