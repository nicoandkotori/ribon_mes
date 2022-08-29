package com.web.basicinfo.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Inventory")
public class Inventory extends Model<Inventory> {
    @TableId(value = "cInvCode")
    private String cinvcode;
    @TableField(value = "cInvAddCode")
    private String cinvaddcode;
    @TableField(value = "cInvName")
    private String cinvname;
    @TableField(value = "cInvStd")
    private String cinvstd;
    @TableField(value = "cInvCCode")
    private String cinvccode;
    @TableField(value = "cVenCode")
    private String cvencode;
    @TableField(value = "cReplaceItem")
    private String creplaceitem;
    @TableField(value = "cPosition")
    private String cposition;
    @TableField(value = "bSale")
    private Boolean bsale;
    @TableField(value = "bPurchase")
    private Boolean bpurchase;
    @TableField(value = "bSelf")
    private Boolean bself;
    @TableField(value = "bComsume")
    private Boolean bcomsume;
    @TableField(value = "bProducing")
    private Boolean bproducing;
    @TableField(value = "bService")
    private Boolean bservice;
    @TableField(value = "bAccessary")
    private Boolean baccessary;
    @TableField(value = "iTaxRate")
    private BigDecimal itaxrate;
    @TableField(value = "iInvWeight")
    private Double iinvweight;

    private BigDecimal ivolume;

    private Double iinvrcost;

    private Double iinvsprice;

    private Double iinvscost;

    private Double iinvlscost;

    private Double iinvncost;

    private BigDecimal iinvadvance;

    private Double iinvbatch;

    private BigDecimal isafenum;

    private BigDecimal itopsum;

    private BigDecimal ilowsum;

    private Double ioverstock;

    private String cinvabc;

    private Boolean binvquality;

    private Boolean binvbatch;

    private Boolean binventrust;

    private Boolean binvoverstock;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dsdate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dedate;

    private Boolean bfree1;

    private Boolean bfree2;

    private String cinvdefine1;

    private String cinvdefine2;

    private String cinvdefine3;

    private Integer iId;

    private Boolean binvtype;

    private Double iinvmpcost;

    private String cquality;

    private Double iinvsalecost;

    private Double iinvscost1;

    private Double iinvscost2;

    private Double iinvscost3;

    private Boolean bfree3;

    private Boolean bfree4;

    private Boolean bfree5;

    private Boolean bfree6;

    private Boolean bfree7;

    private Boolean bfree8;

    private Boolean bfree9;

    private Boolean bfree10;

    private String ccreateperson;

    private String cmodifyperson;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dmodifydate;

    private Double fsubscribepoint;

    private Double fvagquantity;

    private String cvaluetype;

    private Boolean bfixexch;

    private Double foutexcess;

    private Double finexcess;

    private Short imassdate;

    private Short iwarndays;

    private Double fexpensesexch;

    private Boolean btrack;

    private Boolean bserial;

    private Boolean bbarcode;

    private Integer iid;

    private String cbarcode;

    private String cinvdefine4;

    private String cinvdefine5;

    private String cinvdefine6;

    private String cinvdefine7;

    private String cinvdefine8;

    private String cinvdefine9;

    private String cinvdefine10;

    private Integer cinvdefine11;

    private Integer cinvdefine12;

    private Double cinvdefine13;

    private Double cinvdefine14;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cinvdefine15;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date cinvdefine16;

    private Byte igrouptype;

    private String cgroupcode;

    private String ccomunitcode;

    private String casscomunitcode;

    private String csacomunitcode;

    private String cpucomunitcode;

    private String cstcomunitcode;

    private String ccacomunitcode;

    private String cfrequency;

    private Short ifrequency;

    private Short idays;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dlastdate;

    private Double iwastage;

    private Boolean bsolitude;

    private String centerprise;

    private String caddress;

    private String cfile;

    private String clabel;

    private String ccheckout;

    private String clicence;

    private Boolean bspecialties;

    private String cdefwarehouse;

    private Double ihighprice;

    private Double iexpsalerate;

    private String cpricegroup;

    private String coffergrade;

    private Double iofferrate;

    private String cmonth;

    private Short iadvancedate;

