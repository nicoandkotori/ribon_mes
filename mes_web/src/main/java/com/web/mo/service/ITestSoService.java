package com.web.mo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.util.ResponseResult;
import com.web.mo.entity.TestSo;
import com.web.mo.entity.TestSoList;
import com.web.mo.entity.U8MpsNetdemand;

import java.util.List;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface ITestSoService extends IService<TestSo> {

    ResponseResult calculate(List<U8MpsNetdemand> list) throws Exception;
}
