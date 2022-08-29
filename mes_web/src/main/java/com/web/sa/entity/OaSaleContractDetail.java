package com.web.sa.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("formson_0131")
public class OaSaleContractDetail extends Model<OaSaleContractDetail> {
    @TableId(value = "ID")
    private Long id;

    private Long formmainId;

    private Integer sort;

    private BigDecimal field0007;

    private BigDecimal field0010;

    private BigDecimal field0011;

    private BigDecimal field0012;

    private String field0008;

    private String field0009;

    private String field0013;

    private String field0123;
    private String field0103;
//    field0007	 	DECIMAL	 	20	 	序号	 	序号	 	NUMERIC(20,0)
//    field0008	 	VARCHAR	 	255	 	产品名称	 	文本	 	NVARCHAR(255)
//    field0009	 	VARCHAR	 	255	 	型号	 	文本	 	NVARCHAR(255)
//    field0010	 	DECIMAL	 	20	 	数量	 	文本	 	NUMERIC(20,0)
//    field0011	 	DECIMAL	 	20	 	含税单价	 	文本	 	NUMERIC(20,2)
//    field0012	 	DECIMAL	 	20	 	含税总价	 	文本	 	NUMERIC(20,2)
//    field0013	 	VARCHAR	 	255	 	备注	 	文本	 	NVARCHAR(255)


}