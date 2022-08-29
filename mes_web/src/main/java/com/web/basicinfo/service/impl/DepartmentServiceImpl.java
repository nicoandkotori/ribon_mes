package com.web.basicinfo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.basicinfo.entity.Department;
import com.web.basicinfo.entity.Vendor;
import com.web.basicinfo.mapper.DepartmentMapper;
import com.web.basicinfo.mapper.VendorMapper;
import com.web.basicinfo.service.IDepartmentService;
import com.web.basicinfo.service.IVendorService;
import org.springframework.stereotype.Service;

/**
 * Created by cai on 2021-3-12.
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
