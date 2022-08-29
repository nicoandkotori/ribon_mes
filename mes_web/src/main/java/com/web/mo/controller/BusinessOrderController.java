package com.web.mo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.CustomStringUtils;
import com.common.util.ParamUtil;
import com.common.util.ResponseResult;
import com.common.util.TableResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.modules.security.util.SecurityUtil;
import com.util.EnumUtils;
import com.util.ResponseResultPDA;

import com.web.common.controller.BasicController;
import com.web.mo.entity.TestSoList;
import com.web.mo.entity.U8MpsNetdemand;
import com.web.mo.service.IMpsNetdemandService;
import com.web.mo.service.ITestSoListService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/mo/businessorder")
public class BusinessOrderController  extends BasicController {

    @Autowired
    private ITestSoListService testSoListService;
    @Autowired
    private IMpsNetdemandService mpsNetdemandService;

    @Value("${account.u8DB}")
    private String u8DB;

    //查询图号，订单号列表
    @RequestMapping(value = "/getdetaillist")
    @ResponseBody
    public TableResult<TestSoList> getDetailList(String querystr){
        TableResult<TestSoList> result = new TableResult<TestSoList>();
        try {

            TestSoList query= JSON.parseObject(querystr,TestSoList.class);
            if(query==null)
            {
                query=new TestSoList();
            }
            List<String> list=new ArrayList<>();
            //1锯料，2自制激光，3剪料，4冲件
            if(CustomStringUtils.isNotBlank(query.getProType1()) &&query.getProType1().equals(true))
            {
                list.add("锯料");
            }
            if(CustomStringUtils.isNotBlank(query.getProType2()) &&query.getProType2().equals(true))
            {
                list.add("自制激光");
            }
            if(CustomStringUtils.isNotBlank(query.getProType3()) &&query.getProType3().equals(true))
            {
                list.add("剪料");
            }
            if(CustomStringUtils.isNotBlank(query.getProType4()) &&query.getProType4().equals(true))
            {
                list.add("冲件");
            }
            if(CustomStringUtils.isNotBlank(query.getProType5()) &&query.getProType5().equals(true))
            {
                list.add("安装");
            }


            query.setListProType(list);
            //设置账套数据
            query.setU8DB(u8DB);

            List<TestSoList> scaList= testSoListService.getDetailList(query);

            result.setRows(scaList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //半成品抵扣单查询
    @RequestMapping(value = "/getdetaillistbywhcode")
    @ResponseBody
    public TableResult<TestSoList> getDetailListByWhcode(String querystr){
        TableResult<TestSoList> result = new TableResult<TestSoList>();
        try {

            TestSoList query= JSON.parseObject(querystr,TestSoList.class);
            if(query==null)
            {
                query=new TestSoList();
            }
            query.setWhCode("3");
            //设置账套数据
            query.setU8DB(u8DB);

            List<TestSoList> scaList= testSoListService.getDetailListByWhCode(query);
            for(TestSoList m:scaList)
            {
                if(m.getAdQty()==null)
                {
                    m.setAdQty(BigDecimal.ZERO);
                }
                if(m.getYqty()==null)
                {
                    m.setYqty(BigDecimal.ZERO);
                }
                m.setNowAdQty(m.getAdQty().subtract(m.getYqty()));
                if(m.getNowAdQty().compareTo(BigDecimal.ZERO)==-1)
                {
                    m.setNowAdQty(BigDecimal.ZERO);
                }
            }
            result.setRows(scaList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    //获取需要打印的数据,设备总清单
    @RequestMapping(value = "/getprintlistbydevice")
    @ResponseBody
    public List<TestSoList> getprintlistbydevice(String querystr){
        try {

            TestSoList query= JSON.parseObject(querystr,TestSoList.class);
            if(query==null)
            {
                query=new TestSoList();
            }

            //设置账套数据
            query.setU8DB(u8DB);
            List<TestSoList> list=testSoListService.getDetailList(query);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //获取需要打印的数据,设备总清单  多选
    @RequestMapping(value = "/getprintlistbydevicebatch")
    @ResponseBody
    public List<TestSoList> getprintlistbydevicebatch(String querystr){
        try {
            List<TestSoList> listAll=new ArrayList<>();
            List<TestSoList> list= JSON.parseArray(querystr,TestSoList.class);
            if(list!=null)
            {
                for(TestSoList testSoList:list)
                {
                    TestSoList query=new TestSoList();
                    query.setOrderNo(testSoList.getOrderNo());
                    query.setProductInvCode(testSoList.getProductInvCode());
                    //设置账套数据
                    query.setU8DB(u8DB);
                    List<TestSoList> list1=testSoListService.getDetailList(query);
                    if(list1!=null)
                    {
                        listAll.addAll(list1);
                    }
                }
            }



            return listAll;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }



    //获取需要打印的条数,设备总清单
    @RequestMapping(value = "/getdevicecountlist")
    @ResponseBody
    public List<TestSoList>  getdevicecountlist(String querystr){
        try {

            TestSoList query= JSON.parseObject(querystr,TestSoList.class);
            if(query==null)
            {
                query=new TestSoList();
            }

            //设置账套数据
            query.setU8DB(u8DB);
            List<TestSoList> list=testSoListService.getDetailList(query);
            if(list!=null&&list.size()>0)
            {
                List<TestSoList> newList = list.stream()
                        .collect(
                                Collectors.collectingAndThen(
                                        Collectors.toCollection(() -> new TreeSet<>(
                                                Comparator.comparing(o -> o.getOrderNo()+","+o.getProductInvCode())
                                        )), ArrayList::new
                                )
                        );
                return newList;
            }
            else
            {
                return null;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }




    //查询图号，订单号列表
    @RequestMapping(value = "/getprintlistbydeviceorder")
    @ResponseBody
    public List<TestSoList> getprintlistbydeviceorder(String querystr){
        try {

            TestSoList query= JSON.parseObject(querystr,TestSoList.class);
            if(query==null)
            {
                query=new TestSoList();
            }
            //设置账套数据
            List<String> listProType=new ArrayList<>();
            //1锯料，2自制激光，3剪料，4冲件
            if(CustomStringUtils.isNotBlank(query.getProType1()) &&query.getProType1().equals(true))
            {
                listProType.add("锯料");
            }
            if(CustomStringUtils.isNotBlank(query.getProType2()) &&query.getProType2().equals(true))
            {
                listProType.add("自制激光");
            }
            if(CustomStringUtils.isNotBlank(query.getProType3()) &&query.getProType3().equals(true))
            {
                listProType.add("剪料");
            }
            if(CustomStringUtils.isNotBlank(query.getProType4()) &&query.getProType4().equals(true))
            {
                listProType.add("冲件");
            }
            if(CustomStringUtils.isNotBlank(query.getProType5()) &&query.getProType5().equals(true))
            {
                listProType.add("安装");
            }

            query.setListProType(listProType);
            query.setU8DB(u8DB);
            List<TestSoList> list=testSoListService.getPrintListDevice(query);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    //获取需要打印的数据,生产任务单
    @RequestMapping(value = "/getprintproduct")
    @ResponseBody
    public List<U8MpsNetdemand> getprintproduct(String orderNo){
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            U8MpsNetdemand query= new U8MpsNetdemand();
            query.setPlancode(orderNo);
            //设置账套数据
            List<U8MpsNetdemand> list=mpsNetdemandService.getPrintProduct(query);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    //获取需要打印的数据,生产任务单  批量打印
    @RequestMapping(value = "/getprintproductbatch")
    @ResponseBody
    public List<U8MpsNetdemand> getprintproductbatch(String queryStr){
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            List<U8MpsNetdemand> listReturn=new ArrayList<>();
            List<U8MpsNetdemand> listQuery= JSON.parseArray(queryStr,U8MpsNetdemand.class);
            if(listQuery!=null)
            {
                //查询出批量的需要打印的数据
                for(U8MpsNetdemand m:listQuery)
                {
                    U8MpsNetdemand query= new U8MpsNetdemand();
                    query.setPlancode(m.getPlancode());
                    //设置账套数据
                    List<U8MpsNetdemand> list=mpsNetdemandService.getPrintProduct(query);
                    if(list!=null)
                    {
                        listReturn.addAll(list);
                    }
                }
            }
            //批量的任务单号和备注清空
            if(listReturn.size()>0)
            {
                listReturn.get(0).setPlancode("");
                listReturn.get(0).setCmemo("");
            }
            return listReturn;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }



    //获取需要打印的数据,生产任务单  批量打印(汇总)
    @RequestMapping(value = "/getprintproductbatchtotal")
    @ResponseBody
    public List<U8MpsNetdemand> getprintproductbatchtotal(String queryStr){
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            List<U8MpsNetdemand> listReturn=new ArrayList<>();
            List<U8MpsNetdemand> listQuery= JSON.parseArray(queryStr,U8MpsNetdemand.class);
            if(listQuery!=null)
            {
                //查询出批量的需要打印的数据
                for(U8MpsNetdemand m:listQuery)
                {
                    U8MpsNetdemand query= new U8MpsNetdemand();
                    query.setPlancode(m.getPlancode());
                    //设置账套数据
                    List<U8MpsNetdemand> list=mpsNetdemandService.getPrintProduct(query);
                    if(list!=null)
                    {
                        listReturn.addAll(list);
                    }
                }
            }
            //批量的任务单号和备注清空
            if(listReturn.size()>0)
            {
                listReturn.get(0).setPlancode("");
                listReturn.get(0).setCmemo("");
            }
            return listReturn;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    /**
     * 作废
     * @param mData
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public ResponseResult delete(String mData) {
        ResponseResult result = new ResponseResult();
        try{
            if (CustomStringUtils.isBlank(mData)) {
                throw new Exception("参数异常！");
            }
            TestSoList testSoList = JSON.parseObject(mData, TestSoList.class);

            result=testSoListService.delete(testSoList);
        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }



    /**
     * 审核出库
     * @param mData
     * @return
     */
    @RequestMapping(value = "/saveT6")
    @ResponseBody
    public ResponseResult saveT6(String mData) {
        ResponseResult result = new ResponseResult();
        try{

            if (CustomStringUtils.isBlank(mData)) {
                throw new Exception("参数异常！");
            }

            List<TestSoList> testSoList = JSON.parseArray(mData, TestSoList.class);
//            if(testSoList!=null)
//            {
//                for(TestSoList testSoList1:testSoList)
//                {
//                    testSoList1=testSoListService.getById(testSoList1.getId());
//                }
//            }
            DbContextHolder.setDbType(DBTypeEnum.db2);
            result=testSoListService.saveU8(testSoList);
        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }



    @RequestMapping(value = "/export")
    public void export(String queryStr, HttpServletResponse response){
        try{
            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
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
            sheet.setColumnWidth(0, 40 * 25 * 5);
            sheet.setColumnWidth(1, 40 * 20 * 5);
            sheet.setColumnWidth(2, 40 * 20 * 4);
            sheet.setColumnWidth(3, 40 * 20 * 4);
            sheet.setColumnWidth(4, 40 * 23 * 2);
            sheet.setColumnWidth(5, 40 * 20 * 2);
            sheet.setColumnWidth(6, 40 * 20 * 4);
            sheet.setColumnWidth(7, 40 * 20 * 4);
            sheet.setColumnWidth(8, 40 * 20 * 4);
            sheet.setColumnWidth(9, 40 * 20 * 4);
            sheet.setColumnWidth(10, 40 * 20 * 4);
            sheet.setColumnWidth(11, 40 * 20 * 7);
            sheet.setColumnWidth(12, 40 * 20 * 2);
            sheet.setColumnWidth(13, 40 * 20 * 2);
            sheet.setColumnWidth(14, 40 * 20 * 4);
            sheet.setColumnWidth(15, 40 * 20 * 4);
            sheet.setColumnWidth(16, 40 * 20 * 4);
            sheet.setColumnWidth(17, 40 * 20 * 4);
            int irow = -1;
            // 设置标题
//            irow += 1;
//            row = instanceRow(sheet, irow, 30);
//            SetValueRegion(irow, irow, 0, 15, irow, 0, sheet, row, styletitle, "设备总清单");



            String [] headerArr = {
                    "子件编码", "子件代码","子件名称",  "规格型号", "单位", "单耗", "理论用量", "库存量", "在途量", "可用量", "加工类型", "下料尺寸","长","宽", "材料名称", "材料规格", "部件名称", "生产备注"
            };

            irow += 1;
            row = instanceRow(sheet, irow, 20);
            for (int i=0; i<headerArr.length; i++){
                SetValue(irow, i, sheet, row, styletxtheader, headerArr[i]);
            }
            TestSoList query= JSON.parseObject(queryStr,TestSoList.class);
            if(query==null)
            {
                query=new TestSoList();
            }
            List<String> list=new ArrayList<>();
            //1锯料，2自制激光，3剪料，4冲件
            if(CustomStringUtils.isNotBlank(query.getProType1()) &&query.getProType1().equals(true))
            {
                list.add("锯料");
            }
            if(CustomStringUtils.isNotBlank(query.getProType2()) &&query.getProType2().equals(true))
            {
                list.add("自制激光");
            }
            if(CustomStringUtils.isNotBlank(query.getProType3()) &&query.getProType3().equals(true))
            {
                list.add("剪料");
            }
            if(CustomStringUtils.isNotBlank(query.getProType4()) &&query.getProType4().equals(true))
            {
                list.add("冲件");
            }
            if(CustomStringUtils.isNotBlank(query.getProType5()) &&query.getProType5().equals(true))
            {
                list.add("安装");
            }


            query.setListProType(list);
            //设置账套数据
            query.setU8DB(u8DB);

            List<TestSoList> scaList= testSoListService.getDetailList(query);



            for (TestSoList s : scaList){

                //分隔尺寸，得到长宽
                if(s.getMatSize()!=null)
                {
                    List<String> ss = Arrays.asList( s.getMatSize().split(","));
                    for(String q:ss)
                    {
                        if(q.contains("L")||q.contains("l"))
                        {
                            s.setLen(q.replace("L","").replace("l",""));
                        }
                        else if(q.contains("W")||q.contains("w"))
                        {
                            s.setWidth(q.replace("W","").replace("w",""));
                        }

                    }
                }



                irow += 1;
                row = instanceRow(sheet, irow, 20);

                int iCol = 0;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getPsCode()==null?"":s.getPsCode());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvAddCode()==null?"":s.getInvAddCode());


                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvName()==null?"":s.getInvName());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvStd()==null?"":s.getInvStd());


                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvUnit()==null?"":s.getInvUnit());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getQty());
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getQtys());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getCurQty());


                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getPoQty());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getKyQty());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getProType()==null?"":s.getProType());
                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getMatSize()==null?"":s.getMatSize());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getLen()==null?"":new BigDecimal(s.getLen()));
                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getWidth()==null?"":new BigDecimal(s.getWidth()) );
                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getMInvName()==null?"":s.getMInvName());
                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getMInvStd()==null?"":s.getMInvStd());
                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getPartName()==null?"":s.getPartName());
                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getMemo()==null?"":s.getMemo());

            }



            wb.write(out);
            byte[] fileNameByte = null;

            String pdtname = "设备总清单";

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
