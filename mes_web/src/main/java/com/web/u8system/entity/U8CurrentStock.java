package com.web.u8system.entity;

import java.math.BigDecimal;

/**
 * 现存量
 * Created by sunyin on 2019/5/5.
 */
public class U8CurrentStock {
    private Integer autoId;
    private String cWhCode;
    private String cInvCode;
    private Integer itemId;
    private Integer iSoType;        //订单类型
    private String iSodid;          //订单ID
    private BigDecimal iQuantity;   //结存数量
    private BigDecimal iNum;   //结存数量
    private BigDecimal fInQuantity; //待入库数量
    private BigDecimal fOutQuantity; //待出库数量
    private BigDecimal fDisableQuantity; //不合格数量
    private BigDecimal fAvaQuantity;//可用数量
    private Boolean bStopFlag;      //库存是否冻结
    private Boolean bGspStop;       //GSP是否冻结 
    private String cBatch;//批号

    private String u8Account;       //账套
    private Integer idun;           //收发记录主表id
    private Integer idsun;          //收发记录子表autoid
    private String cfree1;
    private String cfree2;
    private String cfree3;

    public BigDecimal getiNum() {
        return iNum;
    }

    public void setiNum(BigDecimal iNum) {
        this.iNum = iNum;
    }

    public String getCfree3() {
        return cfree3;
    }

    public void setCfree3(String cfree3) {
        this.cfree3 = cfree3;
    }

    public String getcBatch() {
        return cBatch;
    }

    public void setcBatch(String cBatch) {
        this.cBatch = cBatch;
    }

    public Integer getAutoId() {
        return autoId;
    }

    public void setAutoId(Integer autoId) {
        this.autoId = autoId;
    }

    public String getcWhCode() {
        return cWhCode;
    }

    public void setcWhCode(String cWhCode) {
        this.cWhCode = cWhCode;
    }

    public String getcInvCode() {
        return cInvCode;
    }

    public void setcInvCode(String cInvCode) {
        this.cInvCode = cInvCode;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getiSoType() {
        return iSoType;
    }

    public void setiSoType(Integer iSoType) {
        this.iSoType = iSoType;
    }

    public String getiSodid() {
        return iSodid;
    }

    public void setiSodid(String iSodid) {
        this.iSodid = iSodid;
    }

    public BigDecimal getiQuantity() {
        return iQuantity;
    }

    public void setiQuantity(BigDecimal iQuantity) {
        this.iQuantity = iQuantity;
    }

    public BigDecimal getfInQuantity() {
        return fInQuantity;
    }

    public void setfInQuantity(BigDecimal fInQuantity) {
        this.fInQuantity = fInQuantity;
    }

    public BigDecimal getfOutQuantity() {
        return fOutQuantity;
    }

    public void setfOutQuantity(BigDecimal fOutQuantity) {
        this.fOutQuantity = fOutQuantity;
    }

    public BigDecimal getfDisableQuantity() {
        return fDisableQuantity;
    }

    public void setfDisableQuantity(BigDecimal fDisableQuantity) {
        this.fDisableQuantity = fDisableQuantity;
    }

    public BigDecimal getfAvaQuantity() {
        return fAvaQuantity;
    }

    public void setfAvaQuantity(BigDecimal fAvaQuantity) {
        this.fAvaQuantity = fAvaQuantity;
    }

    public Boolean getbStopFlag() {
        return bStopFlag;
    }

    public void setbStopFlag(Boolean bStopFlag) {
        this.bStopFlag = bStopFlag;
    }

    public Boolean getbGspStop() {
        return bGspStop;
    }

    public void setbGspStop(Boolean bGspStop) {
        this.bGspStop = bGspStop;
    }

    public String getU8Account() {
        return u8Account;
    }

    public void setU8Account(String u8Account) {
        this.u8Account = u8Account;
    }

    public Integer getIdun() {
        return idun;
    }

    public void setIdun(Integer idun) {
        this.idun = idun;
    }

    public Integer getIdsun() {
        return idsun;
    }

    public void setIdsun(Integer idsun) {
        this.idsun = idsun;
    }

    public String getCfree2() {
        return cfree2;
    }

    public void setCfree2(String cfree2) {
        this.cfree2 = cfree2;
    }

    public String getCfree1() {
        return cfree1;
    }

    public void setCfree1(String cfree1) {
        this.cfree1 = cfree1;
    }
}
