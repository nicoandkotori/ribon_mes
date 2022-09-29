package com.web.pd.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.*;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.common.controller.BasicController;
import com.web.om.dto.OmOrderMainDTO;
import com.web.om.dto.OmProductVM;
import com.web.om.entity.*;
import com.web.om.mapper.OmMoDetailsMapper;
import com.web.om.mapper.OmMoMainMapper;
import com.web.om.service.*;
import com.web.pd.dto.PdOrderDetailDTO;
import com.web.pd.dto.PdOrderMainDTO;
import com.web.pd.entity.PdOrderDetail;
import com.web.pd.entity.PdOrderMain;
import com.web.pd.entity.PdOrderMaterial;
import com.web.pd.entity.PdOrderPart;
import com.web.pd.service.PdOrderDetailService;
import com.web.pd.service.PdOrderMainService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.util.annotation.Nullable;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生产订单controller
 *
 * @author mijiahao
 * @date 2022/09/27
 */
@RestController
@RequestMapping(value = "/pd/order")
public class PdOrderController extends BasicController {




    @Autowired
    private IOmMoMainService u8MainService;

    @Autowired
    private OmMoMainMapper u8MainMapper;

    @Autowired
    private OmMoDetailsMapper u8ProductMapper;
    
    @Autowired
    private PdOrderMainService pdOrderMainService;

    @Autowired
    private PdOrderDetailService mesProductService;

