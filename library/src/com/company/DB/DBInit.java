package com.company.DB;

import java.sql.*;

public class DBInit {

    /*
        使用JDBC连接数据库，连接的是MySQL，需要自行更改数据库的密码。
        创建的数据库名为library
     */

    /*
        会重置所有表哟~
     */
    private static Connection con;
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/library?serverTimezone=Asia/Shanghai";//设置了上海的时间
    private static final String user = "root";
    private static final String usrpasswd = "liuziming";//记得更改密码


    public static String getDriver() {
        return driver;
    }

    public static String getUrl() {
        return url;
    }

    public static Connection getCon() {
        return con;
    }

    public static String getUser() {
        return user;
    }

    public static String getUsrpasswd() {
        return usrpasswd;
    }

    private static void connect() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        con = DriverManager.getConnection(url, user, usrpasswd);
        if (!con.isClosed())
            System.out.println("Succeeded connecting to the Database!");
    }


    /*
        创建书本类的数据表
     */

    private static void CreateBookTable() {
        try {
            connect();
            Statement statement = con.createStatement();
            String test = "drop table if exists Book";
            statement.executeUpdate(test);
            String sqlString = "create table Book ("
                    + "ISBN varchar(20) PRIMARY KEY,"
                    + "Title varchar(255),"
                    + "Author varchar(255),"
                    + "Price varchar(10),"
                    + "remain int, "
                    + "sumb int )";
            statement.executeUpdate(sqlString);
            statement.close();
            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    //创建用户表
    //Authority字段为权限，0为注册用户，1为管理员，2为超级管理员
    //Isbantemp代表该用户有没有还书
    //Isbantemp1代表用户有没有超时还书被禁
    //dayofunlock代表用户解锁的天数
    private static void CreateUserTable() {
        try {
            connect();
            Statement statement = con.createStatement();
            String test = "drop table if exists Usr";
            statement.executeUpdate(test);
            String sqlString = "create table Usr ("
                    + "Account varchar(255) PRIMARY KEY,"
                    + "Password varchar(255),"
                    + "Authority int default 0,"
                    + "Isbantemp int default 0,"
                    + "Isbantemp1 int default 0,"
                    + "Dateofunlock DATE ,"
                    + "RemainTime int default 4)";
            statement.executeUpdate(sqlString);
            statement.close();
            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }


    private static void CreateBorrowLendTable() {
        try {
            connect();
            Statement statement = con.createStatement();
            String test = "drop table if exists BorrowLend";
            statement.executeUpdate(test);
            String sqlString = "create table BorrowLend ("
                    + "id int primary key auto_increment,"
                    + "Account varchar(255),"
                    + "ISBN varchar(255),"
                    + "BorrowDate DATE,"
                    + "Sta int default 0,"
                    + "Isxued int default 0,"
                    + "DayofReturn Date)";
            statement.executeUpdate(sqlString);
            statement.close();
            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        CreateBookTable();
        CreateUserTable();
        CreateBorrowLendTable();
    }


}




