package com.web.mo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.common.util.ParamUtil;
import com.common.util.ResponseResult;
import com.web.mo.entity.TestSoList;
import com.web.mo.entity.U8MpsNetdemand;
import com.web.mo.mapper.TestSoListMapper;
import com.web.mo.mapper.TestSoMapper;
import com.web.mo.mapper.U8MpsNetdemandMapper;
import com.web.mo.service.IMpsNetdemandService;
import com.web.mo.service.ITestSoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class MpsNetdemandServiceImpl extends ServiceImpl<U8MpsNetdemandMapper, U8MpsNetdemand> implements IMpsNetdemandService {

    @Autowired
    private U8MpsNetdemandMapper u8MpsNetdemandMapper;

    @Override
    public IPage<U8MpsNetdemand> getList(U8MpsNetdemand mainDTO, IPage<U8MpsNetdemand> page) throws Exception {
        page.setRecords(u8MpsNetdemandMapper.getList(page,mainDTO));
        return page;
    }
    @Override
    public List<U8MpsNetdemand> getPrintProduct(U8MpsNetdemand query)throws Exception {
        return u8MpsNetdemandMapper.getPrintProduct(query);
    }


    @Override
    public IPage<U8MpsNetdemand> getListForPrint(U8MpsNetdemand mainDTO, IPage<U8MpsNetdemand> page) throws Exception {
        page.setRecords(u8MpsNetdemandMapper.getListForPrint(page,mainDTO, ParamUtil.getParam("localDatabase").toString()));
        return page;
    }

}
