package com.company.DB;

import java.sql.*;

public class DBQuery {
    private static Connection con = null;

    public static Connection getCon() {
        return con;
    }

    //连接数据库
    public static void connect() {
        try {
            //加载驱动程序
            Class.forName(DBInit.getDriver());
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(DBInit.getUrl(), DBInit.getUser(), DBInit.getUsrpasswd());
        } catch (ClassNotFoundException e) {
            //数据库驱动类异常处理
            e.printStackTrace();

        } catch (SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    //关闭数据库
    public static void close(){
        try{
            Statement statement = con.createStatement();
            statement.close();
            con.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    //返回查询结果
    public static ResultSet Query(String sql){
        try{
            Statement statement = con.createStatement();
            return statement.executeQuery(sql);
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        } catch (SQLException e){
            //e.printStackTrace();
            return null;
        }
    }
    //更新或插入数据
    public static void Update(String sql){
        try{
            Statement statement = con.createStatement();
            statement.executeUpdate(sql);
        }catch (SQLException e){
            //e.printStackTrace();
        }
    }




}
