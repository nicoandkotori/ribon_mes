package com.web.common.util;

import com.common.util.CustomStringUtils;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 读取Excel
 *
 * @author lp
 */
public class ExcelRead {
    public int totalRows = 0; //sheet中总行数
    public static int totalCells = 0; //每一行总单元格数

    /**
     * read the Excel .xlsx,.xls
     *
     * @param file jsp中的上传文件
     * @return
     * @throws IOException
     */
    public Map<String, Object> readExcel(File file,int startRow) throws IOException {
        if (file == null) {
            return null;
        }
        String filename = file.getName();
        if (CustomStringUtils.isBlank(filename)) {
            return null;
        }
        //判断是否为excel类型文件
        if(!filename.toLowerCase().endsWith(".xls")&&!filename.toLowerCase().endsWith(".xlsx"))
        {
            throw new IOException("文件不是excel类型");
        }
        InputStream input = FileUtils.openInputStream(file);
        Workbook wb = null;
        FormulaEvaluator formulaEvaluator = null;
        if (filename.toLowerCase().endsWith(".xls")) {
            // 创建文档
            wb = new HSSFWorkbook(input);
            formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) wb);
            return readXlsx(wb,input, startRow, formulaEvaluator);
        } else if (filename.toLowerCase().endsWith(".xlsx")) {
            wb = new XSSFWorkbook(input);
            formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) wb);
            return readXlsx(wb, input, startRow, formulaEvaluator);
        }
        return null;
    }

    /**
     * read the Excel 2010 .xlsx
     *
     * @param wb
     * @param formulaEvaluator
     * @return
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public Map<String,Object> readXlsx(Workbook wb, InputStream input, int startRow,
                                       FormulaEvaluator formulaEvaluator){
        // IO流读取文件
        Map<String, Object> outterMap = new HashMap<String, Object>();
        try {
            //读取sheet(页)
            int numberOfSheets = wb.getNumberOfSheets();
            if(numberOfSheets > 0){
                Sheet xssfSheet = wb.getSheetAt(wb.getActiveSheetIndex());
                if (xssfSheet == null) {
                    outterMap.put("totalRows", totalRows);
                    outterMap.put("totalCells", totalCells);
                    return outterMap;
                }
                totalRows = xssfSheet.getLastRowNum();
                Row headerRow = xssfSheet.getRow(0);//标题行
                if(headerRow != null){
                    totalCells = headerRow.getLastCellNum();
                }
                //读取Row,从第三行开始
                for (int r = startRow; r <= totalRows; r++) {
                    Row xssfRow = xssfSheet.getRow(r);
                    Map<String, Object> rowMap = new HashMap<String, Object>();
                    if (xssfRow != null) {
                        //读取列，从第一列开始
                        for (int c = 0; c < totalCells; c++) {
                            Cell cell = xssfRow.getCell(c);
                            if (cell == null) {
                                rowMap.put("c"+c,"");
                                continue;
                            }
                            rowMap.put("c" + c, ExcelUtil.getCellValueFormula(cell,formulaEvaluator));
                        }
                        outterMap.put("r"+r,rowMap);
                    }
                }
            }
            outterMap.put("totalRows", totalRows);
            outterMap.put("totalCells", totalCells);
            return outterMap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}