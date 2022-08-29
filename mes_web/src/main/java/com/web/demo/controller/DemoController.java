package com.web.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.CustomStringUtils;
import com.common.util.LayTableResult;
import com.web.common.controller.BasicController;
import com.web.demo.entity.OrderWorkType;
import com.web.demo.service.IOrderWorkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lenovo on 2021/8/12.
 */
@RestController
@RequestMapping("/demo")
public class DemoController extends BasicController {

    @Autowired
    private IOrderWorkTypeService orderWorkTypeService;

    /**
     * 获取厂班类型（下拉表格）
     */
    @RequestMapping(value = "/findlworktypelist")
    @ResponseBody
    public LayTableResult<OrderWorkType> findWorkTypeList(int page, int limit, String keyword) {
        LayTableResult<OrderWorkType> result = new LayTableResult<>();
        try {
            IPage<OrderWorkType> page1 = new Page<>(page, limit);

            LambdaQueryWrapper<OrderWorkType> select = new LambdaQueryWrapper<>();
            select.eq(OrderWorkType::getIzDelete, 0);

            if (CustomStringUtils.isNotBlank(keyword)) {
                select.like(OrderWorkType::getTypeClass, keyword)
                        .or().like(OrderWorkType::getTypeName, keyword);
            }
            select.orderByAsc(OrderWorkType::getTypeClass, OrderWorkType::getTypeName);
            IPage<OrderWorkType> pg = orderWorkTypeService.page(page1, select);

            result.setCount(pg.getTotal());
            result.setData(pg.getRecords());
        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("加载数据出错！");
            e.printStackTrace();
        }
        return result;
    }
}
