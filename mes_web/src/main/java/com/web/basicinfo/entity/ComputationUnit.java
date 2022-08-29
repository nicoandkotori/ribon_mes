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
@TableName("ComputationUnit")
public class ComputationUnit  extends Model<ComputationUnit> {
    @TableId(value = "cComunitCode")
    private String ccomunitcode;

    private String ccomunitname;

    private String cgroupcode;

    private String cbarcode;

    private Boolean bmainunit;

    private BigDecimal ichangrate;

    private Double iproportion;

    private Short inumber;

//    private Date pubufts;

    private String censingular;

    private String cenplural;

    private String cunitrefinvcode;
}