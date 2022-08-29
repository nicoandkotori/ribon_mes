package com.web.ar.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.util.ResponseResult;
import com.web.ar.entity.TestAcc;
import com.web.ar.entity.TestAccList;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface ITestAccListService extends IService<TestAccList> {

    ResponseResult delete() throws Exception;

}
