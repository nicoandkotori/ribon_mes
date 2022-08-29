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
@TableName("Person")
public class Person {
    @TableId(value = "cpersoncode")
    private String cpersoncode;

    private String cpersonname;

    private String cdepcode;

    private String cpersonprop;

    private Double fcreditquantity;

    private Integer icredate;

    private String ccregrade;

    private Double ilowrate;

    private String coffergrade;

    private Double iofferrate;



    private String cpersonemail;

    private String cpersonphone;

    private Date dpvaliddate;

    private Date dpinvaliddate;
}