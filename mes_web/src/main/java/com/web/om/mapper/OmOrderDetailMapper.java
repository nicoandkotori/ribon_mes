package com.web.om.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.om.entity.OmMoMaterials;
import com.web.om.entity.OmOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OmOrderDetailMapper   extends BaseMapper<OmOrderDetail> {

    /**
     * 根据主键和数据库名称更新数据
     *
     * @param product      产品
     * @param databaseName 数据库名称
     * @return int
     */
    int updateWithDBName(@Param("product")OmOrderDetail product,@Param("database")String databaseName);

    /**
     * 真正的批量插入
     *
     * @param productList 产品列表
     * @return int
     */
    int insertBatch(@Param("productList") List<OmOrderDetail> productList);
}