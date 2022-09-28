package com.web.pd.mapper;

import com.web.om.entity.OmOrderPart;
import com.web.pd.entity.PdOrderPart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author mijiahao
* @description 针对表【pd_order_part】的数据库操作Mapper
* @createDate 2022-09-27 08:43:23
* @Entity com.web.pd.entity.PdOrderPart
*/
public interface PdOrderPartMapper extends BaseMapper<PdOrderPart> {
    /**
     * 得到部件关联材料的数据
     *
     * @param mainId 主表ID
     * @return {@link List}<{@link OmOrderPart}>
     */
    List<PdOrderPart> getPartsJoinMaterials(String mainId);
}



