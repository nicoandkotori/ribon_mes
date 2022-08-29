package com.web.basicinfo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.common.util.R;
import com.web.basicinfo.entity.Inventory;
import com.web.mo.dto.BomDTO;
import com.web.mo.entity.TestSoList;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface IInventoryService extends IService<Inventory> {
    Inventory getInfoById(String invCode) throws Exception;
    List<Inventory> getMenuList() throws Exception;

    IPage<Inventory> getList(Inventory query, IPage<Inventory> page) throws Exception;

    BigDecimal getPrice(String invCode) throws Exception;

    R saveImport(List<Inventory> list);

}
