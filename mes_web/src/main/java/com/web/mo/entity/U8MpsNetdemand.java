package com.web.mo.entity;

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
@TableName("mps_netdemand")
public class U8MpsNetdemand {
    @TableId(value = "demandId")
    private Integer demandid;

    private Integer partid;

    private Byte sotype;

    private String sodid;

    private String socode;

    private Integer soseq;

    private String plancode;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date duedate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startdate;

    private Date lusd;

    private Date lucd;

    private BigDecimal planqty;

    private Object crdqty;

    private Byte supplytype;

    private Integer schid;

    private Boolean manualflag;

    private Boolean delflag;

    private Boolean modifyflag;

    private Date ufts;

    private Integer projectid;

    private Byte status;

    private Date firmdate;

    private String firmuser;

    private Byte srpsotype;

    private Integer srpsodid;

    private Integer soid;

    private String demandcode;

    private String supplyingrcode;

    private String supplyingpcode;

    private Byte ordertype;

    private Integer orderdid;

    private String define22;

    private String define23;

    private String define24;

    private String define25;

    private Double define26;

    private Double define27;

    private String define28;

    private String define29;

    private String define30;

    private String define31;

    private String define32;

    private String define33;

    private Integer define34;

    private Integer define35;

    private Date define36;

    private Date define37;

    private Object procqty;

    private Object procedqty;

    private Integer copydemandid;

    private Object orgplanqty;

    private Object orgdemqty;

    private Date orgstartdate;

    private Date orgduedate;

    private Byte orgdemtype;

    private Integer orgdemdid;

    private String orgdemcode;

    private Integer orgdemseq;

    private Object orgdocdemqty;

    private Date closedate;

    private Date closetime;

    private String closeuser;

    private Byte orgstatus;

    private String factorycode;

    private String puremplcode;


    @TableField(exist = false)
    private Date dateStart;
    @TableField(exist = false)
    private Date dateEnd;
    @TableField(exist = false)
    private String cinvcode;
    @TableField(exist = false)
    private String cinvname;
    @TableField(exist = false)
    private String cinvstd;
    @TableField(exist = false)
    private String ccomunitname;
    @TableField(exist = false)
    private String csocode;
    @TableField(exist = false)
    private String cmemo;
    @TableField(exist = false)
    private String ccusname;
    @TableField(exist = false)
    private String ccusabbname;
    @TableField(exist = false)
    private String cdefine1;



}