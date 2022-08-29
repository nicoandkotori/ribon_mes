package com.web.common.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import java.text.SimpleDateFormat;

/**
 * Excel工具类
 *
 * @author lp
 */
public class ExcelUtil {
    /**
     * 单元格格式
     *
     * @param cell
     * @return
     */
    @SuppressWarnings({"static-access", "deprecation"})
    public static Object getValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                //日期格式的处理
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                }
                return cell.getNumericCellValue();
            //字符串
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            // 公式
            case HSSFCell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            // 空白
            case HSSFCell.CELL_TYPE_BLANK:
                return "";
            // 布尔取值
            case HSSFCell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue() + "";
            //错误类型
            case HSSFCell.CELL_TYPE_ERROR:
                return cell.getErrorCellValue() + "";
        }
        return "";
    }

    //处理公式
    public static Object getCellValueFormula(Cell cell, FormulaEvaluator formulaEvaluator) {
        if (cell == null || formulaEvaluator == null) {
            return null;
        }

        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            String cellFormula = cell.getCellFormula();
            int commaIdx = cellFormula.lastIndexOf(",");
            char c = cellFormula.charAt(commaIdx + 1);
            if(c == ')' || c == ' '){
                cellFormula = cellFormula.substring(0,commaIdx+1) + "FALSE)";
                cell.setCellFormula(cellFormula);
            }
            return formulaEvaluator.evaluate(cell).getNumberValue();
        }
        return getValue(cell);
    }
}
