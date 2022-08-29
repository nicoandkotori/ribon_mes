package com.web.st.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.ResponseResult;
import com.common.util.TableResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.st.dto.DeviceListDTO;
import com.web.st.service.IDeviceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping(value = "/st/devicelist")
public class DeviceListController {

    @Autowired
    private IDeviceListService deviceListService;

    @RequestMapping(value = "/getlistbypage")
    public TableResult<DeviceListDTO> getListByPage(int page, int rows, String queryStr) {
        TableResult<DeviceListDTO> result = new TableResult<>();
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            DeviceListDTO query = JSON.parseObject(queryStr, DeviceListDTO.class);
            if (query == null) {
                query = new DeviceListDTO();
            }
            if (query.getDateEnd() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(query.getDateEnd());
                calendar.add(Calendar.DATE, 1);
                query.setDateEnd(calendar.getTime());
            }

            IPage<DeviceListDTO> page1 = new Page<>(page, rows);
            IPage<DeviceListDTO> resultPage = deviceListService.getList(query, page1);
            result.setRecords(resultPage.getTotal());
            result.setTotal((int)Math.ceil(resultPage.getTotal()/(double)resultPage.getSize()));
            result.setRows(resultPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/getbyorderno")
    public ResponseResult getByOrderNo(String orderNo, String proType) {
        ResponseResult result = new ResponseResult();
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            List<DeviceListDTO> list = deviceListService.getListByOrderNo(orderNo, proType);
            result.setResult(list);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
