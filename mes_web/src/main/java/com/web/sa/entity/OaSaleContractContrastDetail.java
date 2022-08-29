package com.web.sa.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("formson_0111")
public class OaSaleContractContrastDetail {
    @TableId(value = "ID")
    private Long id;

    private Long formmainId;

    private Integer sort;

    private String field0016;

    private String field0017;

    private String field0018;

    private String field0019;

    private String field0020;

    private BigDecimal field0021;

    private String field0022;

    private String field0023;

    private String field0024;

    private String field0025;

    private BigDecimal field0026;

    private String field0027;

    private String field0028;

    private String field0029;

    private String field0030;

    private String field0046;

    private String field0063;


//    字段名称	 	字段类型	 	字段长度	 	显示名称	 	字段输入类型	 	字段最终类型
//    field0016	 	VARCHAR	 	255	 	图纸名称	 	文本	 	NVARCHAR(255)
//    field0017	 	VARCHAR	 	100	 	派工存货编码	 	文本	 	NVARCHAR(100)
//    field0018	 	VARCHAR	 	100	 	U8正式编码	 	文本	 	NVARCHAR(100)
//    field0019	 	VARCHAR	 	255	 	U8正式名称	 	文本	 	NVARCHAR(255)
//    field0020	 	VARCHAR	 	255	 	U8正式规格型号	 	文本	 	NVARCHAR(255)
//    field0021	 	DECIMAL	 	20	 	数量	 	文本	 	NUMERIC(20,0)
//    field0022	 	VARCHAR	 	255	 	备注	 	文本	 	NVARCHAR(255)
//    field0023	 	VARCHAR	 	100	 	输送线速度	 	文本	 	NVARCHAR(100)
//    field0024	 	VARCHAR	 	100	 	减速机编码	 	文本	 	NVARCHAR(100)
//    field0025	 	VARCHAR	 	255	 	减速机型号	 	文本	 	NVARCHAR(255)
//    field0026	 	DECIMAL	 	20	 	减速机数量	 	文本	 	NUMERIC(20,0)
//    field0027	 	VARCHAR	 	20	 	部门主管	 	选人	 	NVARCHAR(20)
//    field0028	 	VARCHAR	 	20	 	技术负责人	 	选人	 	NVARCHAR(20)
//    field0029	 	VARCHAR	 	20	 	设计人员	 	选人	 	NVARCHAR(20)
//    field0030	 	VARCHAR	 	20	 	设计人员岗位	 	选岗位	 	NVARCHAR(20)

}