package com.web.pd.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.om.dto.OmOrderMainDTO;
import com.web.pd.dto.PdOrderDetailDTO;
import com.web.pd.dto.PdOrderMainDTO;
import com.web.pd.entity.PdOrderMain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author mijiahao
* @description 针对表【pd_order_main】的数据库操作Mapper
* @createDate 2022-09-27 08:40:57
* @Entity com.web.pd.entity.PdOrderMain
*/
public interface PdOrderMainMapper extends BaseMapper<PdOrderMain> {

    List<PdOrderMainDTO> findMainByPage(IPage<PdOrderMainDTO> page, @Param("main") PdOrderMainDTO mainDTO);

    /**
     * 查询产品表join材料表的数据
     *
     * @param query 查询
     * @return {@link List}<{@link OmOrderMainDTO}>
     * @throws Exception 异常
     */
    List<PdOrderDetailDTO> getMainAndProductList(@Param("main") PdOrderMainDTO query) throws Exception;



}




