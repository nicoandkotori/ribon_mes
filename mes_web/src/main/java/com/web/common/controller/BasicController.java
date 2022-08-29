package com.web.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.common.util.CustomStringUtils;
import com.modules.security.PreSecurityUser;
import com.modules.security.util.JwtUtil;
import com.web.common.util.CodeModel;
import com.web.common.util.QR_Code;
import io.jsonwebtoken.Claims;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class BasicController {

    @Autowired
    private JwtUtil jwtUtil;
	/**
	 * 获取当前登录用户
	 * 不能以security中的为判断， 因为后面如果分布式， 通过token 以redis中的为准。
	 * @return
	 */
	protected PreSecurityUser getCurrentUser(HttpServletRequest request){
        try{
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null ) {
                return null;
            } else {

                PreSecurityUser user = jwtUtil.getUserFromToken(authHeader);

                /*List<String> userDep = this.getUserDepFromDatabase(user);
                user.setUserDeps(userDep);*/
                return user;
            }
        }catch (Exception e){
            return null;
        }

	}


	public String encoding(String src) {
		if (src == null)
			return "";
		StringBuilder result = new StringBuilder();
		if (src != null) {
			src = src.trim();
			for (int pos = 0; pos < src.length(); pos++) {
				char ch = src.charAt(pos);
				switch (ch) {
				case '＂':
					result.append("\"");
					break;
				default:
					result.append(src.charAt(pos));
					break;
				}
			}
		}
		return result.toString();
	}

	
	//创建行
	protected HSSFRow instanceRow(HSSFSheet sheet, int CurRow, int Height){
		  HSSFRow row = sheet.createRow((int) CurRow);
        row.setHeight((short)(15.625*Height));
        return row;
	}
	
	//区域设置单元格
	protected void SetValueRegion(int rowstart, int rowend, int colstart, int colend, int CurRow, int CurCol, HSSFSheet sheet, HSSFRow row , HSSFCellStyle style, Object val){
		CellRangeAddress cra=new CellRangeAddress(rowstart, rowend, colstart,colend);
	 	
		
		sheet.addMergedRegion(cra);
		//HSSFRow row = sheet.createRow((int) CurRow); 
		HSSFCell cell = row.createCell(CurCol);
		cell.setCellStyle(style);
		if(val!=null){
			Class cls=val.getClass();
			String clsname=cls.getName();
			//if(clsname.equals("java.lang.String")){java.math.BigDecimal
			//	cell.setCellValue(val.toString());
			//}
			//if(clsname.equals("java.util.Date")){
				//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
				//Date date = null; //初始化date   
				//try {  
				    //date = sdf.parse(str); //Mon Jan 14 00:00:00 CST 2013   
				
			//}
			cell.setCellValue(val.toString());
		}
	}

	//区域设置单元格
	protected void SetValueRegionGet(int rowstart, int rowend, int colstart, int colend, int CurRow, int CurCol, HSSFSheet sheet, HSSFRow row , HSSFCellStyle style, Object val){
		CellRangeAddress cra=new CellRangeAddress(rowstart, rowend, colstart,colend);


		sheet.addMergedRegion(cra);
		//HSSFRow row = sheet.createRow((int) CurRow);
		HSSFCell cell = row.getCell(CurCol);
		cell.setCellStyle(style);
		if(val!=null){
			Class cls=val.getClass();
			String clsname=cls.getName();
			//if(clsname.equals("java.lang.String")){java.math.BigDecimal
			//	cell.setCellValue(val.toString());
			//}
			//if(clsname.equals("java.util.Date")){
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//Date date = null; //初始化date
			//try {
			//date = sdf.parse(str); //Mon Jan 14 00:00:00 CST 2013

			//}
			cell.setCellValue(val.toString());
		}
	}
	
	//区域设置单元格   日期类型的 
	protected void SetValueRegion(int rowstart, int rowend, int colstart, int colend, int CurRow, int CurCol, HSSFSheet sheet, HSSFRow row , HSSFCellStyle style, Object val, String datefmt){
		CellRangeAddress cra=new CellRangeAddress(rowstart, rowend, colstart,colend);
	 	
		
		sheet.addMergedRegion(cra);
		//HSSFRow row = sheet.createRow((int) CurRow); 
		HSSFCell cell = row.createCell(CurCol);
		cell.setCellStyle(style);
		if(val!=null){
			Class cls=val.getClass();
			String clsname=cls.getName();
		
			if(clsname.equals("java.util.Date")||clsname.equals("java.lang.Long")){
				if(CustomStringUtils.isNotBlank(datefmt)){
                    SimpleDateFormat sdf = new SimpleDateFormat(datefmt);
                    String s = sdf.format(val);

                    cell.setCellValue(s);
                }
			}else{
				cell.setCellValue(val.toString());
			}
		}
	}
		
	
	//区域设置单元格
	protected void SetValueDecimalRegion(int rowstart, int rowend, int colstart, int colend, int CurRow, int CurCol, HSSFSheet sheet, HSSFRow row ,
                                         HSSFCellStyle style, Object val){
		CellRangeAddress cra=new CellRangeAddress(rowstart, rowend, colstart,colend);


		sheet.addMergedRegion(cra);

		SetCellValueDecimal(CurRow, CurCol, sheet, row, style, val);
	}
	//设置单元格
	protected void SetValue(int CurRow, int CurCol, HSSFSheet sheet, HSSFRow row , HSSFCellStyle style, Object val){
		 
       //HSSFRow row = sheet.createRow((int) CurRow); 
		if(val!=null){
			Class cls=val.getClass();
			String clsname=cls.getName();
	        HSSFCell cell = row.createCell(CurCol);
	        cell.setCellStyle(style);
	      
	       	cell.setCellValue(val.toString());
       }
	}
	
	protected void SetValue(int CurRow, int CurCol, HSSFSheet sheet, HSSFRow row , HSSFCellStyle style, Object val, String datefmt){
 	 
       //HSSFRow row = sheet.createRow((int) CurRow); 
		if(val!=null){
			HSSFCell cell = row.createCell(CurCol);
	        cell.setCellStyle(style);
	        
			Class cls=val.getClass();
			String clsname=cls.getName();
			if(clsname.equals("java.util.Date") || clsname.equals("java.lang.Long")){
				if(CustomStringUtils.isNotBlank(datefmt)){
                    SimpleDateFormat sdf = new SimpleDateFormat(datefmt);
                    String s = sdf.format(val);

                    cell.setCellValue(s);
                }
			}
			else{
				cell.setCellValue(val.toString());
	       	}
       }
	}

	protected void SetCellFormula(int CurRow, int CurCol, HSSFSheet sheet, HSSFRow row, HSSFCellStyle style, Object val) {
		HSSFCell cell = row.createCell(CurCol);
		cell.setCellStyle(style);
		if (val != null) {
			cell.setCellFormula(val.toString());
		}
	}

	protected void SetValueDecimal(int CurRow, int CurCol, HSSFSheet sheet, HSSFRow row , HSSFCellStyle style, Object val, int decimalLen){
		 
        //HSSFRow row = sheet.createRow((int) CurRow); 
		HSSFCell cell = row.createCell(CurCol);
        cell.setCellStyle(style);
		if(val!=null){
			Class cls=val.getClass();
			String clsname=cls.getName();
	         
	       	
	       	if(clsname.equals("java.math.BigDecimal")){
				if(decimalLen>0){
					String dt="0.";
					for(int i=0;i<decimalLen;i++){
						dt=dt+"0";
					}
					DecimalFormat df1 = new DecimalFormat(dt);
					String str = df1.format(val);
					cell.setCellValue(str);
				}
				else{
					BigDecimal bd = new BigDecimal(val.toString());
					cell.setCellValue(bd.intValue());
				}
				
			}
			else{
				cell.setCellValue(val.toString());
			}
       }
	}

	//数字形式
	protected void SetValueNumber(int CurRow, int CurCol, HSSFSheet sheet, HSSFRow row , HSSFCellStyle style, Object val){
		HSSFCell cell = row.createCell(CurCol);
		cell.setCellStyle(style);
		if(val!=null){
			Class cls=val.getClass();
			String clsname=cls.getName();
			if(clsname.equals("java.math.BigDecimal")) {
				BigDecimal bd = new BigDecimal(val.toString());
				cell.setCellValue(bd.doubleValue());
			}else{
				cell.setCellValue(val.toString());
			}
		}
	}

	protected void SetCellValueDecimal(int CurRow, int CurCol, HSSFSheet sheet, HSSFRow row , HSSFCellStyle style, Object val){

		//HSSFRow row = sheet.createRow((int) CurRow);
		HSSFCell cell = row.createCell(CurCol);
		cell.setCellStyle(style);
		if(val!=null){
			Class cls=val.getClass();
			String clsname=cls.getName();


			if(clsname.equals("java.math.BigDecimal")){


				BigDecimal bd = new BigDecimal(val.toString());
				cell.setCellValue(Double.valueOf(bd.toString()));



			}
			else{
				cell.setCellValue(val.toString());
			}
		}else{
			cell.setCellValue(0d);
		}
	}

	/**
	 * 生成二维码图片
	 * @param msg
	 * @param realPath
	 * @return BufferedImage
	 * @throws Exception
	 */
	protected BufferedImage addqrcode(String msg, String realPath) throws Exception {
		CodeModel info = new CodeModel();
		info.setContents(msg);
		info.setWidth(100);
		info.setHeight(100);
		info.setFontSize(10);
		info.setLogoFile(new File(realPath));
		info.setDesc(null);
		info.setDate(null);
		info.setLogoFile(null);
		QR_Code code = new QR_Code();
		code.createCodeImage(info, realPath);
		BufferedImage bi =  ImageIO.read(new FileInputStream(realPath));
		return bi;
	}

	/**
	 * 将二维码插入到表格
	 * @param msg
	 * @throws Exception
	 */
	protected void addQrcodeToExcel(HSSFWorkbook wb, HSSFSheet sheet, String msg, short col1, int row1, short col2, int row2, String realPath) throws Exception{
		// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

		// 放置二维码图片
//        String realPath = "D://zhengyang_barcode_temp/zhengyang_qrcode.png";
		BufferedImage bi =  addqrcode(msg, realPath);
		ByteArrayOutputStream os = new ByteArrayOutputStream();// 新建流。
		ImageIO.write(bi, "png", os); // 注：图片格式png，如果是jpg，图片会蒙上一层红色
		byte b[] = os.toByteArray();

		// anchor主要用于设置图片的属性
		// HSSFClientAnchor(int dx1, int dy1, int dx2, int dy2, short col1, int row1, short col2, int row2)
		// dx1左上角所在cell的偏移x坐标，一般可设0，范围值为:0~1023,超过1023就到右侧相邻的单元格里
		// dy1左上角所在cell的偏移y坐标，一般可设0，范围值为:0~256,超过256就到下方的单元格里
		// dx2右下角所在cell的偏移x坐标，一般可设0，范围值为:0~1023,超过1023就到右侧相邻的单元格里
		// dy2右下角所在cell的偏移y坐标，一般可设0，范围值为:0~256,超过256就到下方的单元格里
		// col1左上角所在列，row1左上角所在行
		// col2右下角所在列，row2右下角所在行
		HSSFClientAnchor anchor = new HSSFClientAnchor(10, 10, 0, 0, col1, row1, col2, row2);
		//插入图片
		patriarch.createPicture(anchor, wb.addPicture(b,HSSFWorkbook.PICTURE_TYPE_PNG));
		os.close();
	}
}
