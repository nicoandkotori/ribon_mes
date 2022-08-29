package com.web.basicinfo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.*;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.basicinfo.entity.ComputationUnit;
import com.web.basicinfo.entity.Inventory;
import com.web.basicinfo.entity.InventoryClass;
import com.web.basicinfo.entity.Warehouse;
import com.web.basicinfo.service.IComputationUnitService;
import com.web.basicinfo.service.IInventoryClassService;
import com.web.basicinfo.service.IInventoryService;
import com.web.basicinfo.service.IWarehouseService;
import com.web.common.controller.BasicController;
import com.web.common.util.ExcelRead;
import com.web.common.util.FileUtils;
import com.web.mo.dto.BomDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by caihuan on 2021-11-13.
 */
@RestController
@RequestMapping(value = "/basicinfo/inventory")
public class InventoryController extends BasicController {
    @Autowired
    private IInventoryService inventoryService;





    @RequestMapping(value = "/getlistbypage")
    @ResponseBody

    public TableResult<Inventory> findPage(int page, int rows, String querystr){
        DbContextHolder.setDbType(DBTypeEnum.db2);
        TableResult<Inventory> result = new TableResult<>();
        IPage<Inventory> page1 = new Page<>(page, rows);
        IPage<Inventory> resultPage = new Page<>(page, rows);
        try{
            Inventory data = JSON.parseObject(querystr, Inventory.class);
            if(data == null){
                data = new Inventory();
            }
            resultPage =  inventoryService.getList(data,page1);

        }catch(Exception e){

            e.printStackTrace();
        }
        result.setRecords(resultPage.getTotal());
        result.setTotal((int)Math.ceil(resultPage.getTotal()/(double)resultPage.getSize()));
        result.setRows(resultPage.getRecords());
        return result;
    }




