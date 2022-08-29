package com.web.basicinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.basicinfo.entity.Inventory;
import com.web.basicinfo.entity.OaInventoryApply;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface IOaInventoryApplyService extends IService<OaInventoryApply> {

    void syncInventoryApplyData()throws Exception;;
}
