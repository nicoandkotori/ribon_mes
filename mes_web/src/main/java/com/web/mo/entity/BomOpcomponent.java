package com.web.mo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bom_opcomponent")
public class BomOpcomponent {
    @TableId(value = "OpComponentId")
    private Integer opcomponentid;

    private Integer bomid;

    private Integer sortseq;

    private String opseq;

    private Integer componentid;

    private Date effbegdate;

    private Date effenddate;

    private Byte fvflag;

    private BigDecimal baseqtyn;

    private BigDecimal baseqtyd;

    private Object compscrap;

    private Boolean byproductflag;

    private Integer optionsid;



    private String auxunitcode;

    @TableField(value="changerate",strategy = FieldStrategy.IGNORED)
    private BigDecimal changerate;

    @TableField(value="auxbaseqtyn",strategy = FieldStrategy.IGNORED)
    private Object auxbaseqtyn;

    private Byte producttype;

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

    private String remark;

    private Boolean recursiveflag;

    private String free1;

    private String free2;

    private String free3;

    private String free4;

    private String free5;

    private String free6;

    private String free7;

    private String free8;

    private String free9;

    private String free10;

    private Boolean bprocessproduct;

    private Boolean bprocessmaterial;

    public void setDefValue() throws Exception{
        opseq="0000";
        effbegdate= DateUtil.parseStrToDate("2000-01-01","yyyy-MM-dd");
        effenddate=DateUtil.parseStrToDate("2099-12-31","yyyy-MM-dd");
        fvflag=1;
        byproductflag=false;
        producttype=1;
        recursiveflag=false;
        bprocessmaterial=false;
        bprocessproduct=false;
        free1="";
        free2="";
        free3="";
        free4="";
        free5="";
        free6="";
        free7="";
        free8="";
        free9="";
        free10="";

    }
}