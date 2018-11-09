package com.fh.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ExcelXlsx {
	public static void main(String[] args) throws IOException {
//		String path = "d:/";
//		String fileName = "test";    --写入项目本地物理磁盘时用
//		String fileType = "xlsx";
//		writer(path, fileName, fileType);
//		read(path, fileName, fileType);
	}

	/**
	 * 随行付写出（浏览器）
	 * @param model
	 * @throws IOException
	 */
	public void writer(Map<String, Object> model,String filename,HttpServletResponse response,String path)
			throws IOException {
		
		// 创建工作文档对象
		Workbook wb = new XSSFWorkbook();
		// 创建sheet对象
		Sheet sheet1 = (Sheet) wb.createSheet("sheet1");
		
		Cell cell;
		
		sheet1.autoSizeColumn(1);
		List<String> titles = (List<String>) model.get("titles");
		int len = titles.size();
		// 循环写入行数据
	 
		Row row1 = (Row) sheet1.createRow(0);
		//设置标题
		for(int j=0; j<len; j++){ 
			String title = titles.get(j);
			cell = row1.createCell(j);
			cell.setCellType(cell.CELL_TYPE_STRING);
			cell.setCellValue(title);
		}
 
	 
		
		List<PageData> varList = (List<PageData>) model.get("varList");
		int varCount = varList.size();
		// 循环写入行数据
		for(int i=0; i<varCount; i++){
			Row row = (Row) sheet1.createRow(i+1);
			PageData vpd = varList.get(i);
			// 循环写入列数据
			for (int j = 0; j < len; j++) {
				String varstr = vpd.getString("var"+(j+1)) != null ? vpd.getString("var"+(j+1)) : "";
				if(j==0)
					sheet1.setColumnWidth(j,varstr.toString().length() * 400);
				else
					sheet1.setColumnWidth(j,varstr.toString().length() * 800);
				cell = row.createCell(j);
				cell.setCellType(cell.CELL_TYPE_STRING);
				cell.setCellValue(varstr);
			}
		}
		
		// 创建文件流
		OutputStream stream = new FileOutputStream(path);
		// 写入数据
		wb.write(stream);
		// 关闭文件流
		stream.close();
		
	      ByteArrayOutputStream os = new ByteArrayOutputStream();
	      wb.write(os);
	      byte[] content = os.toByteArray();
	      InputStream is = new ByteArrayInputStream(content);
	      // 设置response参数，可以打开下载页面
	      response.reset();
	      response.setContentType("application/vnd.ms-excel;charset=utf-8");
	      response.setHeader("Content-Disposition", "attachment;filename="
	          + new String((filename + ".xlsx").getBytes(), "iso-8859-1"));
	      ServletOutputStream out = response.getOutputStream();
	      BufferedInputStream bis = null;
	      BufferedOutputStream bos = null;

	      try {
	        bis = new BufferedInputStream(is);
	        bos = new BufferedOutputStream(out);
	        byte[] buff = new byte[2048];
	        int bytesRead;
	        // Simple read/write loop.
	        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	          bos.write(buff, 0, bytesRead);
	        }
	      } catch (Exception e) {
	        // TODO: handle exception
	        e.printStackTrace();
	      } finally {
	        if (bis != null)
	          bis.close();
	        if (bos != null)
	          bos.close();
	      }
	}

	
	
	
	/**
	 * 易宝写出（浏览器）
	 * @param model
	 * @throws IOException
	 */
	public void writeryb(Map<String, Object> model,String filename,HttpServletResponse response,String path)
			throws IOException {
		
		
		HSSFWorkbook workbook = new HSSFWorkbook();  
		HSSFSheet sheet1 = workbook.createSheet("批量上传数据（CSV）");  
		HSSFRow row = sheet1.createRow(0);  //从第1行开始
		HSSFCell cell = row.createCell((short)0);  //从第1列开始
		
		HSSFCellStyle headerStyle = workbook.createCellStyle(); //标题样式
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont headerFont = workbook.createFont();	//标题字体
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short)11);
		headerStyle.setFont(headerFont);
		short width = 20,height=25*20;
		sheet1.setDefaultColumnWidth(width);
		
		
			List<String> titles = (List<String>) model.get("titles");
			int len = titles.size();
