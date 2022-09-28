package com.web.pd.mapper;

import com.web.om.entity.OmOrderDetail;
import com.web.pd.entity.PdOrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author mijiahao
* @description 针对表【pd_order_detail】的数据库操作Mapper
* @createDate 2022-09-27 08:42:33
* @Entity com.web.pd.entity.PdOrderDetail
*/
public interface PdOrderDetailMapper extends BaseMapper<PdOrderDetail> {
    /**
     * 得到产品表join材料表的记录
     *
     * @param mainId 主表ID
     * @return {@link List}<{@link OmOrderDetail}>
     */
    List<PdOrderDetail> getProductJoinMaterials(String mainId);
}




