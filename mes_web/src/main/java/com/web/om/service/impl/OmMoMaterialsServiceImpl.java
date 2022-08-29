package com.web.om.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.om.entity.OmMoMaterials;
import com.web.om.mapper.OmMoMainMapper;
import com.web.om.mapper.OmMoMaterialsMapper;
import com.web.om.service.IOmMoMaterialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class OmMoMaterialsServiceImpl extends ServiceImpl<OmMoMaterialsMapper, OmMoMaterials> implements IOmMoMaterialsService {


    @Autowired
    private OmMoMainMapper omMoMainMapper;

    @Value("${account.acountId}")
    private String accId;

}
