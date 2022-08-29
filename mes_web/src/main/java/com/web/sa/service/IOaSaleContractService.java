package com.web.sa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.util.ResponseResult;
import com.web.basicinfo.entity.InventorySub;
import com.web.event.content.SaleOrderContent;
import com.web.sa.entity.OaSaleContract;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface IOaSaleContractService extends IService<OaSaleContract> {
    void syncSaleContractData()throws Exception;
    void syncSaleContractDataEdit()throws Exception;
    void syncSaleContractDataDelete()throws Exception;


}
