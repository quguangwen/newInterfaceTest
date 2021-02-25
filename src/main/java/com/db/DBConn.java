package com.db;

import com.util.ReadProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * 数据库初始化
 * @author 曲广文
 */
public class DBConn extends  BaseDB{
    /**
     * 根据DB名称初始化数据库实例，
     * @param dbName
     */
    public void getDBconn(String dbName){
        String username = ReadProperties.getProByKey("sqlusername");
        String password = ReadProperties.getProByKey("sqlpassword");
        String url = ReadProperties.getProByKey("mysql") + "/" + dbName;
        String driver = ReadProperties.getProByKey("driverClass");
        try {
            Class.forName(driver);
            setConn(DriverManager.getConnection(url,username,password));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
