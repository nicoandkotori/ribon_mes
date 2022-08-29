package com.web.om.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.om.entity.OmMoDetails;
import com.web.om.mapper.OmMoDetailsMapper;
import com.web.om.mapper.OmMoMainMapper;
import com.web.om.service.IOmMoDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class OmMoDetailsServiceImpl extends ServiceImpl<OmMoDetailsMapper, OmMoDetails> implements IOmMoDetailsService {


    @Autowired
    private OmMoMainMapper omMoMainMapper;

    @Value("${account.acountId}")
    private String accId;

}
