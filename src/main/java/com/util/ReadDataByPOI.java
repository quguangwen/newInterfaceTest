package com.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ReadDataByPOI {
    @SuppressWarnings("unchecked")
    public Object[][] readExcels(String filePath, String sheetName) throws Exception{

        FileInputStream inputStream = new FileInputStream(filePath);
        Workbook workbook = null;
        String extensionName = filePath.substring(filePath.indexOf("."));
        if (extensionName.equals(".xls")){
            workbook= new HSSFWorkbook(inputStream);
        }
        else if (extensionName.equals(".xlsx")){
            workbook= new XSSFWorkbook(inputStream);
        }
        else{
            System.out.println("文件格式不正确");
        }

        FileInputStream fis = new FileInputStream(new File(filePath));
        Sheet sheet = workbook.getSheet(sheetName);
        int rows=sheet.getPhysicalNumberOfRows();

        @SuppressWarnings("uncheckd")
        HashMap<String, Object>[][] arrmap = new HashMap[rows-1][1];
        List<String> list = new ArrayList<String>();

        for(int i = 1 ; i < sheet.getPhysicalNumberOfRows() ; i++){
            arrmap[i-1][0] = new HashMap<String,Object>();
        }

        for(int i = 0 ; i < 1 ; i++){
            Row r = sheet.getRow(i);
            for (int j = 0; j < r.getPhysicalNumberOfCells(); j++) {
                Cell cell = r.getCell(j);
                list.add(getCellValue(cell));
            }
        }

        for(int i = 1 ; i < sheet.getPhysicalNumberOfRows() ; i++){
            Row r = sheet.getRow(i);
            if(r != null) {
                for (int j = 0; j < list.size(); j++) {
                    Cell cell = r.getCell(j);
                    String brandName = getCellValue(cell);
                    if (!brandName.equals("无法解析")) {
                        arrmap[i - 1][0].put(list.get(j), brandName);
                    }
                }
            }

        }
        return arrmap;
    }

    private static String getCellValue(Cell cell){
        int cellType=0;
        try {
            cellType = cell.getCellType();
        } catch (Exception e) {

            return "无法解析";
        }
        String value = "";
        if(cellType == Cell.CELL_TYPE_STRING){
            value = cell.getStringCellValue();
        }else if(cellType == Cell.CELL_TYPE_NUMERIC){
            value = String.valueOf((int)cell.getNumericCellValue());
        }else if(cellType == Cell.CELL_TYPE_BOOLEAN){
            value = String.valueOf(cell.getBooleanCellValue());
        }else if(cellType == Cell.CELL_TYPE_BLANK){
            value = "";
        }else if(cellType == Cell.CELL_TYPE_FORMULA){
            value = String.valueOf(cell.getCellFormula());
        }else{
            value = "";
        }
        return value;
    }

    public static void main(String[] args) {
        String filepath = "testData/AItestData.xlsx";
        String sheetName= "temptest";
        try {
            Object[][] hashMap = new ReadDataByPOI().readExcels(filepath,sheetName);
            for(int i = 0; i<hashMap.length;i++){
                HashMap<String,Object> data = (HashMap<String, Object>) hashMap[i][0];
                for(Map.Entry entry : data.entrySet()){
                    System.out.print(entry.getKey() + ":" + entry.getValue() + "  ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
