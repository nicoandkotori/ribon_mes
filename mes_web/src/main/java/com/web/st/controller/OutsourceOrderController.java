package com.web.st.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.ResponseResult;
import com.common.util.TableResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.st.dto.OutsourceOrderDTO;
import com.web.st.service.IOutsourceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/st/outsourceorder")
public class OutsourceOrderController {

    @Autowired
    private IOutsourceOrderService outsourceOrderService;

    @RequestMapping(value = "/getlistbypage")
    public TableResult<OutsourceOrderDTO> getListByPage(int page, int rows, String queryStr) {
        TableResult<OutsourceOrderDTO> result = new TableResult<>();
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            OutsourceOrderDTO query = JSON.parseObject(queryStr, OutsourceOrderDTO.class);
            if (query == null) {
                query = new OutsourceOrderDTO();
            }
            if (query.getDateEnd() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(query.getDateEnd());
                calendar.add(Calendar.DATE, 1);
                query.setDateEnd(calendar.getTime());
            }

            IPage<OutsourceOrderDTO> page1 = new Page<>(page, rows);
            IPage<OutsourceOrderDTO> resultPage = outsourceOrderService.getList(query, page1);
            result.setRecords(resultPage.getTotal());
            result.setTotal((int)Math.ceil(resultPage.getTotal()/(double)resultPage.getSize()));
            result.setRows(resultPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/getbycode")
    public ResponseResult getByCode(String ccode) {
        ResponseResult result = new ResponseResult();
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            OutsourceOrderDTO m = outsourceOrderService.getByCode(ccode);
            List<OutsourceOrderDTO> list = outsourceOrderService.getListByCode(ccode);
            Map<String, Object> map = new HashMap<>();
            map.put("m", m);
            map.put("list", list);
            result.setResult(map);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            e.printStackTrace();

        }
        return result;
    }
}