    private String ccurrencyname;

    private String cproduceaddress;

    private String cproducenation;

    private String cregisterno;

    private String centerno;

    private String cpackingtype;

    private String cenglishname;

    private Boolean bpropertycheck;

    private String cpreparationtype;

    private String ccommodity;

    private Byte irecipebatch;

    private String cnotpatentname;

//    private Date pubufts;

    private Boolean bpromotsales;

    private Short iplanpolicy;

    private Short iropmethod;

    private Short ibatchrule;

    private Double fbatchincrement;

    private Integer iassureprovidedays;

    private Short iteststyle;

    private Short idtmethod;

    private Double fdtrate;

    private Double fdtnum;

    private String cdtunit;

    private Short idtstyle;

    private Integer iqtmethod;

    private Object pictureguid;

    private Boolean bplaninv;

    private Boolean bproxyforeign;

    private Boolean batomodel;

    private Boolean bcheckitem;

    private Boolean bptomodel;

    private Boolean bequipment;

    private String cproductunit;

    private Double forderuplimit;

    private Short cmassunit;

    private Double fretailprice;

    private String cinvdepcode;

    private Integer ialteradvance;

    private Double falterbasenum;

    private String cplanmethod;

    private Boolean bmps;

    private Boolean brop;

    private Boolean breplan;

    private String csrpolicy;

    private Boolean bbillunite;

    private Integer isupplyday;

    private Double fsupplymulti;

    private Double fminsupply;

    private Boolean bcutmantissa;

    private String cinvpersoncode;

    private Integer iinvtfid;

    private String cengineerfigno;

    private Boolean bintotalcost;

    private Short isupplytype;

    private Boolean bconfigfree1;

    private Boolean bconfigfree2;

    private Boolean bconfigfree3;

    private Boolean bconfigfree4;

    private Boolean bconfigfree5;

    private Boolean bconfigfree6;

    private Boolean bconfigfree7;

    private Boolean bconfigfree8;

    private Boolean bconfigfree9;

    private Boolean bconfigfree10;

    private Short idtlevel;

    private String cdtaql;

    private Boolean bperioddt;

    private String cdtperiod;

    private Integer ibigmonth;

    private Integer ibigday;

    private Integer ismallmonth;

    private Integer ismallday;

    private Boolean boutinvdt;

    private Boolean bbackinvdt;

    private Short ienddtstyle;

    private Boolean bdtwarninv;

    private Double fbacktaxrate;

    private String cciqcode;

    private String cwgroupcode;

    private String cwunit;

    private Double fgrossw;

    private String cvgroupcode;

    private String cvunit;

    private Double flength;

    private Double fwidth;

    private Double fheight;

    private Integer idtucounter;

    private Integer idtdcounter;

    private Integer ibatchcounter;

    private String cshopunit;

    private String cpurpersoncode;

    private Boolean bimportmedicine;

    private Boolean bfirstbusimedicine;

    private Boolean bforeexpland;

    private String cinvplancode;

    private BigDecimal fconvertrate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dreplacedate;

    private Boolean binvmodel;

    private Boolean bkccutmantissa;

    private Boolean breceiptbydt;

    private BigDecimal iimptaxrate;

    private Double iexptaxrate;

    private Boolean bexpsale;

    private Double idrawbatch;

    private Boolean bcheckbsatp;

    private String cinvprojectcode;

    private Short itestrule;

    private String crulecode;

    private Boolean bcheckfree1;

    private Boolean bcheckfree2;

    private Boolean bcheckfree3;

    private Boolean bcheckfree4;

    private Boolean bcheckfree5;

    private Boolean bcheckfree6;

    private Boolean bcheckfree7;

    private Boolean bcheckfree8;

    private Boolean bcheckfree9;

    private Boolean bcheckfree10;

    private Boolean bbommain;

    private Boolean bbomsub;

    private Boolean bproductbill;

    private Short icheckatp;

    private Integer iinvatpid;

    private Integer iplantfday;

    private Integer ioverlapday;

    private Boolean bpiece;

    private Boolean bsrvitem;

    private Boolean bsrvfittings;

    private Double fmaxsupply;

    private Double fminsplit;

