package com.tibco.test;  
  
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
  
public class PoiDemo {  
  
    //表头  
    public static final String[] tableHeader = {"企业中文名","所属国家","企业英文名","2003年排名","2004年排名","2005年排名",  
        "2006年排名","2007年排名","主要业务","2003年营业额","2004年营业额","2005年营业额","2006年营业额","2007年营业额","企业编号","名次升降",  
        "图片","状况"};  
    //创建工作本  
    public static HSSFWorkbook demoWorkBook = new HSSFWorkbook();  
    //创建表  
    public static HSSFSheet demoSheet = demoWorkBook.createSheet("The World's 500 Enterprises");  
    //表头的单元格个数目  
    public static final short cellNumber = (short)tableHeader.length;  
    //数据库表的列数  
    public static final int columNumber = 18;  
    /** 
     * 创建表头 
     * @return 
     */  
    public static void createTableHeader()  
    {  
        HSSFHeader header = demoSheet.getHeader();  
        header.setCenter("世界五百强企业名次表");  
        HSSFRow headerRow = demoSheet.createRow((short) 0);  
        for(int i = 0;i < cellNumber;i++)  
        {  
            HSSFCell headerCell = headerRow.createCell((short) i);  
//            headerCell.setEncoding(HSSFCell.ENCODING_UTF_16);  
            headerCell.setCellValue(tableHeader[i]);  
        }  
    }  
    /** 
     * 创建行 
     * @param cells 
     * @param rowIndex 
     */  
    public static void createTableRow(List<String> cells,short rowIndex)  
    {  
        //创建第rowIndex行  
        HSSFRow row = demoSheet.createRow((short) rowIndex);  
        for(short i = 0;i < cells.size();i++)  
        {  
            //创建第i个单元格  
            HSSFCell cell = row.createCell((short) i);  
//            cell.setEncoding(HSSFCell.ENCODING_UTF_16);  
            cell.setCellValue(cells.get(i));  
        }  
    }  
      
    /** 
     * 创建整个Excel表 
     * @throws SQLException  
     * 
     */  
    public static void createExcelSheeet() throws SQLException  
    {  
        createTableHeader();  
//        ResultSet rs = SheetDataSource.selectAllDataFromDB();  
        int rowIndex = 1;  
//        while(rs.next())  
//        {  
//            List<String> list = new ArrayList<String>();  
//            for(int i = 1;i <= columNumber;i++)  
//            {  
//                list.add(rs.getString(i));  
//            }  
//            createTableRow(list,(short)rowIndex);  
//            rowIndex++;  
//        }  
    }  
    /** 
     * 导出表格 
     * @param sheet 
     * @param os 
     * @throws IOException 
     */  
    public void exportExcel(HSSFSheet sheet,OutputStream os) throws IOException  
    {  
        sheet.setGridsPrinted(true);  
        HSSFFooter footer = sheet.getFooter();  
        footer.setRight("Page " + HSSFFooter.page() + " of " +  
        HSSFFooter.numPages());  
        demoWorkBook.write(os);  
    }  
      
    public static void main(String[] args) {  
        String fileName = "D:\\世界五百强企业名次表.xls";  
         FileOutputStream fos = null;  
            try {  
                PoiDemo pd = new PoiDemo();  
                pd.createExcelSheeet();  
                fos = new FileOutputStream(fileName);  
                pd.exportExcel(demoSheet,fos);  
                JOptionPane.showMessageDialog(null, "表格已成功导出到 : "+fileName);  
            } catch (Exception e) {  
                JOptionPane.showMessageDialog(null, "表格导出出错，错误信息 ："+e+"\n错误原因可能是表格已经打开。");  
                e.printStackTrace();  
            } finally {  
                try {  
                    fos.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
    }  
}  