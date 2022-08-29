package com.web.basicinfo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.basicinfo.entity.Vendor;
import com.web.basicinfo.entity.Warehouse;
import com.web.basicinfo.mapper.VendorMapper;
import com.web.basicinfo.mapper.WarehouseMapper;
import com.web.basicinfo.service.IVendorService;
import com.web.basicinfo.service.IWarehouseService;
import org.springframework.stereotype.Service;

/**
 * Created by cai on 2021-3-12.
 */
@Service
public class VendorServiceImpl extends ServiceImpl<VendorMapper, Vendor> implements IVendorService {

}
