package com.web.om.dto;

/**
 * 材料表DTO
 *
 * @author mijiahao
 * @date 2022/08/31
 */
public class OmOrderMaterialDTO {

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
