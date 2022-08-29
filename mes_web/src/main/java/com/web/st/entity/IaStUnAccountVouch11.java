package com.web.st.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Ia_St_UnAccountVouch11")
public class IaStUnAccountVouch11 {
    private Integer idun;

    private Integer idsun;

    private String cvoutypeun;

    private String cbustypeun;
}