//			String type=(String)model.get("type");
			if("tl".equals((String)model.get("type"))) {
				HSSFRow row0 = sheet1.createRow(0);//从第1行开始
				List<String> heads = (List<String>) model.get("head");
				//设置头部
				for(int j=0; j<heads.size(); j++){
					cell=row0.createCell((short)j);//从第1列开始
					String head = heads.get(j);
					cell.setCellType(cell.CELL_TYPE_STRING);
					cell.setCellValue(head);
				}
				
				
				
				
			}else {
				sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
				cell.setCellValue("batchNo(批次号):"+"TX"+System.currentTimeMillis());
			}
			
			
//			sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
//			cell.setCellValue("batchNo(批次号):"+"TX"+System.currentTimeMillis());
			HSSFCell cell1;
			HSSFRow row1 = sheet1.createRow(1);//从第2行开始
			//设置标题
			for(int j=0; j<len; j++){
				cell1=row1.createCell((short)j);//从第1列开始
				String title = titles.get(j);
				cell1.setCellType(cell1.CELL_TYPE_STRING);
				cell1.setCellValue(title);
			}
			
			HSSFCell cell2;
			HSSFRow row2;
			List<PageData> varList = (List<PageData>) model.get("varList");
			int varCount = varList.size();
			// 循环写入行数据
			for(int i=0; i<varCount; i++){
				row2=sheet1.createRow(i+2);//从第3行开始
				PageData vpd = varList.get(i);
				// 循环写入列数据
				for (int j = 0; j < len; j++) {
					String varstr = vpd.getString("var"+(j+1)) ;
					if(varstr==null || varstr.isEmpty() ){
						
						if(j==4){
							varstr = "省份";
						}else
						if(j==5){
							varstr = "城市";
						}else{
							varstr = " ";
						}
					}
					cell2=row2.createCell((short)j);
					cell2.setCellType(cell2.CELL_TYPE_STRING);
					cell2.setCellValue(varstr);
				}
			}
		// 创建文件流
		OutputStream stream = new FileOutputStream(path);
		// 写入数据
		workbook.write(stream);
		// 关闭文件流
		stream.close();
		
		
	      ByteArrayOutputStream os = new ByteArrayOutputStream();
	      workbook.write(os);
	      byte[] content = os.toByteArray();
	      InputStream is = new ByteArrayInputStream(content);
	      // 设置response参数，可以打开下载页面
	      response.reset();
	      response.setContentType("application/vnd.ms-excel;charset=utf-8");
	      response.setHeader("Content-Disposition", "attachment;filename="
	          + new String((filename + ".xls").getBytes(), "iso-8859-1"));
	      ServletOutputStream out = response.getOutputStream();
	      BufferedInputStream bis = null;
	      BufferedOutputStream bos = null;

	      try {
	        bis = new BufferedInputStream(is);
	        bos = new BufferedOutputStream(out);
	        byte[] buff = new byte[2048];
	        int bytesRead;
	        // Simple read/write loop.
	        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	          bos.write(buff, 0, bytesRead);
	        }
	      } catch (Exception e) {
	        // TODO: handle exception
	        e.printStackTrace();
	      } finally {
	        if (bis != null)
	          bis.close();
	        if (bos != null)
	          bos.close();
	      }
	}

	
	
	/**
	 * 写入
	 * @param path
	 * @param fileName
	 * @param fileType
	 * @throws IOException
	 */
	public static void read(String path, String fileName, String fileType)
			throws IOException {
		InputStream stream = new FileInputStream(path + fileName + "."
				+ fileType);
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(stream);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(stream);
		} else {
			System.out.println("您输入的excel格式不正确");
		}
		Sheet sheet1 = wb.getSheetAt(0);
		for (Row row : sheet1) {
			for (Cell cell : row) {
				System.out.print(cell.getStringCellValue() + "  ");
			}
			System.out.println();
		}
	}
}
