package com.web.om.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.om.entity.OmOrderDetail;
import com.web.om.entity.OmOrderMain;
import com.web.om.mapper.OmOrderDetailMapper;
import com.web.om.mapper.OmOrderMainMapper;
import com.web.om.service.IOmOrderDetailService;
import com.web.om.service.IOmOrderMainService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class OmOrderDetailServiceImpl extends ServiceImpl<OmOrderDetailMapper, OmOrderDetail> implements IOmOrderDetailService {



    @Value("${account.acountId}")
    private String accId;

}
