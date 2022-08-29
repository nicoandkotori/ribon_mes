package com.web.basicinfo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.basicinfo.entity.Customer;
import com.web.basicinfo.entity.Inventory;
import com.web.basicinfo.mapper.CustomerMapper;
import com.web.basicinfo.mapper.InventoryMapper;
import com.web.basicinfo.service.ICustomerService;
import com.web.basicinfo.service.IInventoryService;
import org.springframework.stereotype.Service;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