    @RequestMapping(value = "/getbyid")
    @ResponseBody
    public Inventory getById(String cinvcode) {
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            if(CustomStringUtils.isBlank(cinvcode)){
                throw new Exception("参数异常");
            }
            Inventory inventory = inventoryService.getInfoById(cinvcode);
            return inventory;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value="/getMenuList")
    @ResponseBody
    public List<Inventory> getMenuList() {
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            List<Inventory> menuList =  inventoryService.getMenuList();
            return menuList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 委外订单获取最新单价
     * @param cinvcode
     * @return
     */
    @RequestMapping(value = "/getprice")
    @ResponseBody
    public BigDecimal getPrice(String cinvcode) {
        try {
            DbContextHolder.setDbType(DBTypeEnum.db2);
            if(CustomStringUtils.isBlank(cinvcode)){
                throw new Exception("参数异常");
            }
            return inventoryService.getPrice(cinvcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //region  上传导入
    /**
     * 读取EXCEL
     */
    @RequestMapping(value = "/readexcel")
    public ResponseResult readExcel(MultipartFile file) {
        ResponseResult result = new ResponseResult();
        try {
            if (file == null || file.isEmpty() || CustomStringUtils.isBlank(file.getOriginalFilename())) {
                result.setSuccess(false);
                result.setMsg("文件不存在！");
                return result;
            }

            String filePath = System.getProperty("user.dir") + "/mes_web/target/classes/static/import/" + System.currentTimeMillis() + "/";
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            filePath += file.getOriginalFilename();
            File newFile = new File(filePath);
            file.transferTo(newFile);

            List<Inventory> list = getListFromExcel(newFile);
            FileUtils.deleteFile(filePath);
            result.setResult(list);
            result.setMsg("读取成功！");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private List<Inventory> getListFromExcel(File file) throws Exception {
        try {
            //开始读取行
            int startRow = 1;
            Map<String, Object> map = new ExcelRead().readExcel(file, startRow);
            if (map == null) {
                return null;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2=new SimpleDateFormat("yyyy/M/d");
            Calendar calendar = Calendar.getInstance();

            Integer totalRows = (Integer) map.get("totalRows");
            Integer totalCells = (Integer) map.get("totalCells");

            List<Inventory> list = new ArrayList<Inventory>();

            for (int r = startRow; r <= totalRows; r++) {
                Map<String, Object> rowMap = (Map<String, Object>) map.get("r" + r);
                Inventory p = new Inventory();
                for (int c = 0; c < totalCells; c++) {
                    //p.setId("temp" + String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
                    Object val = rowMap.get("c" + c);
                    String valStr = "";
                    BigDecimal valDecimal = BigDecimal.ZERO;
                    Integer valInt=null;
                    String className = val.getClass().getName();
                    if(className.equals("java.lang.Double")){
                        valDecimal = new BigDecimal((double) val);
                    }else{
                        if(className.equals("java.lang.Integer")){
                            valInt=  Integer.parseInt(val.toString());
                        }else{
                            valStr = (String) val;
                        }
                    }

                   /* if (className.equals("java.lang.String")) {
                        valStr = (String) val;
                    } else if (className.equals("java.lang.Double")) {
                        valDecimal = new BigDecimal((double) val);
                    }*/
                    //获取属性的列位置
                    int remainder = c % totalCells;
                    switch (remainder) {
                        case 0:
                            p.setCinvcode(valStr);
                            break;
                        case 1:
                            p.setCinvname(valStr);
                            break;
                        case 2:
                            p.setCinvccode(valStr);
                            break;
                        case 3:
                            if(valStr.length()==1){
                                valStr="0"+valStr;
                            }
                            p.setCgroupcode(valStr);
                            break;
                        case 4:
                            p.setCcomunitcode("01003");
                            break;
                        case 5:
                            if(valInt!=null){
                                p.setIgrouptype(Byte.valueOf(valInt.toString()));
                            }else{
                                //从string中 转过来（因为单元格是  字符格式化，所以读成了 字条）
                                p.setIgrouptype(Byte.valueOf(valStr));
                            }

                            break;
                        case 6:
                            if(valStr.contains("/")){
                                p.setDsdate(sdf2.parse(valStr));
                            }
                            if(valStr.contains("-")){
                                p.setDsdate(sdf.parse(valStr));
                            }

                            break;
                        case 7:
                            p.setCinvaddcode(valStr);
                            break;
                        case 8:
                            p.setCinvstd(valStr);
                            break;
                        case 13:
                            //是否委外
                            if(valStr.equals("0")){
                                p.setBproxyforeign(false);
                            }else{
                                p.setBproxyforeign(true);
                            }

                            break;
                        case 14:
                            if(valStr.equals("0")){
                                p.setBsale(false);
                            }else{
                                p.setBsale(true);
                            }
                            break;
                        case 15:
                            if(valStr.equals("0")){
                                p.setBpurchase(false);
                            }else{
                                p.setBpurchase(true);
                            }
                            break;
                        case 16:
                            //是否自制
                            if(valStr.equals("0")){
                                p.setBself(false);
                            }else{
                                p.setBself(true);
                            }
                            break;
                        case 17:
                            //是否生产耗用
                            if(valStr.equals("0")){
                                p.setBcomsume(false);
                            }else{
                                p.setBcomsume(true);
                            }
                            break;
                        case 113:
                            //
                            p.setCinvdefine9(valStr);

                            break;
                        case 114:
                            //
                            p.setCinvdefine7(valStr);

                            break;
                        default:
                            break;
                    }
                }
                p.setImportStatus("未导入");
                list.add(p);
            }


            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *  excel内容 保存
     * @param datas
     * @return
     */
    @RequestMapping(value = "/saveImport")
    public R saveImport(String datas){
        try{
            List<Inventory> list=new ArrayList<>();
            if(StringUtils.isNotBlank(datas)){
                list=JSON.parseArray(datas,Inventory.class);
            }
            if(list.size()<=0){
                return   R.error(301,"保存失败,没有导入的数据！");
            }

            //save
            DbContextHolder.setDbType(DBTypeEnum.db2);
            R r=   inventoryService.saveImport(list);
            return  r;

        }catch (Exception e){
            return R.error(201,"保存异常，请重试："+e.getMessage());
        }
    }
    //endregion
}
