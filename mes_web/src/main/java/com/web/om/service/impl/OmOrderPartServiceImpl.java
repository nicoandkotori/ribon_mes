package com.web.om.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.om.entity.OmOrderDetail;
import com.web.om.entity.OmOrderPart;
import com.web.om.mapper.OmOrderDetailMapper;
import com.web.om.mapper.OmOrderPartMapper;
import com.web.om.service.IOmOrderDetailService;
import com.web.om.service.IOmOrderPartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class OmOrderPartServiceImpl extends ServiceImpl<OmOrderPartMapper, OmOrderPart> implements IOmOrderPartService {



    @Value("${account.acountId}")
    private String accId;

}
