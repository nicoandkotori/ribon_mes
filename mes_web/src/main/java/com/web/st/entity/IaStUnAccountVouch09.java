package com.web.st.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("IA_ST_UnAccountVouch09")
public class IaStUnAccountVouch09 {
    private Long idun;

    private Long idsun;

    private String cvoutypeun;

    private String cbustypeun;
}