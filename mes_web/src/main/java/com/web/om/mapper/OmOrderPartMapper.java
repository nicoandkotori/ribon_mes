package com.web.om.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.om.entity.OmOrderDetail;
import com.web.om.entity.OmOrderPart;

import java.util.List;

public interface OmOrderPartMapper  extends BaseMapper<OmOrderPart> {
    /**
     * 得到部件关联材料的数据
     *
     * @param mainId 主表ID
     * @return {@link List}<{@link OmOrderPart}>
     */
    List<OmOrderPart> getPartsJoinMaterials(String mainId);
}