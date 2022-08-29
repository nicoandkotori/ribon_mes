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
@TableName("Department")
public class Department {
    @TableId(value = "cdepcode")
    private String cdepcode;

    private Boolean bdepend;

    private String cdepname;

    private Byte idepgrade;

    private String cdepperson;

    private String cdepprop;

    private String cdepphone;

    private String cdepaddress;

    private String cdepmemo;

    private Double icreline;

    private String ccregrade;

    private Integer icredate;

    private String coffergrade;

    private Double iofferrate;


    private Boolean bshop;

    private Object cdepguid;

    private Date ddepbegindate;

    private Date ddependdate;

    private String vauthorizedoc;

    private String vauthorizeunit;

    private String cdepfax;

    private String cdeppostcode;

    private String cdepemail;

    private String cdeptype;

    private Integer binheritdutybasic;

    private Integer binheritworkcalendar;

    private String cdutycode;

    private String crestcode;

    private Boolean bim;

    private String cdepnameen;

    private Boolean bretail;

    private String cdepfullname;

    private Integer ideporder;

    private String cdepleader;

    private Date dmodifydate;

    private String cespacemembid;
}