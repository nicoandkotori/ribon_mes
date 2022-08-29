package com.web.basicinfo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Inventory_Sub")
public class InventorySub extends Model<InventorySub> {

    @TableId(value = "cInvSubCode")
    private String cinvsubcode;

    private Double fbuyexcess;

    private Short isurenesstype;

    private Short idatetype;

    private Integer idatesum;

    private Short idynamicsurenesstype;

    private Double ibestrowsum;

    private Double ipercentumsum;

    private Boolean bisattachfile;

    private Boolean binbyprocheck;

    private Short irequiretrackstyle;

    private Short iexpiratdatecalcu;

    private Short ibomexpandunittype;

    private Boolean bpurpricefree1;

    private Boolean bpurpricefree2;

    private Boolean bpurpricefree3;

    private Boolean bpurpricefree4;

    private Boolean bpurpricefree5;

    private Boolean bpurpricefree6;

    private Boolean bpurpricefree7;

    private Boolean bpurpricefree8;

    private Boolean bpurpricefree9;

    private Boolean bpurpricefree10;

    private Boolean bompricefree1;

    private Boolean bompricefree2;

    private Boolean bompricefree3;

    private Boolean bompricefree4;

    private Boolean bompricefree5;

    private Boolean bompricefree6;

    private Boolean bompricefree7;

    private Boolean bompricefree8;

    private Boolean bompricefree9;

    private Boolean bompricefree10;

    private Boolean bsalepricefree1;

    private Boolean bsalepricefree2;

    private Boolean bsalepricefree3;

    private Boolean bsalepricefree4;

    private Boolean bsalepricefree5;

    private Boolean bsalepricefree6;

    private Boolean bsalepricefree7;

    private Boolean bsalepricefree8;

    private Boolean bsalepricefree9;

    private Boolean bsalepricefree10;

    private Double finvoutuplimit;

    private Boolean bbondedinv;

    private Boolean bbatchcreate;

    private Boolean bbatchproperty1;

    private Boolean bbatchproperty2;

    private Boolean bbatchproperty3;

    private Boolean bbatchproperty4;

    private Boolean bbatchproperty5;

    private Boolean bbatchproperty6;

    private Boolean bbatchproperty7;

    private Boolean bbatchproperty8;

    private Boolean bbatchproperty9;

    private Boolean bbatchproperty10;

    private Boolean bcontrolfreerange1;

    private Boolean bcontrolfreerange2;

    private Boolean bcontrolfreerange3;

    private Boolean bcontrolfreerange4;

    private Boolean bcontrolfreerange5;

    private Boolean bcontrolfreerange6;

    private Boolean bcontrolfreerange7;

    private Boolean bcontrolfreerange8;

    private Boolean bcontrolfreerange9;

    private Boolean bcontrolfreerange10;

    private BigDecimal finvciqexch;

    private Integer iwarrantyperiod;

    private Short iwarrantyunit;

    private Boolean binvkeypart;

    private Integer iacceptearlydays;

    private Double fprocesscost;

    private Double fcurllaborcost;

    private Double fcurlvarmanucost;

    private Double fcurlfixmanucost;

    private Double fcurlomcost;

    private Double fnextllaborcost;

    private Double fnextlvarmanucost;

    private Double fnextlfixmanucost;

    private Double fnextlomcost;

    private Date dinvcreatedatetime;

    private String cinvappdocno;

    private Boolean bpuquota;

    private Boolean binvrohs;

    private Boolean bprjmat;

    private BigDecimal fprjmatlimit;

    private Boolean binvasset;

    private Boolean bsrvproduct;

    private Integer iacceptdelaydays;

    private Integer iplancheckday;

    private Integer imaterialscycle;

    private Short idrawtype;

    private Boolean bsckeyprojections;

    private Integer isupplyperiodtype;

    private Integer itimebucketid;

    private Integer iavailabilitydate;

    private Double fmaterialcost;

    private Boolean bimport;

    private Integer inearrejectdays;

    private Boolean bchecksubitemcost;

    private BigDecimal froundfactor;

    private Boolean bconsiderfreestock;

    private Boolean bsuitretail;

    private Boolean bfeaturematch;

    private Boolean bproducebyfeatureallocate;

    private Boolean bmaintenance;

    private Integer imaintenancecycle;

    private Short imaintenancecycleunit;

    private Boolean bcoupon;

    private Boolean bstorecard;

    private Boolean bprocessproduct;

    private Boolean bprocessmaterial;

    public void setDefaultValue(){
        isurenesstype=Short.valueOf("1");
        bisattachfile=false;
        binbyprocheck=true;
        irequiretrackstyle=Short.valueOf("2");
        iexpiratdatecalcu=Short.valueOf("0");
        ibomexpandunittype=Short.valueOf("1");
        bbondedinv=false;
        bbatchcreate=false;
        finvciqexch=BigDecimal.ONE;
        iwarrantyunit=Short.valueOf("0");
        binvkeypart=true;
        iacceptdelaydays=0;
        dinvcreatedatetime=new Date();
        bpuquota=false;
        binvrohs=false;
        bprjmat=false;
        binvasset=false;
        bsrvproduct=false;
        iacceptearlydays=999;
        iplancheckday=0;
        idrawtype=Short.valueOf("0");
        bsckeyprojections=false;
        isupplyperiodtype=1;
        iavailabilitydate=1;
        bimport=false;
        bchecksubitemcost=true;
        froundfactor=BigDecimal.ZERO;
        bconsiderfreestock=true;
        bsuitretail=false;
        bfeaturematch=false;
        bproducebyfeatureallocate=false;
        bmaintenance=false;
        imaintenancecycleunit=Short.valueOf("0");
        bcoupon=false;
        bstorecard=false;
        bprocessmaterial=false;
        bprocessproduct=false;

        bbatchproperty1=false;
        bbatchproperty2=false;
        bbatchproperty3=false;
        bbatchproperty4=false;
        bbatchproperty5=false;
        bbatchproperty6=false;
        bbatchproperty7=false;
        bbatchproperty8=false;
        bbatchproperty9=false;
        bbatchproperty10=false;

        bcontrolfreerange1=false;
        bcontrolfreerange2=false;
        bcontrolfreerange3=false;
        bcontrolfreerange4=false;
        bcontrolfreerange5=false;
        bcontrolfreerange6=false;
        bcontrolfreerange7=false;
        bcontrolfreerange8=false;
        bcontrolfreerange9=false;
        bcontrolfreerange10=false;

        bpurpricefree1=false;
        bpurpricefree2=false;
        bpurpricefree3=false;
        bpurpricefree4=false;
        bpurpricefree5=false;
        bpurpricefree6=false;
        bpurpricefree7=false;
        bpurpricefree8=false;
        bpurpricefree9=false;
        bpurpricefree10=false;

        bompricefree1=false;
        bompricefree2=false;
        bompricefree3=false;
        bompricefree4=false;
        bompricefree5=false;
        bompricefree6=false;
        bompricefree7=false;
        bompricefree8=false;
        bompricefree9=false;
        bompricefree10=false;

        bsalepricefree1=false;
        bsalepricefree2=false;
        bsalepricefree3=false;
        bsalepricefree4=false;
        bsalepricefree5=false;
        bsalepricefree6=false;
        bsalepricefree7=false;
        bsalepricefree8=false;
        bsalepricefree9=false;
        bsalepricefree10=false;


    }


}