    private Boolean bspecialorder;

    private Boolean btracksalebill;

    private String cinvmnemcode;

    private Short iplandefault;

    private Double ipfbatchqty;

    private Integer iallocateprintdgt;

    private Boolean bcheckbatch;

    private Boolean bmngoldpart;

    private Short ioldpartmngrule;

    private String cretaildefreturnwh;
    @TableField(exist = false)
    private String ccomunitcodeassist;

    @TableField(exist = false)
    private String ccomunitnameassist;
    @TableField(exist = false)
    private String ccomunitname;
    @TableField(exist = false)
    private BigDecimal ichangrate;

    @TableField(exist = false)
    private List<Inventory> nodes;//初始化菜单用
    @TableField(exist = false)
    private String text;//初始化菜单用
    @TableField(exist = false)
    private String color;//初始化菜单用
    @TableField(exist = false)
    private String href;//初始化菜单用
    @TableField(exist = false)
    private List<Inventory> children;//初始化菜单用
    @TableField(exist = false)
    private String element;//初始化菜单用
    @TableField(exist = false)
    private String cinvcname;//
    @TableField(exist = false)
    private String importStatus;//
    @TableField(exist = false)
    private String parentCode;//
    @TableField(exist = false)
    private String parentName;//

    @TableField(exist = false)
    private String  bproxyforeignStr;
    public String getBproxyforeignStr(){
        if(bproxyforeign!=null&&bproxyforeign==true){
            return "是";
        }else{
            return "否";
        }
    }

    @TableField(exist = false)
    private String bsaleStr;
    public String getBsaleStr(){
        if(bsale!=null&&bsale==true){
            return "是";
        }else{
            return "否";
        }
    }
    @TableField(exist = false)
    private String bpurchaseStr;
    public String getBpurchaseStr(){
        if(bpurchase!=null&&bpurchase==true){
            return "是";
        }else{
            return "否";
        }
    }
    @TableField(exist = false)
    private String bselfStr;
    public String getBselfStr(){
        if(bself!=null&&bself==true){
            return "是";
        }else{
            return "否";
        }
    }
    @TableField(exist = false)
    private String bcomsumeStr;
    public String getBcomsumeStr(){
        if(bcomsume!=null&&bcomsume==true){
            return "是";
        }else{
            return "否";
        }
    }


    public void setDefaultValue(){

        bself=false;
        iplandefault=0;
        bcomsume=false;
        bbommain=false;
        bbomsub=false;
        bpurchase=false;
        bsale=false;
        bproducing=false;
        baccessary=false;
        bproductbill=false;


        itaxrate=BigDecimal.valueOf(13);
        iimptaxrate=BigDecimal.valueOf(13);
        ivolume=BigDecimal.ZERO;
        iinvadvance=BigDecimal.ZERO;
        isafenum=BigDecimal.ZERO;
        itopsum=BigDecimal.ZERO;
        ilowsum=BigDecimal.ZERO;
        binvbatch=false;
        binvquality=false;
        binventrust=false;
        binvoverstock=false;
        dsdate=new Date();
        bfree1=false;
        bfree2=false;
        cinvdefine1="";
        cinvdefine2="";
        cinvdefine3="";
        binvtype=false;

        bfree3=false;
        bfree4=false;
        bfree5=false;
        bfree6=false;
        bfree7=false;
        bfree8=false;
        bfree9=false;
        bfree10=false;
        ccreateperson="demo";
        bfixexch=false;
        btrack=false;
        bserial=false;
        bbarcode=false;

        bsolitude=false;
        bspecialties=false;
        bpropertycheck=false;
        irecipebatch=Byte.valueOf("0");
        bpromotsales=false;
        bplaninv=false;
        bproxyforeign=false;
        batomodel=false;
        bcheckitem=false;
        bptomodel=false;
        bequipment=false;
        cmassunit=Short.valueOf("0");

        cplanmethod="L";
        bmps=false;
        brop=false;
        breplan=false;
        csrpolicy="PE";
        bbillunite=false;
        bcutmantissa=false;
        bintotalcost=true;
        isupplytype=Short.valueOf("0");


        bperioddt=false;
        boutinvdt=false;
        bbackinvdt=false;
        bdtwarninv=false;
        bimportmedicine=false;
        bfirstbusimedicine=false;
        bforeexpland=false;
        fconvertrate=BigDecimal.ONE;
        binvmodel=false;
        bkccutmantissa=false;
        breceiptbydt=false;
        bexpsale=false;
        bcheckbsatp=false;
        icheckatp=Short.valueOf("0");

        bpiece=false;
        bsrvitem=false;
        bsrvfittings=false;
        bspecialorder=false;
        //modify yao  on 2022-7-8
        btracksalebill=true;
        iallocateprintdgt=4;
        bcheckbatch=false;
        bmngoldpart=false;

        bconfigfree1=false;
        bconfigfree2=false;
        bconfigfree3=false;
        bconfigfree4=false;
        bconfigfree5=false;
        bconfigfree6=false;
        bconfigfree7=false;
        bconfigfree8=false;
        bconfigfree9=false;
        bconfigfree10=false;

        bcheckfree1=false;
        bcheckfree2=false;
        bcheckfree3=false;
        bcheckfree4=false;
        bcheckfree5=false;
        bcheckfree6=false;
        bcheckfree7=false;
        bcheckfree8=false;
        bcheckfree9=false;
        bcheckfree10=false;

    }

