package com.web.mo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.web.mo.entity.BomOpcomponent;
import lombok.Data;
import lombok.ToString;

/**
 * BOM子件DTO
 *
 * @author mijiahao
 * @date 2022/09/23
 */

@Data
@ToString
public class BomOpcomponentDTO extends BomOpcomponent {

    @TableField("InvCode")
    private String InvCode ;
}
