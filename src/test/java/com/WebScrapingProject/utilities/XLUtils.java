package com.WebScrapingProject.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLUtils {
	

	public static FileInputStream fi;
	public static FileOutputStream fo;
	public static XSSFWorkbook wb;
	public static XSSFSheet ws;
	public static XSSFRow row;
	public static XSSFRow rowHeader;
	public static XSSFCell cell;
	public static XSSFCell cellHeader;
	public static XSSFCellStyle cs;
	public static XSSFCellStyle csHeader;
	public static XSSFCellStyle style;
	public static String path;
	public static XSSFFont font;

	/*Set Path of the Excel*/

	public XLUtils(String path)
	{
		this.path=path;
	}
	/*Method to get Row count*/

	public static int getRowCount(String xlsheet) throws IOException
	{
		fi=new FileInputStream(path);
		wb=new XSSFWorkbook(fi);
		ws=wb.getSheet(xlsheet);
		int rowcount=ws.getLastRowNum();
		wb.close();
		fi.close();
		return rowcount;		
	}

	/*Method to get Cell count*/

	public static int getCellCount(String xlsheet,int rownum) throws IOException
	{
		fi=new FileInputStream(path);
		wb=new XSSFWorkbook(fi);
		ws=wb.getSheet(xlsheet);
		row=ws.getRow(rownum);
		int cellcount=row.getLastCellNum();
		wb.close();
		fi.close();
		return cellcount;
	}

	/*Method to get Cell data from Excel*/

	public static String getCellData(String xlsheet,int rownum,int colnum) throws IOException
	{
		fi=new FileInputStream(path);
		wb=new XSSFWorkbook(fi);
		ws=wb.getSheet(xlsheet);
		row=ws.getRow(rownum);
		cell=row.getCell(colnum);
		String data;
		try 
		{
			data=cell.getStringCellValue();
		} catch (Exception e) 
		{
			data="";
		}
		wb.close();
		fi.close();
		return data;
	}

	/* Method used to set cell data for excel sheet*/

	public static void setCellData(String xlsheet,int rownum,int colnum,String data) throws IOException
	{

		File xlfile=new File(path);
		if(!xlfile.exists())
		{
			wb=new  XSSFWorkbook();
			fo=new FileOutputStream(path);
			wb.write(fo);
		}
		fi=new FileInputStream(path);
		wb=new XSSFWorkbook(fi);

		if (wb.getSheetIndex(xlsheet)==-1)
			wb.createSheet(xlsheet);
		ws=wb.getSheet(xlsheet);

		if(ws.getRow(rownum)==null)
			ws.createRow(rownum);
		row=ws.getRow(rownum);

		rowHeader=ws.getRow(0);
		cs=wb.createCellStyle();
		csHeader=wb.createCellStyle();
		font=wb.createFont();
		cell=row.createCell(colnum);

		if (rownum != 0)
		{	
			cs.getFont().setBold(true);
			cs.getFont().setFontHeightInPoints((short)20);
			cell.setCellStyle(csHeader);
			cs.setFont(font);
		}
		cs.setAlignment(HorizontalAlignment.LEFT);
		cs.setWrapText(true);
		cell.setCellStyle(cs);
		row.setHeightInPoints(colnum*ws.getDefaultRowHeightInPoints());
		ws.autoSizeColumn(colnum);

		if (rownum==1 || rownum==2 ||rownum==3)
		{
//			ws.getRow(1).setZeroHeight(true);
//			ws.getRow(2).setZeroHeight(true);
//			ws.getRow(3).setZeroHeight(true);
			}
		cell.setCellValue(data);
		fo=new FileOutputStream(xlfile);
		wb.write(fo);		
		wb.close();
		fi.close();
		fo.close();

}}
