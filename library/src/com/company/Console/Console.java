package com.company.Console;

import com.company.DB.DBQuery;
import com.company.DB.UserDatabase;
import com.company.Exception.RemainException;
import com.company.Lib.Book;
import com.company.Lib.ISBN;
import com.company.Lib.Library;
import com.company.Usr.*;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console {

    private static Scanner scanner = new Scanner(System.in);

    private User user;

    private static final String usrnamepat = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";

    private static final String passwdpat = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";

    private Calendar calendar = Calendar.getInstance();

    /*
        注销
     */
    private void logout() {
        signinout();
    }

    /*
        欢迎界面
     */
    //读入函数
    private static String read() {
        String re = null;
        try {
            re = scanner.nextLine();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return re;
    }

    public void printmenu() {
        System.out.print("--------------------欢迎来到图书馆,");
        if (user.IsSuperAdmin())
            System.out.print("超级管理员");
        else if (user.IsAdmin())
            System.out.print("普通管理员");
        else if (user.IsRegisterUser())
            System.out.print("注册用户");
        System.out.print(user.getUsername() + "先生-------------------");
        System.out.println();
        System.out.println("请选择你想使用的功能编号：");
        System.out.println("1.查看馆藏书籍");
        System.out.println("2.查看借阅记录");
        if (user.IsAdmin() || user.IsSuperAdmin()) {
            System.out.println("3.添加馆藏书籍");
            System.out.println("4.修改馆藏书籍");
            System.out.println("5.检索用户");
            if (user.IsSuperAdmin()) {
                System.out.println("6.CRUD管理员");
                System.out.println("7.退出");
                System.out.println("8.注销");
            } else {
                System.out.println("5.退出");
                System.out.println("6.注销");
            }
        } else {
            System.out.println("3.退出");
            System.out.println("4.注销");
        }
        System.out.print("请输入您的命令，按回车键结束:");
    }

    /*
       检索书籍菜单
     */
    public void printsubmenu() {
        System.out.println("--------------------欢迎查看馆藏书籍-------------------");
        System.out.println("请选择你想使用的功能编号：");
        System.out.println("1.ISBN定向检索");
        System.out.println("2.关键词检索书名");
        System.out.println("3.返回菜单");
        System.out.println("4.注销");
        System.out.print("请输入您的命令，按回车键结束:");
    }

    /*
        检索用户菜单
     */
    private void searchusrmenu() {
        System.out.println("1.查看所有注册用户信息");
        System.out.println("2.以账号检索用户信息");
        System.out.println("3.退出");
        System.out.println("4.注销");
    }

    /*
        分页打印
     */
    private <T> void printbypg(List<T> re) {
        int size = re.size();
        int page_num = 0;
        page_num = (size % 10 != 0) ? (size / 10 + 1) : (size / 10);
        int now = 1;
        while (true) {
            System.out.println("---------------您现在在" + now + "页----------------------");
            int tmp = 10 * (now - 1);
            if (re.get(0) instanceof User) {
                if (re.isEmpty()) return;
                for (int i = tmp; i < Integer.min(tmp + 10, size); i++) {
                    User usrtmp = (User) re.get(i);
                    System.out.println("用户名为:\t" + usrtmp.getUsername());
                    System.out.println("密码为:\t" + usrtmp.getPassword());
                    System.out.println();
                }
            } else {
                for (int i = tmp; i < Integer.min(tmp + 10, size); i++) {
                    Book tmpbk = (Book) re.get(i);
                    tmpbk.print();
                    System.out.println();
                }
            }
            String jp = "";
            if (now != 1)
                System.out.println("1.上一页");
            if (now != page_num)
                System.out.println("2.下一页");
            System.out.println("3.跳转");
            if (user.IsRegisterUser()) {
                System.out.println("4.借阅");
                System.out.println("5.退出");
            } else
                System.out.println("4.退出");
            try {
                jp = scanner.nextLine();
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                break;
            }
            if (jp.isEmpty() || !isnum(jp))
                System.out.println("非法输入,请重试");
            else {
                int inp_num;
                try {
                    inp_num = Integer.parseInt(jp);
                } catch (NumberFormatException e) {
                    System.out.println("输入的数字太大了，超出范围了");
                    e.printStackTrace();
                }
                if (jp.equals("1"))
                    now--;
                else if (jp.equals("2"))
                    now++;
                else if (jp.equals("3")) {
                    System.out.println("请输入要跳转到的页面,按回车结束");
                    String read = scanner.nextLine();
                    if (read.isEmpty() || !isnum(read))
                        System.out.println("请输入正确的数字");
                    else {
                        int pgn = 1;
                        try {
                            pgn = Integer.parseInt(read);
                        } catch (NumberFormatException e) {
                            System.out.println("输入的数字太大了,超出了范围");
                            e.printStackTrace();
                        }
                        if (pgn < 1 || pgn > page_num)
                            System.out.println("输入数字超出页数范围,请重试");
                        else
                            now = pgn;
                    }
                } else if ((jp.equals("4") && !user.IsRegisterUser()) || (jp.equals("5") && user.IsRegisterUser()))
                    break;
                else if (jp.equals("4") && re.size() > 0 && user.IsRegisterUser()) {
                    borrow();
                } else
                    System.out.println("非法输入,请重试");
            }
        }
    }

    /*
        是否有书超期未还以及更新状态
     */
    private void update() {
        if (user.getIsbantemp() || user.getIsbantemp1()) {
            System.out.println("您已被暂时禁止借阅");
            return;
        }
        if (!user.getIsbantemp() && user.getIsbantemp1() && getdayD(new java.sql.Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)), user.getDateofunlock()) < 0)
            user.setIsbantemp1(false);
        try {
            String sql = "select * from borrowlend";
            ResultSet re = DBQuery.Query(sql);
            while (re.next()) {
                int status = re.getInt("Sta");
                java.sql.Date dayofreturn = re.getDate("DayofReturn");
                if (status == 0 && getdayD(new java.sql.Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)), dayofreturn) < 0) {
                    sql = "update borrowlend set sta = 2 where Account = \"" + user.getUsername() + "\"";
                    DBQuery.Update(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (notbackintime(user.getUsername())) {
            user.setIsbantemp(true);
            user.setIsbantemp1(true);
            System.out.println("你已被暂时禁止借阅,请及时归还书籍");
        }
    }

    private boolean notbackintime(String username) {
        //update();
        try {
            String sql = "select Sta from borrowlend where Account=\"" + username + "\"";
            ResultSet re = DBQuery.Query(sql);
            while (re.next()) {
                int status = re.getInt("Sta");
                if (status == 2)
                    return true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /*
        借书
     */
    private void borrow(String... Isbn) {
        update();
        if (user.getIsbantemp1() || user.getIsbantemp())
            return;
        String isbn = "";
        boolean flagb = true;
        while (!ISBN.checkIsbn(isbn) || flagb) {
            if (Isbn.length == 0) {
                System.out.println("请输入要借阅书籍的ISBN编号");
                isbn = read();
                if (isbn.equals("q")) return;
                if (!ISBN.checkIsbn(isbn)) {
                    System.out.println("错误的ISBN,请重新输入");
                    continue;
                }
            } else
                isbn = Isbn[0];
            if (Library.getBookByIsbn(new ISBN(isbn)) == null) {
                flagb = true;
                System.out.println("无此书信息，请重新输入");
            } else
                flagb = false;
            try {
                ResultSet re = DBQuery.Query("select remain from Book where ISBN = \"" + isbn.replace("-", "") + "\"");
                re.next();
                if (re.getInt("remain") == 0)
                    throw new RemainException("库存为0，无法借阅");
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (RemainException e) {
                flagb = true;
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (user.getBorrowtime() == 0) {
                System.out.println("借阅次数已用完");
                flagb = true;
            }

        }
        String sql = "select remain from Book where ISBN = \"" + isbn.replace("-", "") + "\"";
        int orgremain = 1;
        try {
            ResultSet re = DBQuery.Query(sql);
            re.next();
            orgremain = re.getInt("remain");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int nowremain = orgremain - 1;
        sql = "update Book set remain = " + nowremain + " where ISBN = \"" + isbn.replace("-", "") + "\"";
        DBQuery.Update(sql);
        Calendar caltmp = (Calendar) calendar.clone();
        caltmp.add(Calendar.DAY_OF_YEAR, 30);
        Date rtn = caltmp.getTime();
        sql = "insert into borrowlend (Account, ISBN, BorrowDate, Sta, DayofReturn) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps = DBQuery.getCon().prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, isbn.replace("-", ""));
            ps.setDate(3, new java.sql.Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
            ps.setInt(4, 0);
            ps.setDate(5, new java.sql.Date(calendar.get(Calendar.YEAR), caltmp.get(Calendar.MONTH), caltmp.get(Calendar.DAY_OF_MONTH)));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "update usr set Remaintime = Remaintime - 1 where Account=\"" + user.getUsername() + "\"";
        DBQuery.Update(sql);
        System.out.println("借阅成功！");
        System.out.println();
    }

    /*
        还书
     */
    private void returnbook() {
        update();
        boolean flag1 = false;
        String sql = "select * from borrowlend where Account = \"" + user.getUsername() + "\"";
        try {
            ResultSet re = DBQuery.Query(sql);
            while (re.next()) {
                String isbn = re.getString("ISBN");
                java.sql.Date date = re.getDate("DayofReturn");
                int status = re.getInt("sta");
                Calendar caltmp = Calendar.getInstance();
                caltmp.setTime(date);
                System.out.println("书号:" + isbn);
                System.out.println("借阅时间:" + (caltmp.get(Calendar.YEAR)) + "年" + caltmp.get(Calendar.MONTH) + "月" + caltmp.get(Calendar.DAY_OF_MONTH) + "日");
                System.out.print("状态:");
                if (status == 0)
                    System.out.print("正常借阅");
                else if (status == 1)
                    System.out.print("已经归还");
                else if (status == 2) {
                    System.out.print("超期未还");
                    flag1 = true;
                }
                System.out.println();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
        //System.out.println("请输入你要归还的图书编号");
        System.out.println("--------------------请选择还书或者续借-------------------");
        System.out.println("1.还书");
        System.out.println("2.续借");
        String flag = read();
        if (flag.equals("1")) {
            String isbn = "";
            do {
                System.out.println("请输入你要归还的ISBN号");
                isbn = read();
                if (isbn.equals("q")) return;
            } while (!okret(isbn));

            sql = "select Sta from borrowlend where ISBN=\"" + isbn.replace("-", "") + "\"";
            try {
                ResultSet re = DBQuery.Query(sql);
                while (re.next()) {
                    int status = re.getInt("Sta");
                    if (status == 0 || status == 2) {
                        sql = "update borrowlend set Sta = 1 where ISBN=\"" + isbn.replace("-", "") + "\"" + " and Sta != 1" + " and Account = \"" + user.getUsername() + "\" order by id asc limit 1";
                        DBQuery.Update(sql);
                        sql = "update usr set RemainTime = RemainTime + 1 where Account = \"" + user.getUsername() + "\"";
                        DBQuery.Update(sql);
                        break;
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("还书成功");
            sql = "update book set remain = remain + 1 where ISBN = \"" + isbn.replace("-", "") + "\"";
            DBQuery.Update(sql);
            boolean flag2 = true;
            try {
                sql = "select Sta from borrowlend where Account=\"" + user.getUsername() + "\"";
                ResultSet re = DBQuery.Query(sql);
                while (re.next()) {
                    int status = re.getInt("Sta");
                    if (status == 2)
                        flag2 = false;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (flag1 && flag2) {
                try {
                    sql = "select Sta from borrowlend where Account=\"" + user.getUsername() + "\"" + " and ISBN=\"" + isbn.replace("-", "") + "\"";
                    ResultSet re = DBQuery.Query(sql);
                    if (re != null) {
                        Calendar caltmp = (Calendar) calendar.clone();
                        caltmp.add(Calendar.DAY_OF_YEAR, 30);
                        user.setDateofunlock(new java.sql.Date(caltmp.get(Calendar.YEAR), caltmp.get(Calendar.MONTH), caltmp.get(Calendar.DAY_OF_MONTH)));
                        user.setIsbantemp(false);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }
        if (flag.equals("2")) {
            String isbn = "";
            do {
                System.out.println("请输入你要续借的ISBN号");
                isbn = read();
                if (isbn.equals("q")) return;
            } while (!okret(isbn));
            sql = "select * from borrowlend where Account=\"" + user.getUsername() + "\"";
            try {
                ResultSet re = DBQuery.Query(sql);
                while (re.next()) {
                    String brisbn = re.getString("ISBN");
                    boolean isxued = (re.getInt("Isxued") != 0);
                    java.sql.Date date = re.getDate("DayofReturn");
                    java.sql.Date now = new java.sql.Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    int diff = getdayD(now, date);
                    if (brisbn.equals(isbn.replace("-", "")) && !isxued && diff <= 3 && diff > 0) {
                        Calendar caltmp = Calendar.getInstance();
                        caltmp.setTime(date);
                        caltmp.add(Calendar.DAY_OF_YEAR, 14);
                        String datenow = caltmp.get(Calendar.YEAR) + "-" + (caltmp.get(Calendar.MONTH) + 1) + "-" + caltmp.get(Calendar.DAY_OF_MONTH);
                        sql = "update borrowlend set DayofReturn = \"" + datenow + "\"" + " where Account = \"" + user.getUsername() + "\"" + " and ISBN=\"" + brisbn + "\"";
                        DBQuery.Update(sql);
                        sql = "update borrowlend set Isxued = 1 where Account = \"" + user.getUsername() + "\"" + " and ISBN=\"" + brisbn + "\"";
                        DBQuery.Update(sql);
                        System.out.println("续借成功");
                        break;
                    } else if (isxued)
                        System.out.println("一本书只能续借一次哦!");
                    else if (diff > 3 || diff <= 0)
                        System.out.println("只能在借阅期的最后三天申请办理哦!");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean okret(String isbn) {
        try {
            String sql = "select * from borrowlend where Account=\"" + user.getUsername() + "\"" + " and ISBN = \"" + isbn.replace("-", "") + "\"";
            ResultSet re = DBQuery.Query(sql);
            return re != null;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
        检索用户
     */
    private void searchusr() {
        String flag = "";
        while (!flag.equals("3")) {
            searchusrmenu();
            flag = read();
            if (flag.equals("1")) {
                List<User> prt = new ArrayList<>();
                String sql = "select * from usr where Authority = 0";
                ResultSet re = DBQuery.Query(sql);
                try {
                    while (re.next()) {
                        String account = re.getString("Account");
                        String password = re.getString("Password");
                        prt.add(new RegisterUser(account, password, 0, 4, false, false, new java.sql.Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR))));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
                printbypg(prt);
            } else if (flag.equals("2")) {
                System.out.println("请输入您要找的用户名");
                String usrtmp = "";
                usrtmp = read();
                if (usrtmp.equals("q")) return;
                if (UserDatabase.getUserinfo(usrtmp) == null || UserDatabase.getUserinfo(usrtmp) instanceof Admin)
                    System.out.println("您要找的用户不存在，请重试");
                else {
                    try {
                        System.out.println("用户名为:\t" + UserDatabase.getUserinfo(usrtmp).getUsername());
                        System.out.println("密码为:\t" + UserDatabase.getUserinfo(usrtmp).getPassword());
                        System.out.println();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            } else if (flag.equals("4"))
                logout();
        }


    }

    /*
        CRUD菜单
     */
    private void crudusrmenu() {
        System.out.println("1.检索管理员账号信息");
        System.out.println("2.添加管理员账号信息");
        System.out.println("3.修改管理员账号信息");
        System.out.println("4.删除管理员账号信息");
        System.out.println("5.退出");
        System.out.println("6.注销");
    }

    /*
        修改管理员菜单
     */
    private void changeadminmenu() {
        System.out.println("1.修改管理员账号");
        System.out.println("2.修改管理员密码");
        System.out.println("3.退出");
        System.out.println("4.注销");
    }

    private void crudusr() {
        String flag = "";
        while (!flag.equals("5")) {
            crudusrmenu();
            flag = read();
            if (flag.equals("1")) {
                String sql = "select * from usr where Authority = 1";
                ResultSet re = DBQuery.Query(sql);
                List<User> prt = new ArrayList<>();
                try {
                    while (re.next()) {
                        String account = re.getString("Account");
                        String password = re.getString("Password");
                        prt.add(new Admin(account, password, 1, 4, false, false, new java.sql.Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR))));
                    }
                } catch (SQLException e) {
                    System.out.println("未找到该管理员");
                    e.printStackTrace();
                    return;
                }
                printbypg(prt);

            } else if (flag.equals("3")) {
                String usrname = "";
                while (UserDatabase.getUserinfo(usrname) == null) {
                    System.out.println("请输入您要修改的管理员账户");
                    usrname = read();
                    if (usrname.equals("q")) return;
                    if (UserDatabase.getUserinfo(usrname) instanceof SuperAdmin || UserDatabase.getUserinfo(usrname) instanceof RegisterUser) {
                        System.out.println("不存在此管理员，请重试");
                    }
                }
                String flagg = "";
                while (!flagg.equals("3")) {
                    changeadminmenu();
                    flagg = read();
                    if (flagg.equals("1")) {
                        System.out.println("请输入新的管理员用户名");
                        String newusrname = "";
                        newusrname = read();
                        if (newusrname.equals("q")) return;
                        boolean flagy = false;
                        if (UserDatabase.getUserinfo(newusrname) != null) {
                            flagy = true;
                            System.out.println("此用户名已有，请重新输入");
                        } else if (!Pattern.matches(User.getUsrnamepat(), newusrname))
                            System.out.println("请输入新的管理员用户名");
                        while (!Pattern.matches(User.getUsrnamepat(), newusrname) || flagy) {
                            System.out.println("请输入新的管理员用户名");
                            newusrname = read();
                            if (newusrname.equals("q")) return;
                            if (!Pattern.matches(User.getUsrnamepat(), newusrname)) {
                                System.out.println("错误的用户名格式，请重新输入");
                                continue;
                            }
                            if (UserDatabase.getUserinfo(newusrname) != null) {
                                System.out.println("此用户名已有，请重新输入");
                                flagy = true;
                            } else
                                flagy = false;
                        }
                        UserDatabase.UpdateAccount(usrname, newusrname);
                        System.out.println("修改成功");
                    } else if (flagg.equals("2")) {
                        System.out.println("请输入新的管理员密码");
                        String newpasswd = "";
                        newpasswd = read();
                        if (newpasswd.equals("q")) return;
                        while (!Pattern.matches(User.getPasswdpat(), newpasswd)) {
                            System.out.println("请输入新的管理员密码");
                            newpasswd = read();
                            if (newpasswd.equals("q")) return;
                            if (!Pattern.matches(User.getPasswdpat(), newpasswd))
                                System.out.println("错误的密码格式，请重新输入");

                        }
                        try {
                            UserDatabase.UpdatePassword(newpasswd, usrname);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        System.out.println("修改成功");
                    } else if (flagg.equals("4"))
                        logout();
                }


            } else if (flag.equals("2")) {
                System.out.println("请输入您要添加的管理员用户名");
                String usrname = "";
                while (!Pattern.matches(User.getUsrnamepat(), usrname) || (UserDatabase.getUserinfo(usrname) != null)) {
                    usrname = read();
                    if (usrname.equals("q")) return;
                    if (!Pattern.matches(User.getUsrnamepat(), usrname))
                        System.out.println("不合法的用户名，请重新输入");
                    if (UserDatabase.getUserinfo(usrname) != null)
                        System.out.println("已有此账号，请重新输入");

                }
                System.out.println("请输入您要添加的管理员密码");
                String passwd = "";
                try {
                    passwd = scanner.nextLine();
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }
                while (!Pattern.matches(User.getPasswdpat(), passwd)) {
                    passwd = read();
                    if (passwd.equals("q")) return;
                    if (!Pattern.matches(User.getPasswdpat(), passwd))
                        System.out.println("不合法的密码，请重新输入");
                }

                UserDatabase.AddUser(new Admin(usrname, passwd, 1, 4, false, false, new java.sql.Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR))));
                System.out.println("添加成功");

            } else if (flag.equals("4")) {
                System.out.println("请输入您要删除的管理员用户名");
                String usrname = "";
                while (UserDatabase.getUserinfo(usrname) == null || UserDatabase.getUserinfo(usrname) instanceof RegisterUser || UserDatabase.getUserinfo(usrname) instanceof SuperAdmin) {
                    usrname = read();
                    if (usrname.equals("q")) return;
                    if (UserDatabase.getUserinfo(usrname) == null || UserDatabase.getUserinfo(usrname) instanceof RegisterUser || UserDatabase.getUserinfo(usrname) instanceof SuperAdmin)
                        System.out.println("未找到此管理员，请重新输入");
                }
                UserDatabase.DelUser(usrname);
                System.out.println("删除成功");
            } else if (flag.equals("6")) {
                logout();
            }
        }
    }

    private void seeborrow() {
        String sql = "select * from usr where Authority=0";
        try {
            ResultSet re = DBQuery.Query(sql);
            while (re.next()) {
                boolean isbantemp = re.getInt("Isbantemp") != 0;
                boolean isbantemp1 = re.getInt("Isbantemp1") != 0;
                String username = re.getString("Account");
                System.out.println("用户名:" + username);
                //User now = UserDatabase.getUserobjinfo().get(entry.getKey());
                System.out.print("用户状态:");
                if (isbantemp && isbantemp1)
                    System.out.print("封禁(不可解封)");
                else if (!isbantemp && isbantemp1)
                    System.out.println("封禁(可解封)");
                else if (!isbantemp && !isbantemp1)
                    System.out.println("正常");
                sql = "select * from borrowlend where Account=\"" + username + "\"";
                ResultSet re1 = DBQuery.Query(sql);
                while (re1.next()) {
                    String isbn = re1.getString("ISBN");
                    java.sql.Date borrowDate = re1.getDate("BorrowDate");
                    java.sql.Date dayofReturn = re1.getDate("DayofReturn");
                    Calendar calbor = Calendar.getInstance();
                    calbor.setTime(borrowDate);
                    Calendar calrtn = Calendar.getInstance();
                    calrtn.setTime(dayofReturn);
                    int status = re1.getInt("Sta");
                    int isxued = re1.getInt("Isxued");
                    System.out.println("书号:\t" + isbn);
                    System.out.println("借阅时间:\t" + calbor.get(Calendar.YEAR) + "年" + calbor.get(Calendar.MONTH) + "月" + calbor.get(Calendar.DAY_OF_MONTH) + "日");
                    System.out.println("借阅时间:\t" + calrtn.get(Calendar.YEAR) + "年" + calrtn.get(Calendar.MONTH) + "月" + calrtn.get(Calendar.DAY_OF_MONTH) + "日");
                    System.out.print("状态:\t");
                    if (status == 0) System.out.print("正常借阅");
                    else if (status == 1) System.out.print("已经归还");
                    else if (status == 2)
                        System.out.print("超期未还");
                    System.out.println();
                    System.out.print("是否续借:\t");
                    if (status == 0)
                        System.out.print("否");
                    else
                        System.out.print("是");
                    System.out.println();
                    System.out.println();
                }
                System.out.println("--------------------");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String flag = "";
        while (!flag.equals("2")) {
            System.out.println("1.解封用户");
            System.out.println("2.退出");
            System.out.println("3.注销");
            String username = null;
            flag = read();
            if (flag.equals("3")) logout();
            else if (flag.equals("1")) {
                do {
                    System.out.println("请输入该用户的用户名");
                    username = read();
                } while (UserDatabase.getUserinfo(username) == null);
                sql = "update usr set Isbantemp1 = 0 where Account=\"" + username + "\"";
                DBQuery.Update(sql);
                System.out.println("解封成功");
            }
        }
    }

    public void mainmenu() {
        String flag = "7";
        do {
            printmenu();
            if (scanner.hasNextLine())
                flag = scanner.nextLine();
            else
                break;
            if (flag.equals("1"))
                submenu1();
            else if (flag.equals("3") && (user.IsAdmin() || user.IsSuperAdmin()))
                submenu2();
            else if (flag.equals("4") && (user.IsAdmin() || user.IsSuperAdmin()))
                submenu3();
            else if ((flag.equals("6") && (user.IsAdmin() && !user.IsSuperAdmin())) || (flag.equals("4") && !(user.IsAdmin() || user.IsSuperAdmin())) || ((flag.equals("8") && user.IsSuperAdmin())))
                logout();
            else if ((flag.equals("5") && user.IsAdmin()))
                searchusr();
            else if (flag.equals("6") && user.IsSuperAdmin())
                crudusr();
            else if (flag.equals("2") && user.IsRegisterUser())
                returnbook();
            else if (flag.equals("2") && (user.IsAdmin() || user.IsSuperAdmin()))
                seeborrow();

        } while ((!flag.equals("3") && user.IsRegisterUser()) || ((!flag.equals("5")) && (user.IsAdmin() && !user.IsSuperAdmin())) || ((!flag.equals("7")) && user.IsSuperAdmin()));
    }

    /*
        修改书籍菜单
     */
    private void changemenu() {
        System.out.println("请选择你要进行的操作");
        System.out.println("1.修改图书的ISBN编号");
        System.out.println("2.修改图书的标题");
        System.out.println("3.修改图书的作者");
        System.out.println("4.修改图书的价格");
        System.out.println("5.修改图书的库存");
        System.out.println("6.修改图书的总量");
        System.out.println("7.删除该图书");
        System.out.println("8.退出");
        System.out.println("9.注销");
    }

    public void submenu3() {
        System.out.println("--------------------欢迎修改馆藏书籍-------------------");
        String isbn = "";
        boolean flagx = true;
        while (!ISBN.checkIsbn(isbn) || flagx) {
            System.out.println("请输入书籍的ISBN编号");
            isbn = read();
            if (isbn.equals("q")) return;
            if (!ISBN.checkIsbn(isbn)) {
                System.out.println("错误的ISBN,请重新输入");
                continue;
            }


            if (Library.getBookByIsbn(new ISBN(isbn)) == null) {
                flagx = true;
                System.out.println("无此书信息，请重新输入");
            } else
                flagx = false;
        }
        Book find = Library.getBookByIsbn(new ISBN(isbn));
        if (find != null)
            System.out.println("已找到此图书，图书信息如下");
        find.print();

        String flag = "";
        while (!flag.equals("8")) {
            changemenu();
            flag = read();
            if (flag.equals("1")) {
                System.out.println("请输入新的ISBN");
                String newisbn = "";
                while (!ISBN.checkIsbn(newisbn)) {
                    newisbn = read();
                    if (newisbn.equals("q")) return;
                    if (!ISBN.checkIsbn(newisbn))
                        System.out.println("错误的ISBN，请重试");
                }


                while (!Library.setbookISBN(new ISBN(isbn), new ISBN(newisbn))) {
                    System.out.println("请输入新的ISBN");
                    newisbn = read();
                    if (newisbn.equals("q")) return;
                }
                isbn = newisbn;
                System.out.println("操作成功！");
            } else if (flag.equals("2")) {
                System.out.println("请输入新的标题");
                String newtitle = "";
                newtitle = read();
                if (newtitle.equals("q")) return;
                Library.setbookTitle(new ISBN(isbn), newtitle);
                System.out.println("操作成功");
            } else if (flag.equals("3")) {
                System.out.println("请输入新的作者名单，以空格结束");
                String author = "";
                author = read();
                if (author.equals("q")) return;
                String[] authora = author.split(" ");
                Library.setbookAuthor(new ISBN(isbn), authora);
                System.out.println("操作成功");
            } else if (flag.equals("4")) {
                String price = "";
                BigDecimal priceB = new BigDecimal("-1.0");
                while (priceB.compareTo(BigDecimal.ZERO) == -1) {
                    System.out.println("请输入新的价格");
                    try {
                        price = scanner.nextLine();
                        priceB = new BigDecimal(price);
                    } catch (NoSuchElementException e) {
                        e.printStackTrace();
                        return;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        if (price.equals("q")) return;
                    }
                    if (priceB.compareTo(BigDecimal.ZERO) == -1)
                        System.out.println("价格输入错误，请重新输入");
                }
                Library.setbookPrice(new ISBN(isbn), priceB);
                System.out.println("操作成功");
            } else if (flag.equals("5")) {
                int remain = -1;
                while (remain < 0) {
                    System.out.println("请输入这本书的剩余量");
                    try {
                        remain = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        e.printStackTrace();
                        scanner.nextLine();
                        String rem = Integer.toString(remain);
                        if (rem.equals("q")) return;
                    } catch (NoSuchElementException e) {
                        e.printStackTrace();
                        return;
                    }
                    if (remain < 0)
                        System.out.println("剩余量输入不合法，请重新输入");
                }
                Library.setbookRemain(new ISBN(isbn), remain);
                System.out.println("操作成功");
            } else if (flag.equals("6")) {
                int sum = -1;
                while (sum < 0) {
                    System.out.println("请输入这本书的总量");
                    try {
                        sum = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        e.printStackTrace();
                        scanner.nextLine();
                        if (Integer.toString(sum).equals("q")) return;
                    } catch (NoSuchElementException e) {
                        e.printStackTrace();
                        return;
                    }
                    if (sum < 0)
                        System.out.println("总量输入不合法，请重新输入");
                }
                Library.setbookSum(new ISBN(isbn), sum);
                System.out.println("操作成功");
            } else if (flag.equals("7")) {
                String sql = "delete from Book where ISBN=\"" + isbn.replace("-", "") + "\"";
                DBQuery.Update(sql);
                System.out.println("删除成功");
            } else if (flag.equals("9"))
                logout();
        }

    }

    /*
        管理员添加书籍
     */
    public void submenu2() {
        System.out.println("--------------------欢迎添加馆藏书籍-------------------");
        String isbn = "";
        boolean flagt = true;
        while (!ISBN.checkIsbn(isbn) || flagt) {
            System.out.println("请输入书籍的ISBN号码");
            isbn = read();
            if (isbn.equals("q")) return;
            if (ISBN.checkIsbn(isbn))
                isbn = isbn.replace("-", "");
            if (Library.getBookByIsbn(new ISBN(isbn)) != null) {
                System.out.println("此书已有，请重新输入");
                flagt = true;
            } else
                flagt = false;
            if (!ISBN.checkIsbn(isbn))
                System.out.println("错误的ISBN,请重新输入");
        }
        String title = "";
        System.out.println("请输入书籍的标题");
        title = read();
        if (title.equals("q")) return;
        String author = "";
        System.out.println("请输入这本书的作者，输入时以空格作为分割");
        author = read();
        if (author.equals("q")) return;
        String[] authora = author.split(" ");
        String price = "";
        BigDecimal priceB = new BigDecimal("-1.0");
        while (priceB.compareTo(BigDecimal.ZERO) == -1) {
            System.out.println("请输入这本书的价格");
            try {
                price = scanner.nextLine();
                priceB = new BigDecimal(price);
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                return;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (price.equals("q")) return;
            if (priceB.compareTo(BigDecimal.ZERO) == -1)
                System.out.println("输入非法，请重新输入");
        }
        int remain = -1;
        while (remain < 0) {
            System.out.println("请输入这本书的初始量");
            try {
                remain = scanner.nextInt();
            } catch (InputMismatchException e) {
                e.printStackTrace();
                if (Integer.toString(remain).equals("q")) return;
                scanner.nextLine();
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                return;
            }
            if (remain < 0)
                System.out.println("输入不合法，请重新输入");
        }


        Library.addbook(new Book(new ISBN(isbn), title, authora, priceB), remain);
        System.out.println("添加成功");


    }

    public void submenu1() {
        String flag = "4";
        do {
            printsubmenu();
            flag = read();
            if (flag.equals("1")) {
                System.out.print("请输入您要查询的ISBN编号,按回车键结束:");
                searchbyISBN();
            } else if (flag.equals("2")) {
                System.out.println("请输入您要查找的关键词,按回车键结束");
                searchbykeyword();
            } else if (flag.equals("4"))
                logout();

        } while (!flag.equals("3"));
    }

    public void searchbyISBN() {
        String find = null;
        while (!ISBN.checkIsbn(find) || Library.getBookByIsbn(new ISBN(find)) == null) {
            find = read();
            if (find.equals("q")) return;
            if (!ISBN.checkIsbn(find))
                System.out.println("这不是合法的ISBN号码！");
            else {
                Book re = Library.getBookByIsbn(new ISBN(find));
                if (re == null)
                    System.out.println("很抱歉,没有找到这本书");
                else {
                    re.print();
                    System.out.println();
                    System.out.println("1.借阅此书");
                    System.out.println("2.退出");
                    System.out.println("3.注销");
                    String flag = read();
                    if (flag.equals("1"))
                        borrow(find.replace("-", ""));
                    else if (flag.equals("2"))
                        break;
                    else if (flag.equals("3"))
                        logout();
                }
            }
        }

    }

    public void searchbykeyword() {
        String find = read();
        if (find.equals("q")) return;
        Library Library = new Library();
        List<Book> re = new ArrayList<Book>();
        re = Library.getBooksByKeyword(find);
        Collections.sort(re, new Comparator<Book>() {
            public int compare(Book b1, Book b2) {
                return b1.getIsbn().toString().compareTo(b2.getIsbn().toString());
            }
        });
        if (re.isEmpty())
            System.out.println("没有找到相关关键字的书籍!");
        else {
            System.out.println("您查询到的结果如下");
            printbypg(re);
        }
    }

    private boolean isnum(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public void signinout() {
        String flag = "";
        while (!flag.equals("3")) {
            String usrname = "";
            String passwd = "";
            System.out.println("--------------------欢迎使用图书馆-------------------");
            System.out.println("--------------------本系统输入时一律按q退出输入-------------------");
            System.out.println("--------------------在主界面按++键可以将日期往后加十天-------------------");
            System.out.println("--------------------在主界面按+键可以将日期往后加三天-------------------");
            System.out.println("今天是" + calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
            System.out.println("请选择注册或者登录");
            System.out.println("1.注册");
            System.out.println("2.登录");
            System.out.println("3.退出");
            flag = read();
            if (flag.equals("++"))
                calendar.add(Calendar.DAY_OF_YEAR, 10);
            if (flag.equals("+"))
                calendar.add(Calendar.DAY_OF_YEAR, 3);
            if (flag.equals("3")) System.exit(0);
            /*
                注册模块
             */
            boolean regflag1 = false;
            boolean regflag2 = false;
            if (flag.equals("1")) {
                while (!regflag1) {
                    System.out.println("请输入您要注册的用户名(由汉字,字母,数字组成)");
                    usrname = read();
                    if (usrname.equals("q")) break;
                    if (Pattern.matches(usrnamepat, usrname) && UserDatabase.getUserinfo(usrname) == null)
                        regflag1 = true;
                    else if (UserDatabase.getUserinfo(usrname) != null)
                        System.out.println("此用户名已存在,请更换用户名!");
                    else
                        System.out.println("用户名必须由汉字或字母或数字组成，不得含有非法字符!");
                }
                while (!regflag2) {
                    System.out.println("请输入您要设置的密码(6-16位数字和字母的组合)");
                    passwd = read();
                    if (passwd.equals("q")) break;
                    if (Pattern.matches(passwdpat, passwd))
                        regflag2 = true;
                    else
                        System.out.println("密码必须是6-16位数字和字母的组合!");
                }

                User usrnow = new RegisterUser(usrname, passwd, 0, 4, false, false, new java.sql.Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR)));
                UserDatabase.AddUser(usrnow);
                this.user = usrnow;
                mainmenu();
            }
            /*
                登录模块
             */
            boolean flaglogin = false;//当此标记为true时说明登录的密码错了，这时直接跳过输入用户名直接输入密码验证
            while (flag.equals("2")) {
                if (!flaglogin) {
                    System.out.println("请输入您要登录账户的用户名");
                    usrname = read();
                    if (usrname.equals("q")) break;
                }

                System.out.println("请输入您的密码");
                passwd = read();
                if (passwd.equals("q")) break;
                try {
                    if (UserDatabase.getUserinfo(usrname) != null && passwd.equals(UserDatabase.getUserinfo(usrname).getPassword())) {
                        this.user = UserDatabase.getUserinfo(usrname);
                        mainmenu();
                        break;
                    } else if (UserDatabase.getUserinfo(usrname) == null) {
                        System.out.println("不存在此用户!请注册");//用户名不存在的时候跳转到注册界面去
                        break;
                    } else if (UserDatabase.getUserinfo(usrname) != null && !passwd.equals(UserDatabase.getUserinfo(usrname).getPassword())) {
                        System.out.println("密码错误,请重新输入");
                        flaglogin = true;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    //判断两个日期相差了多少天
    //首先判断是否跨年了
    //如果跨年了，判断是否是闰年再加上日期
    private int getdayD(java.sql.Date date1, java.sql.Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 == year2)
            return day2 - day1;
        else {
            int daygap = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)
                    daygap += 366;
                else
                    daygap += 355;
            }
            return daygap + (day2 - day1);
        }
    }

}
