package com.lsj.yldf.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;

import com.lsj.uinpay.mobilepay.RSAUtils;
/**
 * 银联代付报表
 * @author lishijie
 *
 */
public class PoiExcel {
	@Test
	public void exp() throws IOException{
		
		HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 7));
        sheet.setDefaultColumnWidth(20);
        HSSFRow rowZero = sheet.createRow(0);
        rowZero.createCell(0).setCellValue("代付模板");;
       
        HSSFCellStyle cellStyle = workbook.createCellStyle();  
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 
    	HSSFDataFormat format = workbook.createDataFormat();
    	cellStyle.setDataFormat(format.getFormat("@"));
    	java.text.DecimalFormat   df   = new   java.text.DecimalFormat("#.00");  
        
        HSSFRow rowOne = sheet.createRow(1);
        rowOne.createCell(0).setCellStyle(cellStyle);
        rowOne.createCell(1).setCellStyle(cellStyle);
        rowOne.createCell(2).setCellStyle(cellStyle);
        rowOne.createCell(3).setCellStyle(cellStyle);
        rowOne.getCell(0).setCellValue("模板ID号");
        rowOne.getCell(1).setCellValue("企业编码");
        rowOne.getCell(2).setCellValue("企业批次号");
        rowOne.getCell(3).setCellValue("");
        HSSFRow rowTwo = sheet.createRow(2);
        rowTwo.createCell(0).setCellStyle(cellStyle);
        rowTwo.createCell(1).setCellStyle(cellStyle);
        rowTwo.createCell(2).setCellStyle(cellStyle);
        rowTwo.createCell(3).setCellStyle(cellStyle);
        rowTwo.getCell(0).setCellValue("id:100");
        rowTwo.getCell(1).setCellValue("10000");
        rowTwo.getCell(2).setCellValue("10000151109002");
        rowTwo.getCell(3).setCellValue("");
        
        HSSFRow rowThree = sheet.createRow(3);
        rowThree.createCell(0).setCellStyle(cellStyle);
        rowThree.createCell(1).setCellStyle(cellStyle);
        rowThree.createCell(2).setCellStyle(cellStyle);
        rowThree.createCell(3).setCellStyle(cellStyle);
        rowThree.getCell(0).setCellValue("日期");
        rowThree.getCell(1).setCellValue("序号");
        rowThree.getCell(2).setCellValue("明细数目");
        rowThree.getCell(3).setCellValue("金额(单位:元)");
        HSSFRow rowFour = sheet.createRow(4);
        rowFour.createCell(0).setCellStyle(cellStyle);
        rowFour.createCell(1).setCellStyle(cellStyle);
        rowFour.createCell(2).setCellStyle(cellStyle);
        rowFour.createCell(3).setCellStyle(cellStyle);
        rowFour.getCell(0).setCellValue("2015-11-09");
        rowFour.getCell(1).setCellValue("001");
        rowFour.getCell(2).setCellValue("5");
        rowFour.getCell(3).setCellValue(df.format(Double.valueOf("186367.46")));
        
        
		HSSFCellStyle cellFiveStyle = workbook.createCellStyle();
		cellFiveStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		cellFiveStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		cellFiveStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		cellFiveStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		cellFiveStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		HSSFRow rowFive = sheet.createRow(5);
		for (int i = 0; i < 8; i++) {
			HSSFCell cell = rowFive.createCell(i);
			cell.setCellStyle(cellFiveStyle);
			cell.setCellValue("明细信息");
		}
		String[] tableHeader = { "明细序号","收款人开户行名称","开户行联行号","收款人银行账号","户名","金额(单位:元)","企业流水号","备注"};
		HSSFRow titleRow = sheet.createRow(6);
		
		for (int i = 0; i < tableHeader.length; i++) {
			HSSFCell titleCell = titleRow.createCell(i);
			titleCell.setCellStyle(cellStyle);
			titleCell.setCellValue(tableHeader[i]);
		}
		
		
		
		
		HSSFRow rowSeven = sheet.createRow(7);
		
		rowSeven.createCell(0).setCellStyle(cellStyle);
		rowSeven.createCell(1).setCellStyle(cellStyle);
		rowSeven.createCell(2).setCellStyle(cellStyle);
		rowSeven.createCell(3).setCellStyle(cellStyle);
		rowSeven.createCell(4).setCellStyle(cellStyle);
		rowSeven.createCell(5).setCellStyle(cellStyle);
		rowSeven.createCell(6).setCellStyle(cellStyle);
		rowSeven.createCell(7).setCellStyle(cellStyle);
		
		rowSeven.getCell(0).setCellStyle(cellStyle);
		rowSeven.getCell(0).setCellType(HSSFCell.CELL_TYPE_STRING);
		rowSeven.getCell(2).setCellStyle(cellStyle);
		rowSeven.getCell(2).setCellType(HSSFCell.CELL_TYPE_STRING);
		rowSeven.getCell(3).setCellStyle(cellStyle);
		rowSeven.getCell(3).setCellType(HSSFCell.CELL_TYPE_STRING);
		rowSeven.getCell(5).setCellStyle(cellStyle);
		rowSeven.getCell(5).setCellType(HSSFCell.CELL_TYPE_STRING);
		rowSeven.getCell(6).setCellStyle(cellStyle);
		rowSeven.getCell(6).setCellType(HSSFCell.CELL_TYPE_STRING);
		
	
		
		rowSeven.getCell(0).setCellValue("01");
		rowSeven.getCell(1).setCellValue("中国银行上海市真北路支行");
		rowSeven.getCell(2).setCellValue("104290075089");
		rowSeven.getCell(3).setCellValue("441669570560");
		rowSeven.getCell(4).setCellValue("上海芯富信息科技有限公司");
		rowSeven.getCell(5).setCellValue(df.format(Double.valueOf("45")));
		rowSeven.getCell(6).setCellValue("201511090001");
		rowSeven.getCell(7).setCellValue("");
		sheet.setGridsPrinted(true);
        
        
        String  path = this.getClass().getResource("/").getFile().toString();
        path = path.replace("test-classes", "classes")+"file/";
        File baseFile = new File(path);
        if(!baseFile.exists()){
            baseFile.mkdirs();
        }
        
        File file = new File(path+"yldf.xls");
        if(!file.exists()){
            file.createNewFile();
        }
        OutputStream opt = new FileOutputStream(file);
        workbook.write(opt);
        opt.flush();
        opt.close();
	}
}
