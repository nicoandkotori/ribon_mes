package com.web.basicinfo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ComputationGroup")
public class ComputationGroup {
    @TableId(value = "cGroupCode")
    private String cgroupcode;

    private String cgroupname;

    private Byte igrouptype;

//    private Date pubufts;

    private String cgrprelinvcode;

    private Boolean bdefaultgroup;
}