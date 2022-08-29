package com.web.st.controller;

import com.alibaba.fastjson.JSON;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.util.ResponseResultPDA;
import com.web.st.dto.OutsourceOrderDTO;
import com.web.st.service.IOutsourceInputService;
import com.web.st.service.IOutsourceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/st/outsourceinput")
public class ApiOutsourceInputController {

    @Autowired
    private IOutsourceInputService outsourceInputService;

    @Autowired
    private IOutsourceOrderService outsourceOrderService;

    @RequestMapping(value = "/getmainbyid")
    public ResponseResultPDA getMainById(String barcode) {
        ResponseResultPDA resultPDA = new ResponseResultPDA();
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            OutsourceOrderDTO m = outsourceOrderService.getByCode(barcode);

            List<OutsourceOrderDTO> resList = new ArrayList();
            List<OutsourceOrderDTO> list = outsourceOrderService.getListByCode(barcode);
            for (OutsourceOrderDTO d : list) {
                if (d.getUninqty().compareTo(BigDecimal.ZERO) > 0) {
                    d.setIquantity(null);
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
    public ResponseResultPDA getDetail(String barcode, String ccode) {
        ResponseResultPDA resultPDA = new ResponseResultPDA();
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            int index = barcode.indexOf("-");
            barcode = barcode.substring(index + 1);
            OutsourceOrderDTO m = outsourceOrderService.getDetail(ccode, barcode);
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
            OutsourceOrderDTO m = JSON.parseObject(mData, OutsourceOrderDTO.class);
            List<OutsourceOrderDTO> list = JSON.parseArray(mDatas, OutsourceOrderDTO.class);
            outsourceInputService.save(m, list, userName);
        } catch (Exception e) {
            resultPDA.setSuccess(false);
            resultPDA.setErrormsg(e.getMessage());
            e.printStackTrace();
        }
        return resultPDA;
    }
}
