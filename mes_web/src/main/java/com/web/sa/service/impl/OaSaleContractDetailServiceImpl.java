package com.web.sa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.sa.entity.OaSaleContract;
import com.web.sa.entity.OaSaleContractDetail;
import com.web.sa.mapper.OaSaleContractDetailMapper;
import com.web.sa.mapper.OaSaleContractMapper;
import com.web.sa.service.IOaSaleContractDetailService;
import com.web.sa.service.IOaSaleContractService;
import org.springframework.stereotype.Service;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class OaSaleContractDetailServiceImpl extends ServiceImpl<OaSaleContractDetailMapper, OaSaleContractDetail> implements IOaSaleContractDetailService {

}
