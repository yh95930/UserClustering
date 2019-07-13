package com.ar_kmeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.io.FileNotFoundException;  
import java.io.IOException;    
import jxl.Sheet;  
import jxl.Workbook;  
import jxl.read.biff.BiffException;


public class GetExcelInfo {
    public static void main(String[] args) {  
        GetExcelInfo obj = new GetExcelInfo();  
        // 此处为我创建Excel路径：E:/zhanhj/studysrc/jxl下  
        File file = new File("E:/数据集/Wine葡萄酒数据集.xls");  
        obj.readExcel(file);  
    }
    
    public ArrayList<float[]> dataSet=new ArrayList<float[]>();
    
    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象  
    public void readExcel(File file) {  
        try {  
            // 创建输入流，读取Excel  
            InputStream is = new FileInputStream(file.getAbsolutePath());  
            // jxl提供的Workbook类  
            Workbook wb = Workbook.getWorkbook(is);  
            // Excel的页签数量  
            int sheet_size = wb.getNumberOfSheets();  
            for (int index = 0; index < sheet_size; index++) {  
                // 每个页签创建一个Sheet对象  
                Sheet sheet = wb.getSheet(index);  
                // sheet.getRows()返回该页的总行数  
                for (int i = 0; i < sheet.getRows(); i++) {  
                	String cellinfo=null;
                	float[] b=new float[20];
                    // sheet.getColumns()返回该页的总列数  
                    for (int j = 0; j < sheet.getColumns(); j++) {  
                    	cellinfo = sheet.getCell(j, i).getContents();      
                    	try {
                    		float a = Float.parseFloat(cellinfo);
                    		b[j]=a;
                    		} catch (NumberFormatException e) {e.printStackTrace();}                    	
                    }                 
                    dataSet.add(b);                                       	
                }  
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (BiffException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}
