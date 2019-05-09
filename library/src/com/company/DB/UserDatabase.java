package com.company.DB;

import com.company.Usr.Admin;
import com.company.Usr.RegisterUser;
import com.company.Usr.SuperAdmin;
import com.company.Usr.User;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDatabase {
    //获取此账号用户的信息
    public static User getUserinfo(String account){
        String sql = "select * from usr where Account = \"" + account + "\"";
        ResultSet re = DBQuery.Query(sql);
        try{
            re.next();
            int flag = re.getInt("Authority");
            if(flag == 0){
                return new RegisterUser(
                        re.getString("Account"),
                        re.getString("Password"),
                        re.getInt("Authority"),
                        re.getInt("RemainTime"),
                        re.getInt("Isbantemp") != 0,
                        re.getInt("Isbantemp1") != 0,
                        re.getDate("Dateofunlock")
                );
            }
            else if(flag == 1){
                return new Admin(
                        re.getString("Account"),
                        re.getString("Password"),
                        re.getInt("Authority"),
                        re.getInt("RemainTime"),
                        re.getInt("Isbantemp") != 0,
                        re.getInt("Isbantemp1") != 0,
                        re.getDate("Dateofunlock")
                );
            }
            else if(flag == 2){
                return new SuperAdmin(
                        re.getString("Account"),
                        re.getString("Password"),
                        re.getInt("Authority"),
                        re.getInt("RemainTime"),
                        re.getInt("Isbantemp") != 0,
                        re.getInt("Isbantemp1") != 0,
                        re.getDate("Dateofunlock")
                );
            }
            else
                return null;

        }catch (SQLException e){
            //e.printStackTrace();
            return null;
        }
    }

    //添加用户
    public static void AddUser(User user){
        int flag = 0;
        if(user instanceof Admin) flag = 1;
        if(user instanceof SuperAdmin) flag = 2;
        String sql = "insert into usr (Account, Password, Authority) VALUES (" + "\"" + user.getUsername() + "\"," + "\"" + user.getpassword() + "\"," + flag + ")";
        DBQuery.Update(sql);
    }

    //删除用户
    public static void DelUser(String username){
        String sql = "delete from usr where Account=\"" + username + "\"";
        DBQuery.Update(sql);
    }

    //更新用户账号
    public static void UpdateAccount(String oldName, String newName){
        String sql = "update usr set Account=\"" + newName + "\"" + " where Account = \"" + oldName + "\"";
        DBQuery.Update(sql);
    }

    //更新用户密码
    public static void UpdatePassword(String newpwd, String account){
        String sql = "update usr set Password=\"" + newpwd + "\"" + " where account = \"" + account + "\"";
        DBQuery.Update(sql);
    }

}
