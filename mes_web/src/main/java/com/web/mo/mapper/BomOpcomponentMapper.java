package com.web.mo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.mo.dto.BomOpcomponentDTO;
import com.web.mo.entity.BomOpcomponent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BomOpcomponentMapper  extends BaseMapper<BomOpcomponent> {


    /**
     * 通过母件的产品编码查询其下所有子件，返回的子件带上子件的产品编码
     *
     * @param invCode 发票代码
     * @return {@link List}<{@link BomOpcomponentDTO}>
     */
    List<BomOpcomponentDTO> getComponentWithParentInvCode(@Param("InvCode") String invCode);
}