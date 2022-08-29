package com.web.om.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.web.om.dto.OmOrderMainDTO;
import com.web.om.dto.OmProductVM;
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
}
