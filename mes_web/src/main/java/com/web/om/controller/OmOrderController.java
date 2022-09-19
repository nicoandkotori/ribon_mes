package com.web.om.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.*;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.basicinfo.entity.Vendor;
import com.web.basicinfo.service.IVendorService;
import com.web.common.controller.BasicController;
import com.web.om.dto.*;
import com.web.om.entity.*;
import com.web.om.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private IOmOrderDetailService detailService;

    @Autowired
    private IOmOrderMaterialService materialService;

    @Autowired
    private IOmOrderPartService partService;

    @Autowired
    private IOmMoMainService u8MainService;

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
    @GetMapping(value = "get_main_by_id")
    public ResponseResult getById(String id) {
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
     * 通过id查询一个订单中的所有数据
     *
     * @return {@link TableResult}
     */
    @GetMapping("get_all_main_data_by_id")
    public TableResult<OmOrderMain> getAllMainDataById(String id){
        try {
            return omMainService.getAllMainDataById(id);
        } catch (Exception e){
            e.printStackTrace();
            return TableResult.error(e.getMessage());
        }
        
    }

    /**
     * 通过mainId获取产品表
     *
     * @return {@link ResponseResult}
     */
    @GetMapping("get_mes_product_by_main_id")
    public TableResult<OmOrderDetail> getMesProductByMainId(String mainId){
        try {
            LambdaQueryWrapper<OmOrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(mainId),OmOrderDetail::getMainId,mainId);
            wrapper.eq(OmOrderDetail::getIzDelete,0);
            List<OmOrderDetail> mainList = detailService.list(wrapper);
            return TableResult.success(mainList);
        } catch (Exception e){
            e.printStackTrace();
            return TableResult.error(e.getMessage());
        }
    }

    /**
     * 通过mainId获取部件表
     *
     * @return {@link ResponseResult}
     */
    @GetMapping("get_mes_part_by_main_id")
    public TableResult<OmOrderPart> getMesPartByMainId(String mainId){
        try {
            LambdaQueryWrapper<OmOrderPart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(mainId),OmOrderPart::getMainId,mainId);
            wrapper.eq(OmOrderPart::getIzDelete,0);
            List<OmOrderPart> mainList = partService.list(wrapper);
            return TableResult.success(mainList);
        } catch (Exception e){
            e.printStackTrace();
            return TableResult.error(e.getMessage());
        }
    }

    /**
     * 分页等于查询部件表
     *
     * @param page  页号
     * @param limit 页面大小
     * @param query 查询条件
     * @return {@link TableResult}
     */
    @PostMapping("equal_find_part")
    public TableResult<OmOrderPart> equalFindPart(Integer page ,Integer limit, String query){
        try {
            if (page == null){
                page = 1;
            }
            if (limit == null){
                limit = 10;
            }
            OmOrderPart omOrderPart = JSON.parseObject(query,OmOrderPart.class);
            LambdaQueryWrapper<OmOrderPart> wrapper = new LambdaQueryWrapper<>();
            IPage<OmOrderPart> iPage = new Page<>(page,limit);
            omOrderPart.setIzDelete(0);
            wrapper.setEntity(omOrderPart);
            IPage<OmOrderPart> resultPage = partService.page(iPage,wrapper);
            List<OmOrderPart> resultList = resultPage.getRecords();
            if (resultList.size()<1){
                return TableResult.error("查询不到数据");
            }
            return TableResult.success(resultList);
        } catch (Exception e){
            e.printStackTrace();
            return TableResult.error(e.getMessage());
        }

    }
    
    /**
     * 分页等于查询材料
     *
     * @param page  页号
     * @param limit 页面大小
     * @param query 查询条件
     * @return {@link TableResult}
     */
    @PostMapping("equal_find_material")
    public TableResult<OmOrderMaterial> equalFind(Integer page ,Integer limit, String query){
        try {
            OmOrderMaterial omOrderMaterial = JSON.parseObject(query,OmOrderMaterial.class);
            LambdaQueryWrapper<OmOrderMaterial> wrapper = new LambdaQueryWrapper<>();
            if (page == null){
                page = 1;
            }
            if (limit == null){
                limit = 10;
            }
            IPage<OmOrderMaterial> iPage = new Page<>(page,limit);
            omOrderMaterial.setIzDelete(0);
            wrapper.setEntity(omOrderMaterial);
            IPage<OmOrderMaterial> resultPage = materialService.page(iPage,wrapper);
            List<OmOrderMaterial> resultList = resultPage.getRecords();
            if (resultList.size()<1){
                return TableResult.error("查询不到数据");
            }
            return TableResult.success(resultList);
        } catch (Exception e){
            e.printStackTrace();
            return TableResult.error(e.getMessage());
        }

    }

    /**
     * 通过mainId获取材料表
     *
     * @return {@link ResponseResult}
     */
    @GetMapping("get_mes_material_by_main_id")
    public TableResult<OmOrderMaterial> getMesMaterialByMainId(String mainId){
        try {
            LambdaQueryWrapper<OmOrderMaterial> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(mainId),OmOrderMaterial::getMainId,mainId);
            wrapper.eq(OmOrderMaterial::getIzDelete,0);
            List<OmOrderMaterial> mainList = materialService.list(wrapper);
            return TableResult.success(mainList);
        } catch (Exception e){
            e.printStackTrace();
            return TableResult.error(e.getMessage());
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
            List<OmOrderDetail> productList = JSON.parseArray(productStr,OmOrderDetail.class);
            List<OmOrderPart> partList = null;
            if (partStr != null){
                partList = JSON.parseArray(partStr,OmOrderPart.class);
            }
            List<OmOrderMaterial> materialList = JSON.parseArray(materialStr,OmOrderMaterial.class);
            return omMainService.saveToMes(main,productList,partList,materialList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 更新mes委外订单
     *
     * @return {@link ResponseResult}
     */
    @PostMapping("update")
    public ResponseResult update(String mainStr,String productStr,String partStr,String materialStr){
        try {
            OmOrderMain main = JSON.parseObject(mainStr, OmOrderMain.class);
            List<OmOrderDetail> productList = JSON.parseArray(productStr,OmOrderDetail.class);
            List<OmOrderPart> partList = null;
            if (partStr != null){
                partList = JSON.parseArray(partStr,OmOrderPart.class);
            }
            List<OmOrderMaterial> materialList = JSON.parseArray(materialStr,OmOrderMaterial.class);
            return omMainService.updateToMes(main,productList,partList,materialList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 通过id作废委外订单
     *
     * @return {@link TableResult}<{@link OmOrderMain}>
     */
    @PostMapping("delete_main_by_id")
    public ResponseResult deleteMainById(String id){
        try {
            /**
             * DATE: 2022/9/15
             * mijiahao TODO: 作废前还要判断有没有审核
             */
            return omMainService.deleteMainById(id);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 审核数据，写入U8
     *
     * @return {@link ResponseResult}
     */
    @PostMapping("audit")
    public ResponseResult audit(String mainStr,String productStr,String materialStr){
        try {
            ResponseResult result = null;
            DbContextHolder.setDbType(DBTypeEnum.db2);
            OmOrderMain main = JSON.parseObject(mainStr, OmOrderMain.class);
            List<OmOrderDetail> productList = JSON.parseArray(productStr,OmOrderDetail.class);
            List<OmOrderMaterial> materialList = JSON.parseArray(materialStr,OmOrderMaterial.class);
            if ("已审核".equals(main.getStatusId())) {
                return ResponseResult.error("已审核，请勿重复审核");
            }
            //主表转换
            OmMoMain u8Main = new OmMoMain();
            u8Main.setDataFromMesMain(main);
            //产品记录转换
            List<OmProductVM> u8DetailList = new ArrayList<>();
            productList.forEach(product ->{
                OmProductVM u8Detail = new OmProductVM();
                u8Detail.setDataFromMesProduct(product);;
                u8DetailList.add(u8Detail);
            });
            //材料记录转换
            List<OmProductVM> u8MaterialList = new ArrayList<>();
            materialList.forEach(material -> {
                OmProductVM u8Material = new OmProductVM();
                u8Material.setDataFromMesMaterial(material);
                u8MaterialList.add(u8Material);
            });
            result = omMainService.audit(u8Main,u8DetailList,u8MaterialList,main);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }

    }





}
