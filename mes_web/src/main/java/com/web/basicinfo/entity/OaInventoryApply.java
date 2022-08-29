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
@TableName("formmain_0096")
public class OaInventoryApply  extends Model<OaInventoryApply> {
    @TableId(value = "ID")
    private Long id;

    private Integer state;

    private String startMemberId;

    private Date startDate;

    private String approveMemberId;

    private Date approveDate;

    private Integer finishedflag;

    private Integer ratifyflag;

    private String ratifyMemberId;

    private Date ratifyDate;

    private Integer sort;

    private String modifyMemberId;

    private Date modifyDate;

    private BigDecimal field0001;

    private String field0002;

    private String field0003;

    private Date field0004;

    private String field0018;

    private Date field0019;

    private String field0020;

    private Date field0021;


    private BigDecimal field0029;
    private BigDecimal field0030;
    private BigDecimal field0031;
    private BigDecimal field0032;


}