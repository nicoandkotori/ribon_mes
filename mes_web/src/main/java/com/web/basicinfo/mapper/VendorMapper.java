package com.web.basicinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.basicinfo.entity.Vendor;
import com.web.basicinfo.entity.Warehouse;

public interface VendorMapper  extends BaseMapper<Vendor> {
    int updateByPrimaryKeySelective(Vendor record);

    int updateByPrimaryKey(Vendor record);
}