package com.web.om.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.*;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.basicinfo.service.IVendorService;
import com.web.common.controller.BasicController;
import com.web.om.dto.*;
import com.web.om.entity.*;
import com.web.om.mapper.OmMoDetailsMapper;
import com.web.om.mapper.OmMoMainMapper;
import com.web.om.service.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;


/**
 * Created by caihuan on 2021-04-13.
 */
@RestController
@RequestMapping(value = "/om/order")
public class OmOrderController extends BasicController {

    @Autowired
    private IOmOrderMainService mesMainService;

    @Autowired
    private IVendorService vendorService;

    @Autowired
    private IOmOrderDetailService mesProductService;

    @Autowired
    private IOmOrderMaterialService mesMaterialService;

    @Autowired
    private IOmOrderPartService partService;

    @Autowired
    private IOmMoMainService u8MainService;

    @Autowired
    private OmMoMainMapper u8MainMapper;

    @Autowired
    private OmMoDetailsMapper u8ProductMapper;

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
            List<OmOrderMain> mainList = mesMainService.list(select);
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
            resultPage =  mesMainService.getMainList(data,page1);

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
            OmOrderMain main = mesMainService.getById(id);
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
            return mesMainService.getAllMainDataById(id);
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
            List<OmOrderDetail> mainList = mesProductService.list(wrapper);
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
            IPage<OmOrderMaterial> resultPage = mesMaterialService.page(iPage,wrapper);
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
            List<OmOrderMaterial> mainList = mesMaterialService.list(wrapper);
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
                list= mesMainService.getDetailList(m);

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
            return mesMainService.saveToMes(main,productList,partList,materialList);
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
            //已审核订单不允许编辑
            DbContextHolder.setDbType(DBTypeEnum.db2);
            OmMoMain u8Main= u8MainService.getById(main.getU8Id());
            if (u8Main != null){
                if (!u8Main.getCstate().toString().equals("0")){
                    throw new Exception("已审核，不允许编辑");
                }
            }

            DbContextHolder.setDbType(DBTypeEnum.db1);
            List<OmOrderDetail> productList = JSON.parseArray(productStr,OmOrderDetail.class);
            List<OmOrderPart> partList = null;
            if (partStr != null){
                partList = JSON.parseArray(partStr,OmOrderPart.class);
            }
            List<OmOrderMaterial> materialList = JSON.parseArray(materialStr,OmOrderMaterial.class);
            return mesMainService.updateToMes(main,productList,partList,materialList);
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
            OmOrderMain mesMain = mesMainService.getById(id);
            Integer u8Id =  mesMain.getU8Id();
            DbContextHolder.setDbType(DBTypeEnum.db2);
            OmMoMain u8Main= u8MainMapper.selectById(u8Id);
            DbContextHolder.setDbType(DBTypeEnum.db1);
            if(u8Main!=null) {
                if (!u8Main.getCstate().toString().equals("0")) {
                    return ResponseResult.error("数据已审核，不允许作废！");
                }
            }
            return mesMainService.deleteMainById(id);
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
            Integer u8Id = main.getU8Id();
            if (u8Id != null) {
                if (u8Id >= 0 ){
                    return ResponseResult.error("已审核，不允许审核");
                }
            }

            List<OmOrderDetail> productList = JSON.parseArray(productStr,OmOrderDetail.class);
            List<OmOrderMaterial> materialList = JSON.parseArray(materialStr,OmOrderMaterial.class);
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
            result = mesMainService.audit(u8Main,u8DetailList,u8MaterialList,main,productList,materialList);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }

    }

    /**
     * 变更
     *
     * @return {@link ResponseResult}
     */
    @PostMapping("change")
    public ResponseResult change(String mainStr,String productStr,String materialStr){
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            OmOrderMain main = JSON.parseObject(mainStr, OmOrderMain.class);
            List<OmOrderDetail> productList = JSON.parseArray(productStr,OmOrderDetail.class);
            List<OmOrderMaterial> materialList = JSON.parseArray(materialStr,OmOrderMaterial.class);
            if (productList.size() == 0){
                return ResponseResult.error("产品列表不能为空");
            }
            if (materialList.size() == 0) {
                return ResponseResult.error("材料列表不能为空");
            }
            return mesMainService.change(main,productList,materialList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }

    }

    /**
     * 弃审
     * @param id
     * @return
     */
    @RequestMapping(value = "uncheck")
    @ResponseBody
    public ResponseResult unCheck(Integer id,String mesId){
        ResponseResult result = new ResponseResult();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            result=  mesMainService.unCheck(id,mesId);

        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

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
            sheet.setColumnWidth(17, 40 * 20 * 5);
            String [] headerArr = {
                    "序号",
                    "业务类型",
                    "订单编号",
                    "订单日期",
                    "供应商",
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

            OmOrderMain mesMain = mesMainService.getById(mesId);
            if (mesMain == null){
                throw new Exception("无此订单！");
            }
            LambdaQueryWrapper<OmOrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OmOrderDetail::getMainId,mesId);
            wrapper.eq(OmOrderDetail::getIzDelete,0);
            List<OmOrderDetail> productList = mesProductService.list(wrapper);


            for (OmOrderDetail product : productList){
                int colId = 0;
                row = instanceRow(sheet, rowId, 20);
                SetValue(rowId, colId, sheet, row, styletxtleft, rowId);
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, "委外加工");
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, mesMain.getVouchCode());
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, DateUtil.getYyyyMmDdStrDate(mesMain.getVouchDate()));
                colId++;
                SetValue(rowId, colId, sheet, row, styletxtleft, mesMain.getVenName());
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

            String pdtname = "委外订单";

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







}
