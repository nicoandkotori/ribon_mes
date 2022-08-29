package com.web.mo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bom_parent")
public class BomParent {
    @TableId(value = "AutoID")
    private String autoid;

    private Integer bomid;

    private Integer parentid;

    private Object parentscrap;

    private Integer sharingpartid;


}