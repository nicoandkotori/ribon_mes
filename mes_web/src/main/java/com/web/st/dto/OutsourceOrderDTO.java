package com.web.st.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OutsourceOrderDTO {
    private Integer moid;
    private Integer modetailsid;
    private String ccode;
    private String cdefine2;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ddate;
    private String cvenabbname;
    private String cdepcode;
    private String cdepname;
    private String cinvcode;
    private String cinvname;
    private String cinvstd;
    private BigDecimal iquantity;
    private BigDecimal inqty;
    private BigDecimal uninqty;
    private BigDecimal curqty;
    private String cmemo;
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date darrivedate;
    private String cpersonname;

    private String iid;
    private String barcodeId;

    private String whCode;
    private String whName;


    private Date dateStart;
    private Date dateEnd;
}
