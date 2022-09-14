package com.web.om.dto;

import com.web.om.entity.OmOrderMaterial;

/**
 * 材料表DTO
 *
 * @author mijiahao
 * @date 2022/08/31
 */
public class OmOrderMaterialDTO extends OmOrderMaterial {

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
