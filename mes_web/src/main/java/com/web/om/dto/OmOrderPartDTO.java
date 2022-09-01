package com.web.om.dto;

import com.web.om.entity.OmOrderPart;

/**
 * 部件表DTO
 *
 * @author mijiahao
 * @date 2022/08/31
 */
public class OmOrderPartDTO extends OmOrderPart {

    private String recordId ;

    private String partRecordId ;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getPartRecordId() {
        return partRecordId;
    }

    public void setPartRecordId(String partRecordId) {
        this.partRecordId = partRecordId;
    }
}
