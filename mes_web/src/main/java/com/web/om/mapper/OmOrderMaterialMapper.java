package com.web.om.mapper;

import com.web.om.entity.OmOrderMaterial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author mijiahao
* @description 针对表【om_order_material】的数据库操作Mapper
* @createDate 2022-09-13 16:03:54
* @Entity com.web.om.entity.OmOrderMaterial
*/
public interface OmOrderMaterialMapper extends BaseMapper<OmOrderMaterial> {

    /**
     * 通过主键和数据库名称修改数据
     *
     * @param material 材料
     * @param database 数据库名称
     * @return int
     */
    int updateWithDBName(@Param("material") OmOrderMaterial material,@Param("database") String database);

}




