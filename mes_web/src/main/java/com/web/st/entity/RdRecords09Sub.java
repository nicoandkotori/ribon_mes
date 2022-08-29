package com.web.st.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rdrecords09sub")
public class RdRecords09Sub {
    @TableId(value = "AutoID")
    private Long autoid;

    private Long id;

    private String cbgItemcode;

    private String cbgItemname;

    private String cbgCaliberkey1;

    private String cbgCaliberkeyname1;

    private String cbgCaliberkey2;

    private String cbgCaliberkeyname2;

    private String cbgCaliberkey3;

    private String cbgCaliberkeyname3;

    private String cbgCalibercode1;

    private String cbgCalibername1;

    private String cbgCalibercode2;

    private String cbgCalibername2;

    private String cbgCalibercode3;

    private String cbgCalibername3;

    private Boolean ibgCtrl;

    private String cbgAuditopinion;

    private Object ibgstsum;

    private Object ibgiasum;

    private String cbgCaliberkey4;

    private String cbgCaliberkeyname4;

    private String cbgCaliberkey5;

    private String cbgCaliberkeyname5;

    private String cbgCaliberkey6;

    private String cbgCaliberkeyname6;

    private String cbgCalibercode4;

    private String cbgCalibername4;

    private String cbgCalibercode5;

    private String cbgCalibername5;

    private String cbgCalibercode6;

    private String cbgCalibername6;
}