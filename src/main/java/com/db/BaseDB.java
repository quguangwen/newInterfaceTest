package com.db;

import com.mysql.cj.protocol.Resultset;
import com.util.LogUtils;
import org.testng.IResultMap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库操作方法类
 * @author 曲广文
 */
public class BaseDB {

    private Connection conn = null;

    public Connection getConn(){
        return conn;
    }
    public void setConn(Connection connection){
        this.conn = connection;
    }

    /**
     * 根据ID查询
     * @param sql
     * sql语句
     * @param column
     * 根据某一列属性查询
     * @return
     */
    public Integer selectID(String sql,String column){
        Integer result = 0;
        try {
            Statement stmt = getConn().createStatement();
            ResultSet resultset =  stmt.executeQuery(sql);
            while(resultset.next()){
                result = resultset.getInt(column);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }

    /**
     * 数据库信息删除操作
     * @param sql
     * sql语句
     */
    public void deleteDB(String sql){
        try {
            Statement stmt = getConn().createStatement();
            int result = stmt.executeUpdate(sql);
            LogUtils.logInfo("Result:" + result);
            System.out.println(result);
        } catch (Exception e) {
            LogUtils.logError(e);
        }

    }

    public void closeDB() throws SQLException {
        conn.close();
    }

    public void updateDB(){

    }
    public void insertDB(){

    }
}
