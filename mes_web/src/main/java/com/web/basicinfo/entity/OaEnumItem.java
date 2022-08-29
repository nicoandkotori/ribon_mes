package com.web.basicinfo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("CTP_ENUM_ITEM")
public class OaEnumItem  extends Model<OaEnumItem> {
    @TableId(value = "ID")
    private Long id;

    private Long refEnumid;

    private String showvalue;

    private String enumvalue;

    private Integer sortnumber;

    private Short state;

    private Short outputSwitch;

    private Long orgAccountId;

    private Long parentId;

    private Long rootId;

    private Short levelNum;

    private String description;

    private String ifuse;

    private Short i18n;

    private Long ext1;

    private String code;
}