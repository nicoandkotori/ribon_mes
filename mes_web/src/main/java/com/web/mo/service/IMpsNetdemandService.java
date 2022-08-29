package com.web.mo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.common.util.ResponseResult;
import com.web.mo.entity.TestSo;
import com.web.mo.entity.U8MpsNetdemand;

import java.util.List;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface IMpsNetdemandService extends IService<U8MpsNetdemand> {
    IPage<U8MpsNetdemand> getList(U8MpsNetdemand query, IPage<U8MpsNetdemand> page) throws Exception;
    List<U8MpsNetdemand> getPrintProduct(U8MpsNetdemand query) throws Exception;
    IPage<U8MpsNetdemand> getListForPrint(U8MpsNetdemand query, IPage<U8MpsNetdemand> page) throws Exception;



}
