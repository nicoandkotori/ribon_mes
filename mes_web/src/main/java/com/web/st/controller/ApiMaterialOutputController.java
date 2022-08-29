package com.web.st.controller;

import com.alibaba.fastjson.JSON;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.util.ResponseResultPDA;
import com.web.st.dto.DeviceListDTO;
import com.web.st.service.IDeviceListService;
import com.web.st.service.IMaterialOutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/st/materialoutput")
public class ApiMaterialOutputController {

    @Autowired
    private IMaterialOutputService materialOutputService;

    @Autowired
    private IDeviceListService deviceListService;

    @RequestMapping(value = "/getmainbyid")
    public ResponseResultPDA getMainById(String barcode) {
        ResponseResultPDA resultPDA = new ResponseResultPDA();
        try {
            String proType;
            if (barcode.startsWith("J-")) {
                proType = "金工";
            } else {
                proType = "外购";
            }

            DbContextHolder.setDbType(DBTypeEnum.db2);
            barcode = barcode.substring(2);
            DeviceListDTO m = deviceListService.getByCode(barcode, proType);

            List<DeviceListDTO> resList = new ArrayList<>();
            List<DeviceListDTO> list = deviceListService.getListByOrderNo(m.getOrderNo(), proType);
            for (DeviceListDTO d : list) {
                if (d.getUnOutQty().compareTo(BigDecimal.ZERO) > 0) {
                    d.setOutQty(null);
                    resList.add(d);
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("main", m);
            map.put("list", resList);
            resultPDA.setResult(map);
        } catch (Exception e) {
            resultPDA.setSuccess(false);
            resultPDA.setErrormsg(e.getMessage());
            e.printStackTrace();
        }
        return resultPDA;
    }

    @RequestMapping(value = "/getdetail")
    public ResponseResultPDA getDetail(String barcode, String orderNo) {
        ResponseResultPDA resultPDA = new ResponseResultPDA();
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            int index = barcode.indexOf("-");
            barcode = barcode.substring(index + 1);
            DeviceListDTO m = deviceListService.getDetail(orderNo, barcode);
            resultPDA.setResult(m);
        } catch (Exception e) {
            resultPDA.setSuccess(false);
            resultPDA.setErrormsg(e.getMessage());
            e.printStackTrace();
        }
        return resultPDA;
    }

    @RequestMapping(value = "/save")
    public ResponseResultPDA save(String mData, String mDatas, String userName) {
        ResponseResultPDA resultPDA = new ResponseResultPDA();
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            DeviceListDTO m = JSON.parseObject(mData, DeviceListDTO.class);
            List<DeviceListDTO> list = JSON.parseArray(mDatas, DeviceListDTO.class);
            materialOutputService.save(m, list, userName);
        } catch (Exception e) {
            resultPDA.setSuccess(false);
            resultPDA.setErrormsg(e.getMessage());
            e.printStackTrace();
        }
        return resultPDA;
    }
}
