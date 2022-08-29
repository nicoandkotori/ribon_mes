package com.web.mo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bom_opcomponentopt")
public class BomOpcomponentopt {
    @TableId(value = "OptionsId")
    private Integer optionsid;
    @TableField("[Offset]")
    private Integer offset;

    private Integer wiptype;

    private Boolean accucostflag;

    private String drawdeptcode;

    private String whcode;

    private Boolean optionalflag;

    private Byte mutexrule;

    private Object planfactor;



    private Boolean costwiprel;

    private Boolean featurerel;

    public void setDefValue() throws Exception{
        accucostflag=true;
        optionalflag=false;
        mutexrule=2;
        planfactor= BigDecimal.valueOf(100);
        costwiprel=false;
        featurerel=false;


    }
}