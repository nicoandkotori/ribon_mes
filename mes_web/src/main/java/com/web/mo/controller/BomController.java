package com.web.mo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.CustomStringUtils;

import com.common.util.LayTableResult;
import com.common.util.ResponseResult;
import com.common.util.TableResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.modules.security.util.SecurityUtil;

import com.web.common.controller.BasicController;
import com.web.mo.dto.BomDTO;
import com.web.mo.service.IBomService;
import com.web.system.entity.User;
import com.web.system.service.IUserService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * Created by caihuan on 2021-04-13.
 */
@RestController
@RequestMapping(value = "/mo/bom")
public class BomController extends BasicController {

    @Autowired
    private IBomService bomService;

    /**
     * bom列表分页数据
     * @param page 当前页
     * @param rows 每页数据条数
     * @param querystr 查询参数
     * @return
     */
    @RequestMapping(value = "/getlistbypage")
    public TableResult<BomDTO> findPage(int page, int rows, String querystr) {
        DbContextHolder.setDbType(DBTypeEnum.db2);
        TableResult<BomDTO> result = new TableResult<>();
        IPage<BomDTO> page1 = new Page<>(page, rows);
        IPage<BomDTO> resultPage = new Page<>(page, rows);
        try{
            BomDTO data = JSON.parseObject(querystr, BomDTO.class);
            if(data == null){
                data = new BomDTO();
            }
            resultPage =  bomService.getList(data,page1);

        }catch(Exception e){

            e.printStackTrace();
        }
        result.setRecords(resultPage.getTotal());
        result.setTotal((int)Math.ceil(resultPage.getTotal()/(double)resultPage.getSize()));
        result.setRows(resultPage.getRecords());
        return result;

    }


    /*
       查询t6bom展开
      */
    @RequestMapping(value="/getu8bomlist")
    @ResponseBody
    public TableResult<BomDTO>getu8bomlist(String productInvCode) {
        TableResult<BomDTO> result = new TableResult<>();
        try{
            List<BomDTO> pg = this.bomService.getBomListByParent(productInvCode);
            result.setRows(pg);
        }catch(Exception e){
            result.setRows(null);
        }
        return result;
    }


    /*
     查询t6bom展开
    */
    @RequestMapping(value="/getu8childbomlist")
    @ResponseBody
    public List<BomDTO>getu8childbomlist(String parentInvCode) {
        List<BomDTO> list=new ArrayList<>();
        try{

            list = this.bomService.getInvChildListFirst(parentInvCode);
            return list;
        }catch(Exception e){
            return null;
        }

    }



    @RequestMapping(value = "/getwktypelist")
    @ResponseBody
    public List<BomDTO> getwktypelist() {

        try {
            String strType="[{\"cinvdefine3\":\"\"},{ \"cinvdefine3\":\"金工\" }, { \"cinvdefine3\":\"外协\" }, { \"cinvdefine3\":\"钣金委外\" }, { \"cinvdefine3\":\"剪料转委外\"}, { \"cinvdefine3\":\"冲件转委外\" }, { \"cinvdefine3\":\"外购\" }, { \"cinvdefine3\":\"剪料\" }, { \"cinvdefine3\":\"锯料\" }, { \"cinvdefine3\":\"冲件\" }, { \"cinvdefine3\":\"自制激光\" }, { \"cinvdefine3\":\"半成品\" }, { \"cinvdefine3\":\"安装\" }]";
            List<BomDTO> list = JSON.parseArray(strType, BomDTO.class);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    @RequestMapping(value = "/getwktypelistbykey")
    @ResponseBody
    public LayTableResult<BomDTO> getListByKey(int page, int limit, String keyword){
        LayTableResult<BomDTO> result = new LayTableResult<>();
        try{

            String strType="[{\"cinvdefine3\":\"\"},{ \"cinvdefine3\":\"金工\" }, { \"cinvdefine3\":\"外协\" }, { \"cinvdefine3\":\"外购\" }, { \"cinvdefine3\":\"剪料\" }, { \"cinvdefine3\":\"锯料\" }, { \"cinvdefine3\":\"冲件\" }, { \"cinvdefine3\":\"自制激光\" }, { \"cinvdefine3\":\"半成品\" }, { \"cinvdefine3\":\"原材料\"}, { \"cinvdefine3\":\"安装\"} ]";
            List<BomDTO> list = JSON.parseArray(strType, BomDTO.class);
            result.setCount(list.size());
            result.setData(list);
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("加载数据出错！");
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 保存更新T6bom方法
     * @param mData
     * @return
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public ResponseResult save(String mData,String mDatas) {
        ResponseResult result = new ResponseResult();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            if (CustomStringUtils.isBlank(mData)) {
                throw new Exception("参数异常！");
            }

            BomDTO main = JSON.parseObject(mData, BomDTO.class);
            List<BomDTO> detailList = JSON.parseArray(mDatas, BomDTO.class);
            if (detailList==null||detailList.size() == 0) {
                result.setSuccess(false);
                result.setMsg("明细表数据不能为空！");
                return result;
            }
            result=bomService.save(main, detailList);

        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    /**
     * 明细表数据删除
     * @param
     * @return
     * @throws Exception
     */

    @RequestMapping("/delete")
    public ResponseResult delete(Integer opComponentId) throws Exception {

        ResponseResult result = new ResponseResult();
        try{

            DbContextHolder.setDbType(DBTypeEnum.db2);
            bomService.delete(opComponentId);
            result.setMsg("删除成功！");


        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }





    @RequestMapping(value = "/export")
    public void export( String invCode,HttpServletResponse response){
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
            sheet.setColumnWidth(0, 40 * 25 * 2);
            sheet.setColumnWidth(1, 40 * 20 * 4);
            sheet.setColumnWidth(2, 40 * 20 * 8);
            sheet.setColumnWidth(3, 40 * 20 * 3);
            sheet.setColumnWidth(4, 40 * 23 * 3);

            int irow = -1;


            String [] headerArr = {
                    "序号", "子件编码","子件名称",  "基本用量", "加工类型"
            };

            irow += 1;
            row = instanceRow(sheet, irow, 20);
            for (int i=0; i<headerArr.length; i++){
                SetValue(irow, i, sheet, row, styletxtheader, headerArr[i]);
            }

            List<BomDTO> list=bomService.selectParentMenuList(invCode);
            List<BomDTO> list1=bomService.selectByMenu(invCode);


            for (BomDTO s : list){
                irow += 1;
                row = instanceRow(sheet, irow, 20);

                int iCol = 0;
                SetValue(irow, iCol, sheet, row, styletxtleft, "");

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvCode());


                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvName());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, BigDecimal.ONE);

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getCinvdefine3()==null?"": s.getCinvdefine3());
            }
            int nowRow=1;
            for (BomDTO s : list1){
                irow += 1;
                row = instanceRow(sheet, irow, 20);

                int iCol = 0;
                SetValue(irow, iCol, sheet, row, styletxtleft, nowRow);

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvCode());


                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getInvName());

                iCol ++;
                SetCellValueDecimal(irow, iCol, sheet, row, stylecelltextcenterdecimal, s.getBaseQtyN());

                iCol ++;
                SetValue(irow, iCol, sheet, row, styletxtleft, s.getCinvdefine3()==null?"": s.getCinvdefine3());
                nowRow++;
            }

            wb.write(out);
            byte[] fileNameByte = null;

            String pdtname = "Bom展开一级列表";

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
