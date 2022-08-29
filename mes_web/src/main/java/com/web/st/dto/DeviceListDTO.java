package com.web.st.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DeviceListDTO {
    private String id;
    private String orderNo;
    private String fromNo;
    private String cmemo;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdate;
    private String conNo;

    private String invCode;
    private String invName;
    private String invStd;
    private String unit;
    private String matSize;
    private String proType;
    private BigDecimal orderQty;
    private BigDecimal outQty;
    private BigDecimal unOutQty;
    private BigDecimal curQty;

    private Integer iid;
    private String barcodeId;

    private String whCode;
    private String depCode;

    private Date dateStart;
    private Date dateEnd;
    private String invType;
}
