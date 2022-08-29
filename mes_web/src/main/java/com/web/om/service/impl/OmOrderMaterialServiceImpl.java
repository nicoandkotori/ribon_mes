package com.web.om.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.om.entity.OmOrderMaterial;
import com.web.om.entity.OmOrderPart;
import com.web.om.mapper.OmOrderMaterialMapper;
import com.web.om.mapper.OmOrderPartMapper;
import com.web.om.service.IOmOrderMainService;
import com.web.om.service.IOmOrderMaterialService;
import com.web.om.service.IOmOrderPartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class OmOrderMaterialServiceImpl extends ServiceImpl<OmOrderMaterialMapper, OmOrderMaterial> implements IOmOrderMaterialService {



    @Value("${account.acountId}")
    private String accId;

}
