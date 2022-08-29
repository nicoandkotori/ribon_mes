package com.web.basicinfo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.basicinfo.entity.ComputationUnit;
import com.web.basicinfo.entity.InventoryClass;
import com.web.basicinfo.mapper.ComputationUnitMapper;
import com.web.basicinfo.mapper.InventoryClassMapper;
import com.web.basicinfo.service.IComputationUnitService;
import com.web.basicinfo.service.IInventoryClassService;
import org.springframework.stereotype.Service;


/**
 *
 */
@Service
public class ComputationUnitServiceImpl extends ServiceImpl<ComputationUnitMapper, ComputationUnit> implements IComputationUnitService {

}