    public void setImportValue(){


        itaxrate=BigDecimal.valueOf(13);
        iimptaxrate=BigDecimal.valueOf(13);
        ivolume=BigDecimal.ZERO;
        iinvadvance=BigDecimal.ZERO;
        isafenum=BigDecimal.ZERO;
        itopsum=BigDecimal.ZERO;
        ilowsum=BigDecimal.ZERO;
        binvbatch=false;
        binvquality=false;
        binventrust=false;
        binvoverstock=false;

        bfree1=false;
        bfree2=false;
        cinvdefine1="";
        cinvdefine2="";
        cinvdefine3="";
        binvtype=false;

        bfree3=false;
        bfree4=false;
        bfree5=false;
        bfree6=false;
        bfree7=false;
        bfree8=false;
        bfree9=false;
        bfree10=false;
        ccreateperson="demo";
        bfixexch=false;
        btrack=false;
        bserial=false;
        bbarcode=false;

        bsolitude=false;
        bspecialties=false;
        bpropertycheck=false;
        irecipebatch=Byte.valueOf("0");
        bpromotsales=false;
        bplaninv=false;
        bproxyforeign=false;
        batomodel=false;
        bcheckitem=false;
        bptomodel=false;
        bequipment=false;
        cmassunit=Short.valueOf("0");

        cplanmethod="L";
        bmps=false;
        brop=false;
        breplan=false;
        csrpolicy="PE";
        bbillunite=false;
        bcutmantissa=false;
        bintotalcost=true;
        isupplytype=Short.valueOf("0");


        bperioddt=false;
        boutinvdt=false;
        bbackinvdt=false;
        bdtwarninv=false;
        bimportmedicine=false;
        bfirstbusimedicine=false;
        bforeexpland=false;
        fconvertrate=BigDecimal.ONE;
        binvmodel=false;
        bkccutmantissa=false;
        breceiptbydt=false;
        bexpsale=false;
        bcheckbsatp=false;
        icheckatp=Short.valueOf("0");

        bpiece=false;
        bsrvitem=false;
        bsrvfittings=false;
        bspecialorder=false;
        //modify yao  on 2022-7-8
        btracksalebill=true;
        iallocateprintdgt=4;
        bcheckbatch=false;
        bmngoldpart=false;

        bconfigfree1=false;
        bconfigfree2=false;
        bconfigfree3=false;
        bconfigfree4=false;
        bconfigfree5=false;
        bconfigfree6=false;
        bconfigfree7=false;
        bconfigfree8=false;
        bconfigfree9=false;
        bconfigfree10=false;

        bcheckfree1=false;
        bcheckfree2=false;
        bcheckfree3=false;
        bcheckfree4=false;
        bcheckfree5=false;
        bcheckfree6=false;
        bcheckfree7=false;
        bcheckfree8=false;
        bcheckfree9=false;
        bcheckfree10=false;

    }

}