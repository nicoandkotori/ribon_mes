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

    private String partRowId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getPartRowId() {
        return partRowId;
    }

    public void setPartRowId(String partRowId) {
        this.partRowId = partRowId;
    }
}
