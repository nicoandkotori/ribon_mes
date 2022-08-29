package com.web.mo.dto;

import lombok.Data;

import java.math.BigDecimal;

//查询显示
@Data
public class BomDTO {

    private String id;
    private String parentId;
    private Boolean isleaf;
    private Boolean loaded;
    private Boolean expanded;
    private Integer menulevel;

    private Integer bomId;
    private Integer parentBomId;
    private Integer partId;
    private Integer bomtype;


    private Integer version;
    private String versiondesc;

    private String childInvCode;

    private String parentInvCode;
    private String parentInvName;
    private String parentInvStd;

    private String invCode;
    private String invName;
    private String invStd;
    private String invAddCode;
    private Integer sortseq;

    private String custCode;
    private String custName;


    private BigDecimal baseQtyN;
    private BigDecimal baseQtyD;

    private String status;
    private String itype;

    private BigDecimal qty;
    private Integer sodId;
    private String soCode;
    private Integer rowno;
    private Integer opComponentId;
    private Integer componentId;

    private BigDecimal compScrap;
    private Integer wipType;
    private Integer offset;
//    @TableField(exist = false)
    private String wipTypeName;

    //    @TableField(exist = false)
    private String statusId;

    private Integer colorState;
    private String pmc;
    private String remark;
    private String invMemo;
    private String invType;
    private String tagCode;

    private String cinvdefine1;
    private String cinvdefine2;
    private String cinvdefine3;
//    private String cinvdefine4;
//    private String cinvdefine5;
//    private String cinvdefine6;
    private String ccomunitname;

//    private BigDecimal auxbaseqtyn;
//    private String personName;
    private Integer sortRow;

    private String cwhname;
    private String cwhcode;
    private String define22;
    private String define23;
    private String define24;
    private String define25;
    private String define26;
    private String define28;
    private String define29;
    private String define30;
    private String define31;
    private String define32;
    private String define33;
    private String define35;


    private Integer izAdd;
    private BigDecimal changerate;//总用量

    private BigDecimal totalQty;//总用量
    private BigDecimal childTotalQty;//下级总用量
    private BigDecimal childQtys;//下级总用量
    private BigDecimal yqty;//已用数量
    private String develop;

    private BigDecimal tdqtyd;//母件用量
    private String cfree1;
}