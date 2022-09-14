package com.web.om.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.common.util.ResponseResult;
import com.web.om.dto.*;
import com.web.om.entity.OmMoMaterials;
import com.web.om.entity.OmOrderMain;

import java.util.List;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface IOmOrderMainService extends IService<OmOrderMain> {

    IPage<OmOrderMainDTO> getMainList(OmOrderMainDTO query, IPage<OmOrderMainDTO> page) throws Exception;

    List<OmOrderMainDTO> getDetailList(OmOrderMainDTO query) throws Exception;

    /**
     * 保存委外订单到mes数据库
     *
     * @return {@link ResponseResult}
     */
    ResponseResult saveToMes(OmOrderMain main,
                             List<OmOrderProductDTO> productList,
                             List<OmOrderPartDTO> partList,
                             List<OmOrderMaterialDTO> materialList);
}
