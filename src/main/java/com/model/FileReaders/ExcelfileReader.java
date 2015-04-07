package com.model.FileReaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class ExcelfileReader { 
	String fileName;
	public String getName() {
		return fileName;
	}

	public void setName(String name) {
		this.fileName = name;
	}
	
	public static void main(String[] args) {
	    try {
	        FileInputStream file = new FileInputStream(new File("C:\\files\\airline.xlsx"));
	
	        //Create Workbook instance holding reference to .xlsx file]
	        XSSFWorkbook workbook = new XSSFWorkbook(file);
	
	        //Get first/desired sheet from the workbook
	        XSSFSheet sheet = workbook.getSheetAt(0);
	
	        //Iterate through each rows one by one
	        java.util.Iterator<Row> rowIterator = sheet.iterator();
	        while (rowIterator.hasNext())
	        {
	            Row row = rowIterator.next();
	            //For each row, iterate through all the columns
	            java.util.Iterator<Cell> cellIterator = row.cellIterator();
	
	            while (cellIterator.hasNext()) 
	            {
	                Cell cell = cellIterator.next();
	                //Check the cell type and format accordingly
	                switch (cell.getCellType()) 
	                {
	                    case Cell.CELL_TYPE_NUMERIC:
	                        System.out.print(cell.getNumericCellValue() + "\t");
	                        break;
	                    case Cell.CELL_TYPE_STRING:
	                        System.out.print(cell.getRichStringCellValue() + "\t");
	                        break;
	                }
	            }
	            System.out.println("");
	        }
	        file.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public JavaRDD<String> readFile(String path,JavaSparkContext sc) throws IOException {
		 String text = "";
		 FileInputStream file = new FileInputStream(path);
			
	        //Create Workbook instance holding reference to .xlsx file]
	        XSSFWorkbook workbook = new XSSFWorkbook(file);
	
	        //Get first/desired sheet from the workbook
	        XSSFSheet sheet = workbook.getSheetAt(0);
	
	        //Iterate through each rows one by one
	        java.util.Iterator<Row> rowIterator = sheet.iterator();
	        List<String> content = new ArrayList();
	        while (rowIterator.hasNext())
	        {
	            Row row = rowIterator.next();
	            //For each row, iterate through all the columns
	            java.util.Iterator<Cell> cellIterator = row.cellIterator();
	
	            while (cellIterator.hasNext()) 
	            {
	                Cell cell = cellIterator.next();
	                //Check the cell type and format accordingly
	                switch (cell.getCellType()) 
	                {
	                    case Cell.CELL_TYPE_NUMERIC:
	                        //System.out.print(cell.getNumericCellValue() + "\t");
	                    	text+=cell.getNumericCellValue() + "\t";
	                    	content.add(cell.getNumericCellValue()+"\t");
	                        break;
	                    case Cell.CELL_TYPE_STRING:
	                       // System.out.print(cell.getRichStringCellValue() + "\t");
	                        text+=cell.getRichStringCellValue() + "\t";
	                        content.add(cell.getRichStringCellValue()+"\t");
	                        break;
	                } 
	            }
	            //System.out.println("");
	            text+="\n";
	        }
	        return sc.parallelize(content);
	    }
}