/*
 * Excel.java
 * Jun 6, 2013
 * com.tibco.test
 * AngularJS
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.util;
/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Workbook;

/** 
 * 生成导出Excel文件对象
* 
*  @author  John.Zhu
* 
 */ 
 public   class  XLSExport  {

    //  定制日期格式 
      private   static  String DATE_FORMAT  =   "yyyy-mm-dd HH:mm:ss" ;  //  "m/d/yy h:mm"

    //  定制浮点数格式 
      private   static  String NUMBER_FORMAT  =   "#,##0.00" ;
      
    private  SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
    private  String xlsFileName;

    private  HSSFWorkbook workbook;

    private  HSSFSheet sheet;

    private  HSSFRow row;

    public Workbook getWorkBook(){
    	return workbook;
    }
    
    /** 
     * 初始化Excel
    * 
    *  @param  fileName
    *            导出文件名
     */ 
      public  XLSExport(String fileName)  {
        this .xlsFileName  =  fileName;
        this .workbook  =   new  HSSFWorkbook();
        this .sheet  =  workbook.createSheet();
   }    
      public  XLSExport()  {
//       this .xlsFileName  =  fileName;
       this .workbook  =   new  HSSFWorkbook();
       this .sheet  =  workbook.createSheet();
  } 
      public  XLSExport(String fileName,String sheetName,String headerName)  {
        this .xlsFileName  =  fileName;
        this .workbook  =   new  HSSFWorkbook();
        if(sheetName!=null)
        	this .sheet  =  workbook.createSheet(sheetName);
        else
        	this.sheet = workbook.createSheet();
        
        this.sheet.getHeader().setLeft("dssd");
        this.sheet.getHeader().setCenter(headerName);
        this.sheet.getHeader().setRight("dsasdsdsd");
   } 
      
 
     /** 
     * 导出Excel文件
    * 
    *  @throws  Exception
     */ 
      public   void  exportXLS()  throws  Exception  {
        try   {
           FileOutputStream fOut  =   new  FileOutputStream(xlsFileName);
           workbook.write(fOut);
           fOut.flush();
           fOut.close();
       }   catch  (FileNotFoundException e)  {
            throw   new  Exception( " 生成导出Excel文件出错! " , e);
       }   catch  (IOException e)  {
            throw   new  Exception( " 写入Excel文件出错! " , e);
       } 
 
   } 
 
     /** 
     * 增加一行
    * 
    *  @param  index
    *            行号
     */ 
      public   void  createRow( int  index)  {
        this .row  =   this .sheet.createRow(index);
   } 
 
     /** 
     * 设置单元格
    * 
    *  @param  index
    *            列号
    *  @param  value
    *            单元格填充值
     */ 
      public   void  setCell( int  index, String value)  {
       HSSFCell cell  =   this .row.createCell( index);
       cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//       cell.setEncoding(XLS_ENCODING);
       cell.setCellValue(value);
   } 
 
     /** 
     * 设置单元格
    * 
    *  @param  index
    *            列号
    *  @param  value
    *            单元格填充值
     */ 
      public   void  setCell( int  index, Calendar value)  {
       HSSFCell cell  =   this .row.createCell(  index);
//       cell.ENCODING_UTF_16  .setEncoding(XLS_ENCODING);
       cell.setCellValue(value.getTime());
       HSSFCellStyle cellStyle  =  workbook.createCellStyle();  //  建立新的cell样式 
         cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT));  //  设置cell样式为定制的日期格式 
         cell.setCellStyle(cellStyle);  //  设置该cell日期的显示格式 
     } 
 
  
      
      public   void  setCell( int  index, Date date )  {
          HSSFCell cell  =   this .row.createCell(  index);
          cell.setCellValue(format.format(date));
//          HSSFCellStyle cellStyle  =  workbook.createCellStyle();  //  建立新的cell样式 
//            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT));  //  设置cell样式为定制的日期格式 
//            cell.setCellStyle(cellStyle);  //  设置该cell日期的显示格式 
        } 
      
     /** 
     * 设置单元格
    * 
    *  @param  index
    *            列号
    *  @param  value
    *            单元格填充值
     */ 
      public   void  setCell( int  index,  int  value)  {
       HSSFCell cell  =   this .row.createCell( index);
       cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
       cell.setCellValue(value);
   } 
 
     /** 
     * 设置单元格
    * 
    *  @param  index
    *            列号
    *  @param  value
    *            单元格填充值
     */ 
      public   void  setCell( int  index,  double  value)  {
       HSSFCell cell  =   this .row.createCell(index);
       cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
       cell.setCellValue(value);
       HSSFCellStyle cellStyle  =  workbook.createCellStyle();  //  建立新的cell样式 
         HSSFDataFormat format  =  workbook.createDataFormat();
       cellStyle.setDataFormat(format.getFormat(NUMBER_FORMAT));  //  设置cell样式为定制的浮点数格式 
         cell.setCellStyle(cellStyle);  //  设置该cell浮点数的显示格式 
     } 
      public static void main(String args[]){
    	        System.out.println( " 开始导出Excel文件 ");
    	        XLSExport e  =   new  XLSExport( "d:/test.xls","大家好！","头头。。。。头啊");
    	       
    	        e.createRow( 0 );
    	        e.setCell( 0 ,  " 编号 " );
    	        e.setCell( 1 ,  " 名称 " );
    	        e.setCell( 2 ,  " 日期 " );
    	        e.setCell( 3 ,  " 金额 " );
    	        
    	        e.createRow( 1 );
    	        
    	        e.setCell( 0 ,  1 );
    	        e.setCell( 1 ,  " 工商银行 " );
    	        e.setCell( 2 , new Date());
    	        e.setCell( 3 ,  111123.99 );
    	        e.createRow( 2 );
    	        
    	        e.setCell( 0 ,  2 );
    	        e.setCell( 1 ,  " 招商银行 " );
    	        e.setCell( 2 , Calendar.getInstance());
    	        e.setCell( 3 ,  222456.88 );
    	        
    	        		
    	         try   {
    	            e.exportXLS();
    	            System.out.println( " 导出Excel文件[成功] " );
    	        }   catch  (Exception e1)  {
    	            System.out.println( " 导出Excel文件[失败] " );
    	            e1.printStackTrace();
    	        } 
      }
 
}