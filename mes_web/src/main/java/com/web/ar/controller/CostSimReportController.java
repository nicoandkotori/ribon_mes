package com.web.ar.controller;

import com.alibaba.fastjson.JSON;
import com.common.util.CustomStringUtils;
import com.common.util.ResponseResult;
import com.common.util.TableResult;
import com.web.ar.entity.CostSimReport;
import com.web.ar.service.ICostSimReportService;
import com.web.ar.service.ITestAccListService;
import com.web.ar.service.ITestAccService;
import com.web.common.controller.BasicController;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.*;


/**
 * Created by caihuan on 2021-04-13.
 */
@RestController
@RequestMapping(value = "/ar/costsimreport")
public class CostSimReportController extends BasicController {

    @Autowired
    private ICostSimReportService costSimReportService;
    @Autowired
    private ITestAccListService testAccListService;
    @Autowired
    private ITestAccService testAccService;


    /**
     * 查询成本核算报表
     * @param querystr
     * @return
     */
    @RequestMapping(value = "/getlist")
    @ResponseBody
    public TableResult<CostSimReport> getList( String querystr){
        TableResult<CostSimReport> result = new TableResult<CostSimReport>();
        try {

            CostSimReport query= JSON.parseObject(querystr,CostSimReport.class);
            if(query==null)
            {
                result.setSuccess(false);
                result.setErrormsg("请选择日期！");
                return result;
            }


            if(query.getDateEnd()!=null)
            {
                Calendar c = Calendar.getInstance();
                c.setTime(query.getDateEnd());
                c.add(Calendar.DAY_OF_MONTH, 1);
                query.setDateEnd(c.getTime());
            }


            //先删除数据，后插入数据
            ResponseResult res=testAccListService.delete();
            if(res.isSuccess()==false)
            {
                result.setSuccess(false);
                result.setErrormsg(res.getMsg());
                return result;
            }
            res= costSimReportService.delete();
            if(res.isSuccess()==false)
            {
                result.setSuccess(false);
                result.setErrormsg(res.getMsg());
                return result;
            }

            //插入入库数据
            res=costSimReportService.insertRd1(query);
            if(res.isSuccess()==false)
            {
                result.setSuccess(false);
                result.setErrormsg(res.getMsg());
                return result;
            }



            //查询计算完成的内容
            List<CostSimReport>  scaList=costSimReportService.getList(query);
            result.setRows(scaList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




    /**
     * 查询金工成本列表

     * @param querystr
     * @return
     */
    @RequestMapping(value = "/getmetallist")
    @ResponseBody
    public TableResult<CostSimReport> getMetalListByPage( String querystr){
        TableResult<CostSimReport> result = new TableResult<CostSimReport>();
        try {

            CostSimReport query= JSON.parseObject(querystr,CostSimReport.class);
            if(query==null)
            {
                result.setSuccess(false);
                result.setErrormsg("请选择年月！");
                return result;
            }

            //先删除数据，后插入数据
            ResponseResult res=testAccListService.delete();
            if(res.isSuccess()==false)
            {
                result.setSuccess(false);
                result.setErrormsg(res.getMsg());
                return result;
            }
            res= costSimReportService.delete();
            if(res.isSuccess()==false)
            {
                result.setSuccess(false);
                result.setErrormsg(res.getMsg());
                return result;
            }
            if((query.getSoCode()==null||query.getSoCode().equals(""))&&(query.getInvCode().equals("")||query.getInvCode().equals("")))
            {
                res=costSimReportService.insertRd(query);
                if(res.isSuccess()==false)
                {
                    result.setSuccess(false);
                    result.setErrormsg(res.getMsg());
                    return result;
                }
            }
            else
            {
                res=costSimReportService.insertSo(query);
                if(res.isSuccess()==false)
                {
                    result.setSuccess(false);
                    result.setErrormsg(res.getMsg());
                    return result;
                }
            }

            List<CostSimReport> scaList=costSimReportService.getMetalList(query);
            result.setRows(scaList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




    /**
     * 查询原料成本列表

     * @param querystr
     * @return
     */
    @RequestMapping(value = "/getmateriallist")
    @ResponseBody
    public TableResult<CostSimReport> getMaterialListByPage( String querystr){
        TableResult<CostSimReport> result = new TableResult<CostSimReport>();
        try {

            CostSimReport query= JSON.parseObject(querystr,CostSimReport.class);
            if(query==null)
            {
                result.setSuccess(false);
                result.setErrormsg("请输入存货编码！");
                return result;
            }

            //先删除数据，后插入数据
            ResponseResult res=testAccListService.delete();
            if(res.isSuccess()==false)
            {
                result.setSuccess(false);
                result.setErrormsg(res.getMsg());
                return result;
            }
            res= costSimReportService.delete();
            if(res.isSuccess()==false)
            {
                result.setSuccess(false);
                result.setErrormsg(res.getMsg());
                return result;
            }

            testAccService.saveByInvCode(query.getInvCode());

            List<CostSimReport> scaList= costSimReportService.getDetailList(query);
            result.setRows(scaList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    /**
     * 保存成功后重新查询
     * @param querystr
     * @return
     */
    @RequestMapping(value = "/getlistforsave")
    @ResponseBody
    public TableResult<CostSimReport> getListForSave1( String querystr){
        TableResult<CostSimReport> result = new TableResult<CostSimReport>();
        try {

            CostSimReport query= JSON.parseObject(querystr,CostSimReport.class);
            if(query==null)
            {
                result.setSuccess(false);
                result.setErrormsg("请选择年月！");
                return result;
            }

            List<CostSimReport> scaList= costSimReportService.getList(query);

            result.setRows(scaList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 保存成功后重新查询
     * @param querystr
     * @return
     */
    @RequestMapping(value = "/getmetallistforsave")
    @ResponseBody
    public TableResult<CostSimReport> getMetalListForSave( String querystr){
        TableResult<CostSimReport> result = new TableResult<CostSimReport>();
        try {

            CostSimReport query= JSON.parseObject(querystr,CostSimReport.class);
            if(query==null)
            {
                result.setSuccess(false);
                result.setErrormsg("请选择年月！");
                return result;
            }

            List<CostSimReport> scaList= costSimReportService.getMetalListForSave(query);

            result.setRows(scaList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 查询成本明细

     * @param querystr
     * @return
     */
    @RequestMapping(value = "/getdetaillist")
    @ResponseBody
    public TableResult<CostSimReport> getDetailList( String querystr){
        TableResult<CostSimReport> result = new TableResult<CostSimReport>();
        try {

            CostSimReport query= JSON.parseObject(querystr,CostSimReport.class);
            if(query==null)
            {
                result.setSuccess(false);
                result.setErrormsg("参数不存在！");
                return result;
            }


            List<CostSimReport> scaList= costSimReportService.getDetailList(query);
            result.setRows(scaList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 查询金工明细

     * @param querystr
     * @return
     */
    @RequestMapping(value = "/getmetaldetaillist")
    @ResponseBody
    public TableResult<CostSimReport> getmetaldetaillist( String querystr){
        TableResult<CostSimReport> result = new TableResult<CostSimReport>();
        try {

            CostSimReport query= JSON.parseObject(querystr,CostSimReport.class);
            if(query==null)
            {
                result.setSuccess(false);
                result.setErrormsg("参数不存在！");
                return result;
            }


            List<CostSimReport> scaList= costSimReportService.getMetalDetailList(query);
            result.setRows(scaList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 保存
     * @param mData

     * @return
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public ResponseResult save(String mData,String iState) {
        ResponseResult result = new ResponseResult();
        try{
            if (CustomStringUtils.isBlank(mData)) {
                throw new Exception("参数异常！");
            }


            List<CostSimReport> list = JSON.parseArray(mData, CostSimReport.class);

            result=testAccService.save(list,iState);
            result.setMsg("计算成功");
        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    /**
     * 保存
     * @param mData

     * @return
     */
    @RequestMapping(value = "/save1")
    @ResponseBody
    public ResponseResult save1(String mData,String iState) {
        ResponseResult result = new ResponseResult();
        try{
            if (CustomStringUtils.isBlank(mData)) {
                throw new Exception("参数异常！");
            }


            List<CostSimReport> list = JSON.parseArray(mData, CostSimReport.class);

            result=testAccService.save1(list,iState);
        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }



    /**
     * 保存
     * @param mData

     * @return
     */
    @RequestMapping(value = "/saveprice")
    @ResponseBody
    public ResponseResult saveprice(String mData) {
        ResponseResult result = new ResponseResult();
        try{
            if (CustomStringUtils.isBlank(mData)) {
                throw new Exception("参数异常！");
            }

            List<CostSimReport> list = JSON.parseArray(mData, CostSimReport.class);

            result=costSimReportService.saveprice(list);
        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    @RequestMapping(value = "/export")
    public void export(String queryStr, Integer izJs, HttpServletResponse response){
        try{
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
            sheet.setColumnWidth(0, 40 * 21 * 5);
            sheet.setColumnWidth(1, 40 * 20 * 5);
            sheet.setColumnWidth(2, 40 * 20 * 5);
            sheet.setColumnWidth(3, 40 * 20 * 5);
            sheet.setColumnWidth(4, 40 * 20 * 5);


            int irow = -1;
            // 设置标题
            irow += 1;
            row = instanceRow(sheet, irow, 30);
            SetValueRegion(irow, irow, 0, 5, irow, 0, sheet, row, styletitle, "金工费成本统计");



            String [] headerArr = {
                    "存货编码",  "存货名称",  "规格型号", "单位","入库量","金加工费"
            };

            irow += 1;
            row = instanceRow(sheet, irow, 20);
            for (int i=0; i<headerArr.length; i++){
                SetValue(irow, i, sheet, row, styletxtheader, headerArr[i]);
            }
            CostSimReport query= JSON.parseObject(queryStr,CostSimReport.class);

            List<CostSimReport> list=new ArrayList<>();
            if(izJs==0)
            {
                list=costSimReportService.getMetalList(query);
            }
            else
            {
                list= costSimReportService.getMetalListForSave(query);
            }

            for (CostSimReport s : list){
                irow += 1;
                row = instanceRow(sheet, irow, 20);

                int iCol = 0;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvCode());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvName());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvStd());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getUnit());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getIqty());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getImonery());


            }

            irow += 1;
            row = instanceRow(sheet, irow, 20);

            int iCol = 0;
            SetValue(irow, iCol, sheet, row, styletxtleft, "合计");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");




            //合计
            BigDecimal sumQty = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getIqty()))
                    .map(CostSimReport::getIqty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumQty);

            //合计
            BigDecimal sumAmount = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getImonery()))
                    .map(CostSimReport::getImonery)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumAmount);

            wb.write(out);
            byte[] fileNameByte = null;

            String pdtname = "金工费成本统计";

            fileNameByte = (pdtname + ".xls").getBytes("GBK");

            String filename = new String(fileNameByte, "ISO8859-1");

            byte[] bytes = out.toByteArray();

            response.setContentType("application/x-msdownload");
            response.setContentLength(bytes.length);
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.getOutputStream().write(bytes);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @RequestMapping(value = "/export1")
    public void export1(String queryStr,Integer izJs,HttpServletResponse response){
        try{
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

            int irow = -1;
            // 设置标题
            irow += 1;
            row = instanceRow(sheet, irow, 30);
            SetValueRegion(irow, irow, 0, 17, irow, 0, sheet, row, styletitle, "成本核算统计");



            String [] headerArr = {
                    "客户名称",  "合同号",  "产品编码", "产品名称","规格","类型","仓库","数量","销售价格","数量(板材型材kg)","金额(板材型材kg)"
                    ,"数量(板材型材M)","金额(板材型材M)","金加工材料费","金加工费","委外合计","其他金额","总金额"
            };

            irow += 1;
            row = instanceRow(sheet, irow, 20);
            for (int i=0; i<headerArr.length; i++){
                SetValue(irow, i, sheet, row, styletxtheader, headerArr[i]);
            }
            CostSimReport query= JSON.parseObject(queryStr,CostSimReport.class);

            List<CostSimReport> list=new ArrayList<>();

            list=costSimReportService.getList(query);

            for (CostSimReport s : list){
                irow += 1;
                row = instanceRow(sheet, irow, 20);

                int iCol = 0;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getCustName());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getOrderNo());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvCode());
                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvName());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvStd()==null?"": s.getInvStd());
                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvType()==null?"": s.getInvType());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getWhName());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, styletxtleft, s.getIqty());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, styletxtleft, s.getSaleAmount());



                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, styletxtleft, s.getKqty());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, styletxtleft, s.getKnum());


                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getMqty());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getMnum());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getWmny());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getWnum());


                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getWtol());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getOmum());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getTolnum());



            }


            wb.write(out);
            byte[] fileNameByte = null;

            String pdtname = "成本核算统计";

            fileNameByte = (pdtname + ".xls").getBytes("GBK");

            String filename = new String(fileNameByte, "ISO8859-1");

            byte[] bytes = out.toByteArray();

            response.setContentType("application/x-msdownload");
            response.setContentLength(bytes.length);
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.getOutputStream().write(bytes);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @RequestMapping(value = "/exportdetail")
    public void exportdetail(String queryStr,HttpServletResponse response){
        try{
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
            sheet.setColumnWidth(0, 40 * 21 * 5);
            sheet.setColumnWidth(1, 40 * 20 * 5);
            sheet.setColumnWidth(2, 40 * 20 * 5);
            sheet.setColumnWidth(3, 40 * 20 * 5);
            sheet.setColumnWidth(4, 40 * 20 * 5);
            sheet.setColumnWidth(5, 40 * 20 * 5);
            sheet.setColumnWidth(6, 40 * 20 * 5);
            sheet.setColumnWidth(7, 40 * 20 * 5);
            sheet.setColumnWidth(8, 40 * 20 * 5);


            int irow = -1;
            // 设置标题
            irow += 1;
            row = instanceRow(sheet, irow, 30);
            SetValueRegion(irow, irow, 0, 8, irow, 0, sheet, row, styletitle, "金工费成本明细");



            String [] headerArr = {
                    "母件编码",  "母件名称",  "金工编码",  "名称",  "规格", "单耗","理论用量","单价","金额"
            };

            irow += 1;
            row = instanceRow(sheet, irow, 20);
            for (int i=0; i<headerArr.length; i++){
                SetValue(irow, i, sheet, row, styletxtheader, headerArr[i]);
            }
            CostSimReport query= JSON.parseObject(queryStr,CostSimReport.class);
            List<CostSimReport> list= costSimReportService.getMetalDetailList(query);

            for (CostSimReport s : list){
                irow += 1;
                row = instanceRow(sheet, irow, 20);

                int iCol = 0;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvCode());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvName());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvCodes());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvNames());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvStds());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getQty());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getQtys());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getIprice());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getImonery());


            }

            irow += 1;
            row = instanceRow(sheet, irow, 20);

            int iCol = 0;
            SetValue(irow, iCol, sheet, row, styletxtleft, "合计");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");


            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");




            //合计
            BigDecimal sumQty = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getQty()))
                    .map(CostSimReport::getQty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumQty);

            //合计
            BigDecimal sumQtys = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getQtys()))
                    .map(CostSimReport::getQtys)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumQtys);
            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            //合计
            BigDecimal sumAmount = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getImonery()))
                    .map(CostSimReport::getImonery)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumAmount);

            wb.write(out);
            byte[] fileNameByte = null;

            String pdtname = "金工费成本明细";

            fileNameByte = (pdtname + ".xls").getBytes("GBK");

            String filename = new String(fileNameByte, "ISO8859-1");

            byte[] bytes = out.toByteArray();

            response.setContentType("application/x-msdownload");
            response.setContentLength(bytes.length);
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.getOutputStream().write(bytes);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @RequestMapping(value = "/exportdetail1")
    public void exportdetail1(String queryStr,HttpServletResponse response){
        try{
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
            sheet.setColumnWidth(0, 40 * 21 * 5);
            sheet.setColumnWidth(1, 40 * 20 * 5);
            sheet.setColumnWidth(2, 40 * 20 * 5);
            sheet.setColumnWidth(3, 40 * 20 * 5);
            sheet.setColumnWidth(4, 40 * 20 * 5);
            sheet.setColumnWidth(5, 40 * 20 * 3);
            sheet.setColumnWidth(6, 40 * 20 * 5);
            sheet.setColumnWidth(7, 40 * 20 * 5);
            sheet.setColumnWidth(8, 40 * 20 * 5);
            sheet.setColumnWidth(9, 40 * 20 * 5);
            sheet.setColumnWidth(10, 40 * 20 * 5);
            sheet.setColumnWidth(11, 40 * 20 * 5);
            sheet.setColumnWidth(12, 40 * 20 * 5);
            int irow = -1;
            // 设置标题
            irow += 1;
            row = instanceRow(sheet, irow, 30);
            SetValueRegion(irow, irow, 0, 11, irow, 0, sheet, row, styletitle, "成本明细");



            String [] headerArr = {
                    "母件编码",  "母件名称",  "编码",  "名称",  "规格", "单位","类型","单耗","理论用量","加工单价","加工金额","材料单价","材料金额"
            };

            irow += 1;
            row = instanceRow(sheet, irow, 20);
            for (int i=0; i<headerArr.length; i++){
                SetValue(irow, i, sheet, row, styletxtheader, headerArr[i]);
            }
            CostSimReport query= JSON.parseObject(queryStr,CostSimReport.class);
            List<CostSimReport> list= costSimReportService.getDetailList(query);

            for (CostSimReport s : list){
                irow += 1;
                row = instanceRow(sheet, irow, 20);

                int iCol = 0;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvCode());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvName()+(s.getStatusId()==null?"":s.getStatusId()));

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvCodes());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvNames());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvStds());
                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getUnit());
                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getCinvdefine3());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getQty());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getQtys());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getCprice());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getCmonery());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getIprice());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getImonery());
            }

            irow += 1;
            row = instanceRow(sheet, irow, 20);

            int iCol = 0;
            SetValue(irow, iCol, sheet, row, styletxtleft, "合计");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");
            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");


            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");


            //合计
            BigDecimal sumQty = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getQty()))
                    .map(CostSimReport::getQty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumQty);

            //合计
            BigDecimal sumQtys = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getQtys()))
                    .map(CostSimReport::getQtys)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumQtys);
            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            //合计
            BigDecimal sumAmount = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getCmonery()))
                    .map(CostSimReport::getCmonery)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumAmount);


            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            //合计
            BigDecimal sumCAmount = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getImonery()))
                    .map(CostSimReport::getImonery)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumCAmount);


            wb.write(out);
            byte[] fileNameByte = null;

            String pdtname = "成本明细";

            fileNameByte = (pdtname + ".xls").getBytes("GBK");

            String filename = new String(fileNameByte, "ISO8859-1");

            byte[] bytes = out.toByteArray();

            response.setContentType("application/x-msdownload");
            response.setContentLength(bytes.length);
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.getOutputStream().write(bytes);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    //导出成本0单价数据
    @RequestMapping(value = "/exportdetailzero1")
    public void exportdetailzero1(String queryStr,HttpServletResponse response){
        try{
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
            sheet.setColumnWidth(0, 40 * 21 * 5);
            sheet.setColumnWidth(1, 40 * 20 * 5);
            sheet.setColumnWidth(2, 40 * 20 * 5);
            sheet.setColumnWidth(3, 40 * 20 * 5);
            sheet.setColumnWidth(4, 40 * 20 * 5);
            sheet.setColumnWidth(5, 40 * 20 * 3);
            sheet.setColumnWidth(6, 40 * 20 * 5);
            sheet.setColumnWidth(7, 40 * 20 * 5);
            sheet.setColumnWidth(8, 40 * 20 * 5);
            sheet.setColumnWidth(9, 40 * 20 * 5);
            sheet.setColumnWidth(10, 40 * 20 * 5);
            sheet.setColumnWidth(11, 40 * 20 * 5);
            sheet.setColumnWidth(12, 40 * 20 * 5);
            int irow = -1;
            // 设置标题
            irow += 1;
            row = instanceRow(sheet, irow, 30);
            SetValueRegion(irow, irow, 0, 11, irow, 0, sheet, row, styletitle, "成本明细0单价");



            String [] headerArr = {
                    "母件编码",  "母件名称",  "编码",  "名称",  "规格", "单位", "类型","单耗","理论用量","加工单价","加工金额","材料单价","材料金额"
            };

            irow += 1;
            row = instanceRow(sheet, irow, 20);
            for (int i=0; i<headerArr.length; i++){
                SetValue(irow, i, sheet, row, styletxtheader, headerArr[i]);
            }
            CostSimReport query= JSON.parseObject(queryStr,CostSimReport.class);
            List<CostSimReport> list= costSimReportService.getDetailList(query);

            for (CostSimReport s : list){
                if(s.getCprice().compareTo(BigDecimal.ZERO)==0||s.getIprice().compareTo(BigDecimal.ZERO)==0)
                {
                    irow += 1;
                    row = instanceRow(sheet, irow, 20);

                    int iCol = 0;
                    SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvCode());

                    iCol ++;
                    SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvName()+(s.getStatusId()==null?"":s.getStatusId()));

                    iCol ++;
                    SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvCodes());

                    iCol ++;
                    SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvNames());

                    iCol ++;
                    SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvStds());
                    iCol ++;
                    SetValue(irow, iCol, sheet, row, styletxtleft, s.getUnit());
                    iCol ++;
                    SetValue(irow, iCol, sheet, row, styletxtleft, s.getCinvdefine3());

                    iCol ++;
                    SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getQty());
                    iCol ++;
                    SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getQtys());

                    iCol ++;
                    SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getCprice());
                    iCol ++;
                    SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getCmonery());

                    iCol ++;
                    SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getIprice());
                    iCol ++;
                    SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getImonery());
                }

            }




            wb.write(out);
            byte[] fileNameByte = null;

            String pdtname = "成本明细0单价";

            fileNameByte = (pdtname + ".xls").getBytes("GBK");

            String filename = new String(fileNameByte, "ISO8859-1");

            byte[] bytes = out.toByteArray();

            response.setContentType("application/x-msdownload");
            response.setContentLength(bytes.length);
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.getOutputStream().write(bytes);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @RequestMapping(value = "/exportmaterial")
    public void exportmaterial(String queryStr,HttpServletResponse response){
        try{
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
            sheet.setColumnWidth(0, 40 * 21 * 5);
            sheet.setColumnWidth(1, 40 * 20 * 5);
            sheet.setColumnWidth(2, 40 * 20 * 5);
            sheet.setColumnWidth(3, 40 * 20 * 5);
            sheet.setColumnWidth(4, 40 * 20 * 5);
            sheet.setColumnWidth(5, 40 * 20 * 3);
            sheet.setColumnWidth(6, 40 * 20 * 5);
            sheet.setColumnWidth(7, 40 * 20 * 5);
            sheet.setColumnWidth(8, 40 * 20 * 5);
            sheet.setColumnWidth(9, 40 * 20 * 5);
            sheet.setColumnWidth(10, 40 * 20 * 5);
            sheet.setColumnWidth(11, 40 * 20 * 5);
            sheet.setColumnWidth(12, 40 * 20 * 5);
            int irow = -1;
            // 设置标题
            irow += 1;
            row = instanceRow(sheet, irow, 30);
            SetValueRegion(irow, irow, 0, 11, irow, 0, sheet, row, styletitle, "产品核算明细");



            String [] headerArr = {
                    "母件编码",  "母件名称",  "编码",  "名称",  "规格", "单位","类型","单耗","理论用量","加工单价","加工金额","采购单价","采购金额"
            };

            irow += 1;
            row = instanceRow(sheet, irow, 20);
            for (int i=0; i<headerArr.length; i++){
                SetValue(irow, i, sheet, row, styletxtheader, headerArr[i]);
            }
            CostSimReport query= JSON.parseObject(queryStr,CostSimReport.class);
            List<CostSimReport> list= costSimReportService.getDetailList(query);

            for (CostSimReport s : list){
                irow += 1;
                row = instanceRow(sheet, irow, 20);

                int iCol = 0;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvCode());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvName()+(s.getStatusId()==null?"":s.getStatusId()));

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvCodes());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvNames());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvStds());
                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getUnit());
                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getCinvdefine3());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getQty());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getQtys());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getCprice());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getCmonery());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getIprice());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getImonery());
            }

            irow += 1;
            row = instanceRow(sheet, irow, 20);

            int iCol = 0;
            SetValue(irow, iCol, sheet, row, styletxtleft, "合计");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");
            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");


            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");


            //合计
            BigDecimal sumQty = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getQty()))
                    .map(CostSimReport::getQty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumQty);

            //合计
            BigDecimal sumQtys = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getQtys()))
                    .map(CostSimReport::getQtys)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumQtys);
            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            //合计
            BigDecimal sumAmount = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getCmonery()))
                    .map(CostSimReport::getCmonery)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumAmount);


            iCol ++;
            SetValue(irow, iCol, sheet, row, stylecelltextcenterdecimal, "");

            //合计
            BigDecimal sumCAmount = list.stream()
                    .filter(g -> CustomStringUtils.isNotBlank(g.getImonery()))
                    .map(CostSimReport::getImonery)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            iCol ++;
            SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, sumCAmount);


            wb.write(out);
            byte[] fileNameByte = null;

            String pdtname = "产品核算明细";

            fileNameByte = (pdtname + ".xls").getBytes("GBK");

            String filename = new String(fileNameByte, "ISO8859-1");

            byte[] bytes = out.toByteArray();

            response.setContentType("application/x-msdownload");
            response.setContentLength(bytes.length);
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.getOutputStream().write(bytes);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
