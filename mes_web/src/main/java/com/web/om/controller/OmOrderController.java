package com.web.om.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.CustomStringUtils;
import com.common.util.DateUtil;
import com.common.util.ResponseResult;
import com.common.util.TableResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.basicinfo.entity.Vendor;
import com.web.basicinfo.service.IVendorService;
import com.web.common.controller.BasicController;
import com.web.om.dto.*;
import com.web.om.entity.OmMoMain;
import com.web.om.entity.OmOrderMain;
import com.web.om.service.IOmMoMainService;
import com.web.om.service.IOmOrderMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * Created by caihuan on 2021-04-13.
 */
@RestController
@RequestMapping(value = "/om/order")
public class OmOrderController extends BasicController {

    @Autowired
    private IOmOrderMainService omMainService;

    @Autowired
    private IVendorService vendorService;

    /**
     * 查询最大的单据号
     */
    @RequestMapping(value = "/getmaxcode")
    public ResponseResult getMaxCode() throws Exception {
        ResponseResult result = new ResponseResult();
        try{
            String head = "OM" + DateUtil.getDateStr(new Date(), "yyyyMMdd");
            String sortNum = "001";
            int sortNumLength = 3;

            LambdaQueryWrapper<OmOrderMain> select=new LambdaQueryWrapper<>();
            select.select(OmOrderMain::getVouchCode)
                    .like(OmOrderMain::getVouchCode, head);
            select.orderByDesc(OmOrderMain::getVouchCode);
            List<OmOrderMain> mainList = omMainService.list(select);
            if(mainList.size() > 0){
                String maxCode = mainList.get(0).getVenCode();
                if (CustomStringUtils.isNotBlank(maxCode)) {
                    sortNum = Integer.parseInt(maxCode.substring(maxCode.length() - sortNumLength)) + 1 + "";
                    while (sortNum.length() < sortNumLength) {
                        sortNum = "0" + sortNum;
                    }
                }
            }
            result.setResult( head + sortNum);
            result.setSuccess(true);
        }catch(Exception e){

            e.printStackTrace();
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;

    }

    /**
     * 委外单列表分页数据
     * @param page 当前页
     * @param rows 每页数据条数
     * @param querystr 查询参数
     * @return
     */
    @RequestMapping(value = "/getmainlistbypage")
    public TableResult<OmOrderMainDTO> findPage(Integer page, Integer rows, String querystr) {
        TableResult<OmOrderMainDTO> result = new TableResult<>();
        IPage<OmOrderMainDTO> page1 = new Page<>(page, rows);
        IPage<OmOrderMainDTO> resultPage = new Page<>(page, rows);
        try{
            OmOrderMainDTO data = JSON.parseObject(querystr, OmOrderMainDTO.class);
            if(data == null){
                data = new OmOrderMainDTO();
            }
            resultPage =  omMainService.getMainList(data,page1);

        }catch(Exception e){

            e.printStackTrace();
        }
        result.setRecords(resultPage.getTotal());
        result.setTotal((int)Math.ceil(resultPage.getTotal()/(double)resultPage.getSize()));
        result.setRows(resultPage.getRecords());
        return result;

    }


    /**
     * 根据id查询委外订单主表
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @GetMapping(value = "getbyid")
    public ResponseResult getById(Integer id) {
        try {
            if (CustomStringUtils.isBlank(id)) {
                throw new Exception("参数异常");
            }
            OmOrderMain main =omMainService.getById(id);
            return ResponseResult.success(main);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 查询明细表
     * @param id
     * @return
     */
    @RequestMapping(value = "/getdetaillist")
    @ResponseBody
    public List<OmOrderMainDTO> getDetailList(String id){
        List<OmOrderMainDTO> list=new ArrayList<>();
        try{
            if(CustomStringUtils.isNotBlank(id))
            {
                OmOrderMainDTO m=new OmOrderMainDTO();
                m.setId(id);
                list=omMainService.getDetailList(m);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 保存委外订单到mes
     *
     * @return {@link ResponseResult}
     */
    @PostMapping("save")
    public ResponseResult save(String mainStr,String productStr,String partStr,String materialStr){
        try {
            OmOrderMain main = JSON.parseObject(mainStr, OmOrderMain.class);
            List<OmOrderProductDTO> productList = JSON.parseArray(productStr,OmOrderProductDTO.class);
            List<OmOrderPartDTO> partList = null;
            if (partStr != null){
                partList = JSON.parseArray(partStr,OmOrderPartDTO.class);
            }
            List<OmOrderMaterialDTO> materialList = JSON.parseArray(materialStr,OmOrderMaterialDTO.class);
            return omMainService.saveToMes(main,productList,partList,materialList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }

    }





}
