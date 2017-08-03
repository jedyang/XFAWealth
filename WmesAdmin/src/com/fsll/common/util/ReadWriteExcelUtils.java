package com.fsll.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 读取、写入excel工具类
 * @author qgfeng
 * @date 2016-12-19
 */
public class ReadWriteExcelUtils {
	private static final String EXCEL_XLS = "xls";
	private static final String EXCEL_XLSX = "xlsx";
	
	public static void main(String[] args) {
/*		String filePath = "D:\\tomcat7\\apache-tomcat-7.0.65\\webapps\\wmesAdmin\\u\\corner\\201612\\member_admin.xls";
		File excelFile = new File(filePath);
		List<Map<String, String>> list;
		try {
			list = ReadWriteExcelUtils.read(excelFile);
			for(Map<String,String> map : list){
				for(Map.Entry<String, String> entry:map.entrySet()){    
				     System.out.print(entry.getKey()+":"+entry.getValue()+"   ");   
				}   
				 System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	
    /**
     * 解析excel文件 ，集合数组格式返回
     * @author qgfeng
     * @throws Exception 
     * @date 2016-12-19
     */
    public static List<Map<String,String>> read(File excelFile) throws Exception{
    	// 文件流  解析
    	checkExcelVaild(excelFile);
		FileInputStream is = new FileInputStream(excelFile); 
	    Workbook workbook = getWorkbok(is,excelFile); 
	    if(workbook==null){
	    	return null;
	    }
	    
	    List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	    int numberOfSheets = workbook.getNumberOfSheets();  // Sheet的数量  
         //遍历excel张数
         for(int i=0; i < numberOfSheets; i++){  
        	 Sheet sheet = workbook.getSheetAt(i);  
        	 Iterator<Row> rowIterator = sheet.iterator();  
        	 //首行标题
        	 Map<Integer,String> titleMap = getTitleMap(sheet.getRow(0));
        	//遍历行数据
        	 while(rowIterator.hasNext())   
                {  
        		 	Row row = rowIterator.next();
        		 	int rowNum = row.getRowNum();
	        		// 跳过第一行的目录 ,第一行为标题 
	                if(rowNum == 0)continue;    
                    //遍历列数据
                    Map<String,String> rowMap = new HashMap<String, String>();
                    short lastNum = row.getLastCellNum();
                    for(int j=0;j<lastNum;j++){
                    	Cell cell = row.getCell(j);
                    	String cellValue = getCellValue(cell);
                    	String columnName = titleMap.get(j);
                    	rowMap.put(columnName, cellValue);
                    }
                   if(!rowMap.isEmpty()){
                	   list.add(rowMap);
                   }
                }  
         }
    	return list;
    }
    
    /**
	 *  判断Excel的版本,获取Workbook 
	 * @author qgfeng
	 * @date 2016-12-19
	 */
	public static Workbook getWorkbok(InputStream in, File file)
			throws IOException {
		Workbook wb = null;
		if (file.getName().endsWith(EXCEL_XLS)) { // Excel xls格式 2003
			wb = new HSSFWorkbook(in);
		} else if (file.getName().endsWith(EXCEL_XLSX)) { // Excel xlsx格式 2007/2010
			wb = new XSSFWorkbook(in);
		}
		return wb;
	}
	
	 /** 
     * 判断文件是否是excel 
     * @throws Exception  
     */  
    public static void checkExcelVaild(File file) throws Exception{  
        if(!file.exists()){  
            throw new Exception("文件不存在");  
        }  
        if(!(file.isFile() && (file.getName().toLowerCase().endsWith(EXCEL_XLS) || file.getName().toLowerCase().endsWith(EXCEL_XLSX)))){  
            throw new Exception("文件不是Excel");  
        }  
    }  
    
    /**
     * 
     * @author qgfeng
     * @date 2016-12-19
     * @param row excel首行数据
     * @return
     */
    public static Map<Integer,String> getTitleMap(Row row){
    	Map<Integer,String> map = new HashMap<Integer, String>();
    	Iterator<Cell> cellIterator = row.cellIterator();  
    	int cellIndex = 0;
        while (cellIterator.hasNext())   
         {  
             Cell cell = cellIterator.next();  
             String cellValue = getCellValue(cell);
             map.put(cellIndex, cellValue);
           cellIndex++;
         } 
    	return map;
    }
    /**
     * 获取不同类型列数据
     * @author qgfeng
     * @date 2016-12-19
     * @param cell
     * @return
     */
	private static String getCellValue(Cell cell) {
		if(cell==null) return null;
    	int cellType = cell.getCellType();  
    	String cellValue = "";
        switch (cellType) {
        case Cell.CELL_TYPE_STRING:  // 文本  
        	cellValue = cell.getStringCellValue();
            break;
        case Cell.CELL_TYPE_NUMERIC: // 数字、日期  
        	cellValue = String.valueOf(cell.getNumericCellValue());
            break;
        case Cell.CELL_TYPE_BOOLEAN:// 布尔型  
        	cellValue = String.valueOf(cell.getBooleanCellValue());
            break;
        case Cell.CELL_TYPE_BLANK:// 空白  
        	cellValue = "";
            break;
        case Cell.CELL_TYPE_ERROR: // 错误  
        	cellValue = "";
        	break;
        }
        return cellValue;
    }
    
    

}
