package com.data;

import com.util.ReadDataByPOI;
import org.testng.annotations.DataProvider;

import java.util.HashMap;

/**
 * TESTNG 测试数据读取方法。
 * @author 曲广文
 */
public class ReadData {

    @DataProvider(name="data")
    public Object[][] getDataFromExcel() throws Exception {
        String filepath = "testData/AItestData.xlsx";
        String sheetName = "UserControl";
        return new ReadDataByPOI().readExcels(filepath,sheetName);
    }

    @DataProvider(name="dataRepo")
    public Object[][] getDataRepoFromExcel() throws Exception {
        String filepath = "testData/AItestData.xlsx";
        String sheetName = "dataRepo";
        return new ReadDataByPOI().readExcels(filepath,sheetName);
    }

    @DataProvider(name="modelRepo")
    public Object[][] getModelRepoFromExcel() throws Exception {
        String filepath = "testData/AItestData.xlsx";
        String sheetName = "modelRepo";
//        String sheetName = "temptest";
        return new ReadDataByPOI().readExcels(filepath,sheetName);
    }
    @DataProvider(name="algorithmRepo")
    public Object[][] getAlgorithmExcel() throws Exception {
        String filePath = "testData/AItestData.xlsx";
        String sheetName = "algorithmRepo";
        return new ReadDataByPOI().readExcels(filePath,sheetName);
    }
    @DataProvider(name="evaluationRepo")
    public Object[][] getEvaluationExcel() throws Exception {
        String filePath = "testData/AItestData.xlsx";
        String sheetName = "evaluationRepo";
        return new ReadDataByPOI().readExcels(filePath,sheetName);
    }

    @DataProvider(name="test")
    public Object[][] getTestData() throws Exception {
        String filePath = "testData/AItestData.xlsx";
        String sheetName = "test";
        return new ReadDataByPOI().readExcels(filePath,sheetName);
    }

    @DataProvider(name="systemManagement")
    public Object[][] getSystemTestData() throws Exception {
        String filePath = "testData/AItestData.xlsx";
        String sheetName = "systemManagement";
        return new ReadDataByPOI().readExcels(filePath,sheetName);
    }

}
