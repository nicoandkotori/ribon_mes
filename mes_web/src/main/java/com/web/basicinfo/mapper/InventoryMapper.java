package com.web.basicinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.basicinfo.entity.Inventory;
import com.web.basicinfo.entity.InventorySub;
import com.web.mo.dto.BomDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryMapper  extends BaseMapper<Inventory> {
    List<Inventory> getList(IPage<Inventory> page, @Param("main") Inventory mainDTO);


    Inventory getInfoById(String cinvcode);
    Inventory getPrice(String cinvcode);
    Inventory getPriceFirst(String cinvcode);

    List<Inventory> selectParentMenuList();
    List<Inventory> selectByMenu(String parentcinvccode);
}