    /**
     * 分页等于查询
     *
     * @param page  页号
     * @param rows 页面大小
     * @param query 查询条件
     * @return {@link TableResult}
     */
    @GetMapping("equal_find")
    public TableResult<PdOrderMain> equalFind(@Nullable Integer page ,@Nullable Integer rows,@Nullable String query) {
        TableResult<PdOrderMain> result = new TableResult<>();
        try {
            if (page == null) {
                page = 1;
            }
            if (rows == null) {
                rows = 10;
            }
            LambdaQueryWrapper<PdOrderMain> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PdOrderMain::getIzDelete,0);
            IPage<PdOrderMain> iPage = new Page<>(page, rows);
            if (StringUtils.isNotBlank(query)){
                PdOrderMain pdOrderMain = JSON.parseObject(query, PdOrderMain.class);
                wrapper.setEntity(pdOrderMain);
            }
            IPage<PdOrderMain> resultPage = pdOrderMainService.page(iPage, wrapper);
            List<PdOrderMain> resultList = resultPage.getRecords();
            if (resultList.size() < 1) {
                return TableResult.error("查询不到数据");
            }
            result.setRecords(resultPage.getTotal());
            result.setRows(resultPage.getRecords());
            result.setTotal((int)Math.ceil(resultPage.getTotal()/(double)resultPage.getSize()));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TableResult.error(e.getMessage());
        }
    }

    /**
     * 分页模糊查询
     *
     * @param page     页面
     * @param rows     行
     * @param querystr querystr
     * @return {@link TableResult}<{@link PdOrderMain}>
     */
    @GetMapping("find_page")
    public TableResult<PdOrderMainDTO> findPage(@Nullable Integer page, @Nullable Integer rows, @Nullable String querystr){
        try{
            TableResult<PdOrderMainDTO> result = new TableResult<>();
            if (page == null) {
                page = 1;
            }
            if (rows == null) {
                rows = 10;
            }
            IPage<PdOrderMainDTO> page1 = new Page<>(page, rows);
            IPage<PdOrderMainDTO> resultPage = new Page<>(page, rows);
            PdOrderMainDTO data = JSON.parseObject(querystr, PdOrderMainDTO.class);
            if(data == null){
                data = new PdOrderMainDTO();
            }
            data.setIzDelete(0);
            resultPage =  pdOrderMainService.getMainList(data,page1);
            result.setRecords(resultPage.getTotal());
            result.setTotal((int)Math.ceil(resultPage.getTotal()/(double)resultPage.getSize()));
            result.setRows(resultPage.getRecords());
            return result;
        }catch(Exception e){
            e.printStackTrace();
            return TableResult.error(e.getMessage());
        }

    }

    /**
     * 通过id查询一个订单中的所有数据
     *
     * @return {@link TableResult}
     */
    @GetMapping("get_all_main_data_by_id")
    public TableResult<PdOrderMain> getAllMainDataById(String id){
        try {
            return pdOrderMainService.getAllMainDataById(id);
        } catch (Exception e){
            e.printStackTrace();
            return TableResult.error(e.getMessage());
        }

    }

    /**
     * 生成最大单号
     */
    @RequestMapping(value = "/get_max_vouch_code")
    public ResponseResult getMaxCode(){
        ResponseResult result = new ResponseResult();
        try{
            String head = "SC" + DateUtil.getDateStr(new Date(), "yyyyMMdd");
            String sortNum = "001";
            int sortNumLength = 3;

            LambdaQueryWrapper<PdOrderMain> select=new LambdaQueryWrapper<>();
            select.select(PdOrderMain::getVouchCode)
                    .like(PdOrderMain::getVouchCode, head);
            select.orderByDesc(PdOrderMain::getVouchCode);
            List<PdOrderMain> mainList = pdOrderMainService.list(select);
            if(mainList.size() > 0){
                String maxCode = mainList.get(0).getVouchCode();
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
     * 保存生产订单到mes
     *
     * @return {@link ResponseResult}
     */
    @PostMapping("save")
    public ResponseResult save(String mainStr,String productStr,String partStr,String materialStr){
        try {
            PdOrderMain main = JSON.parseObject(mainStr, PdOrderMain.class);
            List<PdOrderDetail> productList = JSON.parseArray(productStr,PdOrderDetail.class);
            List<PdOrderPart> partList = null;
            if (partStr != null){
                partList = JSON.parseArray(partStr, PdOrderPart.class);
            }
            List<PdOrderMaterial> materialList = JSON.parseArray(materialStr,PdOrderMaterial.class);
            return pdOrderMainService.saveToMes(main,productList,partList,materialList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 更新mes生产订单
     *
     * @return {@link ResponseResult}
     */
    @PostMapping("update")
    public ResponseResult update(String mainStr,@Nullable String productStr,String partStr,String materialStr){
        try {
            PdOrderMain main = JSON.parseObject(mainStr, PdOrderMain.class);
            //已审核订单不允许编辑
            DbContextHolder.setDbType(DBTypeEnum.db2);
            OmMoMain u8Main= u8MainService.getById(main.getU8Id());
            if (u8Main != null){
                if (!u8Main.getCstate().toString().equals("0")){
                    throw new Exception("已审核，不允许编辑");
                }
            }

            DbContextHolder.setDbType(DBTypeEnum.db1);
            List<PdOrderDetail> productList = JSON.parseArray(productStr,PdOrderDetail.class);
            List<PdOrderPart> partList = null;
            if (partStr != null){
                partList = JSON.parseArray(partStr,PdOrderPart.class);
            }
            List<PdOrderMaterial> materialList = JSON.parseArray(materialStr,PdOrderMaterial.class);
            return pdOrderMainService.updateToMes(main,productList,partList,materialList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }
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
            PdOrderMain main = pdOrderMainService.getById(id);
            return ResponseResult.success(main);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 通过mainId获取产品表
     *
     * @return {@link ResponseResult}
     */
    @GetMapping("get_mes_product_by_main_id")
    public TableResult<PdOrderDetail> getMesProductByMainId(String mainId){
        try {
            LambdaQueryWrapper<PdOrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(mainId),PdOrderDetail::getMainId,mainId);
            wrapper.eq(PdOrderDetail::getIzDelete,0);
            List<PdOrderDetail> mainList = mesProductService.list(wrapper);
            return TableResult.success(mainList);
        } catch (Exception e){
            e.printStackTrace();
            return TableResult.error(e.getMessage());
        }
    }

    /**
     * 通过id作废生产订单
     *
     * @return {@link TableResult}<{@link PdOrderMain}>
     */
//    @PostMapping("delete_main_by_id")
//    public ResponseResult deleteMainById(String id){
//        try {
//            PdOrderMain mesMain = pdOrderMainService.getById(id);
//            Integer u8Id =  mesMain.getU8Id();
//            DbContextHolder.setDbType(DBTypeEnum.db2);
//            OmMoMain u8Main= u8MainMapper.selectById(u8Id);
//            DbContextHolder.setDbType(DBTypeEnum.db1);
//            if(u8Main!=null) {
//                if (!u8Main.getCstate().toString().equals("0")) {
//                    return ResponseResult.error("数据已审核，不允许作废！");
//                }
//            }
//            return pdOrderMainService.deleteMainById(id);
//        } catch (Exception e){
//            e.printStackTrace();
//            return ResponseResult.error(e.getMessage());
//        }
//    }

    /**
     * 审核数据，写入U8
     *
     * @return {@link ResponseResult}
     */
//    @PostMapping("audit")
//    public ResponseResult audit(String mainStr,String productStr,String materialStr){
//        try {
//            ResponseResult result = null;
//            DbContextHolder.setDbType(DBTypeEnum.db2);
//            PdOrderMain main = JSON.parseObject(mainStr, PdOrderMain.class);
//            Integer u8Id = main.getU8Id();
//            if (u8Id != null) {
//                if (u8Id >= 0 ){
//                    return ResponseResult.error("已审核，不允许审核");
//                }
//            }
//
//            List<PdOrderDetail> productList = JSON.parseArray(productStr,PdOrderDetail.class);
//            List<PdOrderDetail> materialList = JSON.parseArray(materialStr,PdOrderDetail.class);
//            //主表转换
//            OmMoMain u8Main = new OmMoMain();
//            u8Main.setDataFromMesMain(main);
//            //产品记录转换
//            List<OmProductVM> u8DetailList = new ArrayList<>();
//            productList.forEach(product ->{
//                OmProductVM u8Detail = new OmProductVM();
//                u8Detail.setDataFromMesProduct(product);;
//                u8DetailList.add(u8Detail);
//            });
//            //材料记录转换
//            List<OmProductVM> u8MaterialList = new ArrayList<>();
//            materialList.forEach(material -> {
//                OmProductVM u8Material = new OmProductVM();
//                u8Material.setDataFromMesMaterial(material);
//                u8MaterialList.add(u8Material);
//            });
//            result = pdOrderMainService.audit(u8Main,u8DetailList,u8MaterialList,main,productList,materialList);
//            return result;
//        } catch (Exception e){
//            e.printStackTrace();
//            return ResponseResult.error(e.getMessage());
//        }
//
//    }

    /**
     * 变更
     *
     * @return {@link ResponseResult}
     */
//    @PostMapping("change")
//    public ResponseResult change(String mainStr,String productStr,String materialStr){
//        try {
//            DbContextHolder.setDbType(DBTypeEnum.db2);
//            PdOrderMain main = JSON.parseObject(mainStr, PdOrderMain.class);
//            List<PdOrderDetail> productList = JSON.parseArray(productStr,PdOrderDetail.class);
//            List<PdOrderDetail> materialList = JSON.parseArray(materialStr,PdOrderDetail.class);
//            if (productList.size() == 0){
//                return ResponseResult.error("产品列表不能为空");
//            }
//            if (materialList.size() == 0) {
//                return ResponseResult.error("材料列表不能为空");
//            }
//            return pdOrderMainService.change(main,productList,materialList);
//        } catch (Exception e){
//            e.printStackTrace();
//            return ResponseResult.error(e.getMessage());
//        }
//
//    }

    /**
     * 出口
     * 弃审
     *
     * @param mesId    mes id
     * @param response 响应
     */
//    @RequestMapping(value = "uncheck")
//    @ResponseBody
//    public ResponseResult unCheck(Integer id,String mesId){
//        ResponseResult result = new ResponseResult();
//        try{
//            DbContextHolder.setDbType(DBTypeEnum.db2);
//            result=  pdOrderMainService.unCheck(id,mesId);
//
//        }catch (Exception e){
//            result.setSuccess(false);
//            result.setMsg(e.getMessage());
//            e.printStackTrace();
//        }
//        return result;
//    }


    /**
     * 导出excel
     *
     * @param mesId    mesId
     * @param response 响应
     */
    @GetMapping("export")
    public void export(String mesId, HttpServletResponse response){
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();

            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet("sheet1");
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            // 1.生成字体对象 (表头)
            HSSFFont fonttitle = wb.createFont();
            fonttitle.setFontHeightInPoints((short) 12);
            fonttitle.setFontName("宋体");

            // 2.生成样式对象 (表头)
            HSSFCellStyle styletitle = wb.createCellStyle();
            styletitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            styletitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            styletitle.setFont(fonttitle); // 调用字体样式对象
            styletitle.setWrapText(true);
            // 样式 表单
            HSSFFont fonttxt = wb.createFont();
            fonttxt.setFontHeightInPoints((short) 12);
            fonttxt.setFontName("宋体");

            HSSFCellStyle styletxt = wb.createCellStyle();
            styletxt.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styletxt.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            styletxt.setFont(fonttxt); // 调用字体样式对象
            styletxt.setWrapText(false);

            HSSFCellStyle styletxtheader = wb.createCellStyle();
            styletxtheader.setBorderTop(BorderStyle.THIN);
            styletxtheader.setBorderLeft(BorderStyle.THIN);
            styletxtheader.setBorderRight(BorderStyle.THIN);
            styletxtheader.setBorderBottom(BorderStyle.THIN);
            styletxtheader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            styletxtheader.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            styletxtheader.setFont(fonttxt); // 调用字体样式对象
            styletxtheader.setWrapText(false);

            HSSFCellStyle styletxtleft = wb.createCellStyle();
            styletxtleft.setBorderTop(BorderStyle.THIN);
            styletxtleft.setBorderLeft(BorderStyle.THIN);
            styletxtleft.setBorderRight(BorderStyle.THIN);
            styletxtleft.setBorderBottom(BorderStyle.THIN);
            styletxtleft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            styletxtleft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            styletxtleft.setFont(fonttxt); // 调用字体样式对象
            styletxtleft.setWrapText(false);

            HSSFFont fonttextcenterdecimal = wb.createFont();
            fonttextcenterdecimal.setFontHeightInPoints((short) 12);
            fonttextcenterdecimal.setFontName("宋体");

            HSSFCellStyle stylecelltextcenterdecimal = (HSSFCellStyle)wb.createCellStyle();
            stylecelltextcenterdecimal.setBorderTop(BorderStyle.THIN);
            stylecelltextcenterdecimal.setBorderLeft(BorderStyle.THIN);
            stylecelltextcenterdecimal.setBorderRight(BorderStyle.THIN);
            stylecelltextcenterdecimal.setBorderBottom(BorderStyle.THIN);
            stylecelltextcenterdecimal.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            stylecelltextcenterdecimal.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            stylecelltextcenterdecimal.setFont(fonttextcenterdecimal);
            stylecelltextcenterdecimal.setDataFormat(HSSFDataFormat.getBuiltinFormat("G/通用格式"));

            HSSFRow row = null;
            //设置列宽
            sheet.setColumnWidth(0, 40 * 21 * 5);
            sheet.setColumnWidth(1, 40 * 20 * 5);
            sheet.setColumnWidth(2, 40 * 20 * 5);
            sheet.setColumnWidth(3, 40 * 20 * 5);
            sheet.setColumnWidth(4, 40 * 20 * 5);
            sheet.setColumnWidth(5, 40 * 20 * 5);
            sheet.setColumnWidth(6, 40 * 20 * 5);
            sheet.setColumnWidth(7, 40 * 20 * 5);
            sheet.setColumnWidth(8, 40 * 20 * 5);
            sheet.setColumnWidth(9, 40 * 20 * 5);
            sheet.setColumnWidth(10, 40 * 20 * 5);
            sheet.setColumnWidth(11, 40 * 20 * 5);
            sheet.setColumnWidth(12, 40 * 20 * 5);
            sheet.setColumnWidth(13, 40 * 20 * 5);
            sheet.setColumnWidth(14, 40 * 20 * 5);
            sheet.setColumnWidth(15, 40 * 20 * 5);
            sheet.setColumnWidth(16, 40 * 20 * 5);
            String [] headerArr = {
                    "序号",
                    "业务类型",
                    "订单编号",
                    "订单日期",
                    "存货编码",
                    "存货名称",
                    "规格型号",
                    "主计量",
                    "数量",
                    "原币单价",
                    "原币含税单价",
                    "计划开工日期",
                    "计划完工日期",
                    "销售合同号",
                    "累计入库数",
                    "单据状态",
                    "入库状态",
            };

            int rowId = 0;
            row = instanceRow(sheet, rowId, 20);
            for (int i=0; i<headerArr.length; i++){
                SetValue(rowId, i, sheet, row, styletxtheader, headerArr[i]);
            }
            rowId++;

            PdOrderMain mesMain = pdOrderMainService.getById(mesId);
            if (mesMain == null){
                throw new Exception("无此订单！");
            }
            LambdaQueryWrapper<PdOrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PdOrderDetail::getMainId,mesId);
            wrapper.eq(PdOrderDetail::getIzDelete,0);
            List<PdOrderDetail> productList = mesProductService.list(wrapper);


            for (PdOrderDetail product : productList){
                int colId = 0;
                row = instanceRow(sheet, rowId, 20);
                SetValue(rowId, colId, sheet, row, styletxtleft, rowId);
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, "生产加工");
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, mesMain.getVouchCode());
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, DateUtil.getYyyyMmDdStrDate(mesMain.getVouchDate()));
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, product.getProductInvCode());
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, product.getProductInvName());
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, product.getProductInvStd());
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, product.getProductInvUnit());
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, product.getProductQty());
                colId++;
                //原币单价-----
                SetValue(rowId, colId, sheet, row, styletxtleft, product.getWorkPriceWithoutTax());
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, product.getAmount());
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, DateUtil.getYyyyMmDdStrDate(product.getPlanStartDate()));
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, DateUtil.getYyyyMmDdStrDate(product.getPlanEndDate()));
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, mesMain.getContractSale());
                colId++;
                BigDecimal inputNum = new BigDecimal(0);
                String inputStatus = "未入库";
                LambdaQueryWrapper<OmMoDetails> selectD = new LambdaQueryWrapper<>();
                selectD.eq(OmMoDetails::getModetailsid,product.getU8MoDetailId());
                DbContextHolder.setDbType(DBTypeEnum.db2);
                List<OmMoDetails> u8ProductList = u8ProductMapper.selectList(selectD);
                if (u8ProductList.size() != 0){
                    OmMoDetails u8Product = u8ProductList.get(0);
                    inputNum = u8Product.getIreceivedqty();
                    inputStatus = "已入库";
                }
                //累计入库数-----
                SetValue(rowId, colId, sheet, row, styletxtleft, inputNum.stripTrailingZeros().toPlainString());
                colId++;
                //单据状态-----
                SetValue(rowId, colId, sheet, row, styletxtleft, mesMain.getStatusId());
                colId++;
                //入库状态
                SetValue(rowId, colId, sheet, row, styletxtleft, inputStatus);
                colId++;
                rowId++;
            }

            wb.write(out);
            byte[] fileNameByte = null;

            String pdtname = "生产订单";

            fileNameByte = (pdtname + ".xls").getBytes("GBK");

            String filename = new String(fileNameByte, "ISO8859-1");

            byte[] bytes = out.toByteArray();

            response.setContentType("application/x-msdownload");
            response.setContentLength(bytes.length);
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.getOutputStream().write(bytes);

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 通过id作废生产订单
     *
     * @return {@link TableResult}<{@link PdOrderMain}>
     */
    @PostMapping("delete_main_by_id")
    public ResponseResult deleteMainById(String id){
        try {
            PdOrderMain mesMain = pdOrderMainService.getById(id);
            //先不校验是否过审
//            Integer u8Id =  mesMain.getU8Id();
//            DbContextHolder.setDbType(DBTypeEnum.db2);
//            OmMoMain u8Main= u8MainMapper.selectById(u8Id);
//            DbContextHolder.setDbType(DBTypeEnum.db1);
//            if(u8Main!=null) {
//                if (!u8Main.getCstate().toString().equals("0")) {
//                    return ResponseResult.error("数据已审核，不允许作废！");
//                }
//            }
            return pdOrderMainService.deleteMainById(id);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 查询主表join产品表的数据
     * @param id
     * @return
     */
    @GetMapping(value = "/get_main_and_product_list")
    public List<PdOrderDetailDTO> getDetailList(String id){
        List<PdOrderDetailDTO> list=new ArrayList<>();
        try{
            if(CustomStringUtils.isNotBlank(id))
            {
                PdOrderMainDTO m=new PdOrderMainDTO();
                m.setId(id);
                list= pdOrderMainService.getMainAndProductList(m);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    
    
}
