package com.company.Usr;


import com.company.DB.DBQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Calendar;
import java.util.regex.Pattern;

public abstract class User {
    private String username;

    private String password;

    private int borrowtime;

    private int authority;

    private Date dayofunlock;

    private boolean Isbantemp;

    private boolean Isbantemp1;//还书后被禁

    public int getAuthority() {
        String sql = "select authority from usr where Account = \"" + username + "\"";
        ResultSet re = DBQuery.Query(sql);
        int authorityre = 0;
        try {
            re.next();
            authorityre = re.getInt("Authority");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authorityre;
    }

    public Date getDateofunlock() {
        String sql = "select Dateofunlock from usr where Account = \"" + username + "\"";
        ResultSet re = DBQuery.Query(sql);
        Date dayofunlockre = null;
        try {
            re.next();
            dayofunlockre = re.getDate("Dateofunlock");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dayofunlockre;
    }

    public void setDateofunlock(Date dayofunlock) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dayofunlock);
        String datenow = calendar.get(Calendar.YEAR) - 1900 + "-" + calendar.get(Calendar.MONTH) + 1 + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        String sql = "update usr set dateofunlock = \"" + datenow + "\"" + " where Account = \"" + username + "\"";
        DBQuery.Update(sql);
    }

    public boolean getIsbantemp() {
        String sql = "select Isbantemp from usr where Account = \"" + username + "\"";
        ResultSet re = DBQuery.Query(sql);
        int Isbantempre = 0;
        try {
            re.next();
            Isbantempre = re.getInt("Isbantemp");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (Isbantempre != 0);
    }

    public void setIsbantemp(boolean isbantemp) {
        int val = isbantemp ? 1 : 0;
        String sql = "update usr set Isbantemp = " + val + " where Account = \"" + username + "\"";
        DBQuery.Update(sql);
    }

    public boolean getIsbantemp1() {
        String sql = "select Isbantemp1 from usr where Account = \"" + username + "\"";
        ResultSet re = DBQuery.Query(sql);
        int Isbantemp1re = 0;
        try {
            re.next();
            Isbantemp1re = re.getInt("Isbantemp1");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (Isbantemp1re != 0);
    }

    public void setIsbantemp1(boolean isbantemp1) {
        int val = isbantemp1 ? 1 : 0;
        String sql = "update usr set Isbantemp1 = " + val + " where Account = \"" + username + "\"";
        DBQuery.Update(sql);
    }

    private static final String usrnamepat = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";

    public static String getUsrnamepat() {
        return usrnamepat;
    }

    private static final String passwdpat = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";

    public static String getPasswdpat() {
        return passwdpat;
    }

    public int getBorrowtime() {
        String sql = "select RemainTime from usr where Account = \"" + username + "\"";
        ResultSet re = DBQuery.Query(sql);
        int RemainTimere = 0;
        try {
            re.next();
            RemainTimere = re.getInt("RemainTime");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return RemainTimere;
    }

    public void setBorrowtime(int borrowtime) {
        String sql = "update usr set borrowtime = " + borrowtime + " where Account = \"" + username + "\"";
        DBQuery.Update(sql);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        String sql = "select Password from usr where Account = \"" + username + "\"";
        ResultSet re = DBQuery.Query(sql);
        String Passwordre = null;
        try {
            re.next();
            Passwordre = re.getString("Password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Passwordre;
    }
    public String getpassword(){
        return password;
    }

    public User(String username, String password, int authority, int borrowtime, boolean isbantemp, boolean isbantemp1, Date dayofunlock) {
        try {
            if (Pattern.matches(usrnamepat, username) && Pattern.matches(passwdpat, password)) {
                this.username = username;
                this.password = password;
                this.authority = authority;
                this.borrowtime = borrowtime;
                this.Isbantemp = isbantemp;
                this.Isbantemp1 = isbantemp1;
                this.dayofunlock = dayofunlock;
            } else
                throw new Exception("错误的用户名格式或密码格式");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean IsRegisterUser() {
        return getAuthority() == 0;
    }

    public boolean IsAdmin() {
        return getAuthority() == 1 || getAuthority() == 2;
    }

    public boolean IsSuperAdmin() {
        return getAuthority() == 2;
    }
}
