package com.web.mo.controller;

import com.common.util.*;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.common.controller.BasicController;
import com.web.common.util.ExcelRead;
import com.web.common.util.FileUtils;
import com.web.mo.dto.BomDTO;
import com.web.mo.service.IBomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by caihuan on 2021-04-13.
 */
@RestController
@RequestMapping(value = "/mo/bomimport")
public class BomImportController extends BasicController {

    @Autowired
    private IBomService bomService;


    /**
     * 读取EXCEL、并保存bom到U8
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

            String filePath = System.getProperty("user.dir") + "/mes_web/target/classes/static/import/" + new Date().getTime() + "/";
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            filePath += file.getOriginalFilename();
            File newFile = new File(filePath);
            file.transferTo(newFile);

            List<BomDTO> list = getListByExcel(newFile);
            FileUtils.deleteFile(filePath);

            DbContextHolder.setDbType(DBTypeEnum.db2);
            bomService.bomImportU8(list);
            DbContextHolder.setDbType(DBTypeEnum.db1);

            result.setResult(list);
            result.setMsg("导入成功！");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private List<BomDTO> getListByExcel(File file) throws Exception {
        try {
            //开始读取行
            int startRow = 1;
            Map<String, Object> map = new ExcelRead().readExcel(file, startRow);
            if (map == null) {
                return null;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();

            Integer totalRows = (Integer) map.get("totalRows");
            Integer totalCells = (Integer) map.get("totalCells");

            List<BomDTO> list = new ArrayList<BomDTO>();

            for (int r = startRow; r <= totalRows; r++) {
                Map<String, Object> rowMap = (Map<String, Object>) map.get("r" + r);
                BomDTO p = new BomDTO();
                for (int c = 0; c < totalCells; c++) {
                    p.setId("temp" + String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
                    Object val = rowMap.get("c" + c);
                    String valStr = "";
                    BigDecimal valDecimal = BigDecimal.ZERO;
                    String className = val.getClass().getName();
                    if (className.equals("java.lang.String")) {
                        valStr = (String) val;
                    } else if (className.equals("java.lang.Double")) {
                        valDecimal = new BigDecimal((double) val);
                    }

                    int remainder = c % totalCells;
                    switch (remainder) {
                        case 0:
                            p.setParentInvCode(CustomStringUtils.isBlank(valStr) ? valDecimal.toString() : valStr);

                            if("0".equals(p.getParentInvCode())){
                                p.setParentInvCode("");
                            }
                            break;
                        case 1:
                            p.setParentInvName(valStr);
                            break;
                        case 2:
                            p.setInvCode(CustomStringUtils.isBlank(valStr) ? valDecimal.toString() : valStr);

                            if("0".equals(p.getInvCode())){
                                p.setInvCode("");
                            }
                            break;
                        case 3:
                            p.setInvName(valStr);
                            break;
                        case 4:
                            p.setCwhcode(CustomStringUtils.isBlank(valStr) ? valDecimal.toString() : valStr);

                            if("0".equals(p.getCwhcode())){
                                p.setCwhcode("");
                            }
                            break;
                        case 5:
                            valStr = valStr.replaceAll(",", "");
                            p.setBaseQtyN(CustomStringUtils.isBlank(valDecimal) ? new BigDecimal(valStr) : valDecimal);
                            break;
                        case 6:
                            valStr = valStr.replaceAll(",", "");
                            p.setBaseQtyD(CustomStringUtils.isBlank(valDecimal) ? new BigDecimal(valStr) : valDecimal);
                            break;
                        default:
                            break;
                    }
                }
                list.add(p);
            }
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

}
