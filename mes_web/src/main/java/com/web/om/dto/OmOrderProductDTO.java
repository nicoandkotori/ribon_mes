package com.web.om.dto;

import com.web.om.entity.OmOrderDetail;

/**
 * 委外订单产品表dto
 *
 * @author mijiahao
 * @date 2022/09/13
 */
public class OmOrderProductDTO extends OmOrderDetail {

    private String recordId ;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }



}
