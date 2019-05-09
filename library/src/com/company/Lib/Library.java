
package com.company.Lib;

import com.company.DB.DBQuery;
import com.company.Exception.AddBookInvalidException;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Library {
    public static Book getBookByIsbn(ISBN isbn) {
        String sql = "select * from Book where ISBN=\""  + isbn.toString() + "\"";
        ResultSet re = DBQuery.Query(sql);
        try {
            re.next();
            return new Book(
                    new ISBN(re.getString("ISBN")),
                    re.getString("Title"),
                    re.getString("Author").split(" "),
                    re.getBigDecimal("Price"),
                    re.getInt("remain"),
                    re.getInt("sumb")
            );
        }catch (NullPointerException e){
            //e.printStackTrace();
            return null;
        }
        catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
    }

    public static List<Book> getBooksByKeyword(String keyword) {
        List<Book> bks = new ArrayList<Book>();
        String sql = null;
        if(!keyword.isEmpty())
            sql = "select * from Book where Title like '%" + keyword + "%'";
        else
            sql = "select * from Book";
        ResultSet re = DBQuery.Query(sql);
        try {
            while (re.next()) {
                bks.add(new Book(
                        new ISBN(re.getString("ISBN")),
                        re.getString("Title"),
                        re.getString("Author").split(" "),
                        re.getBigDecimal("Price"),
                        re.getInt("remain"),
                        re.getInt("sumb")
                ));
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
        return bks;
    }

    //拼接字符串
    private static String join(String[] arr) {
        StringBuilder re = new StringBuilder();
        re.append(arr[0]);
        for (int i = 1; i < arr.length; i++) {
            re.append(" ");
            re.append(arr[i]);
        }
        return re.toString();
    }

    //不存在书时添加一本书
    private static void addone(Book book) {
        try {
            if (Book.isleagal(book)) {
                String sql = String.format(
                        "insert into Book (ISBN, Title, Author, Price, remain, sumb) values ('%s', '%s', '%s', '%s', %d , %d)",
                        book.getIsbn().toString(), book.getTitle(), join(book.getAuthor()), book.getPrice().toString(),
                        1, 1);
                DBQuery.Update(sql);
            } else {
                throw new AddBookInvalidException("添加的这本书信息有误");
            }
        } catch (AddBookInvalidException e) {
            e.printStackTrace();
        }
    }

    private static boolean trybooknull(Book book) {
        try {
            if (book == null) {
                throw new NullPointerException("添加的书本不能为空");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return (book == null);
    }


    //添加一本书
    public static void addbook(Book book) {
        if (trybooknull(book))
            return;
        Book now = getBookByIsbn(book.getIsbn());
        if (now == null) {
            addone(book);
        } else {
            String sql = "UPDATE Book set remain = remain + 1, sumb = sumb + 1 where ISBN="
                    + book.getIsbn().toString();
            DBQuery.Update(sql);
        }
    }


    //添加num本书
    public static void addbook(Book book, int num) {
        if (trybooknull(book))
            return;
        Book now = getBookByIsbn(book.getIsbn());
        if (now == null) {
            try {
                if (Book.isleagal(book)) {
                    try{
                        String sql = String.format(
                                "insert into Book (ISBN, Title, Author, Price, remain, sumb) values ('%s', '%s', '%s', '%s', %d , %d)",
                                book.getIsbn().toString(), book.getTitle(), join(book.getAuthor()), book.getPrice().toString(),
                                num, num);
                        DBQuery.Update(sql);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                } else {
                    throw new AddBookInvalidException("添加的这本书信息有误");
                }
            } catch (AddBookInvalidException e) {
                e.printStackTrace();
            }
        } else {
            String sql = "UPDATE Book set remain = remain +" + num + ", sumb = sumb + " + num + " where ISBN=\"" + book.getIsbn().toString() + "\"";
            DBQuery.Update(sql);
        }
    }

    public static void setbookTitle(ISBN isbn, String title) {
        Book find = getBookByIsbn(isbn);
        try {
            if (find == null)
                throw new NullPointerException("没找到这本书");
            else{
                String sql = "update Book set Title=\"" + title + "\"" + " where ISBN=\"" + isbn.toString() + "\"";
                DBQuery.Update(sql);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void setbookAuthor(ISBN isbn, String[] author) {
        Book find = getBookByIsbn(isbn);
        try {
            if (find == null)
                throw new NullPointerException("没找到这本书");
            else{
                String sql = "update Book set Author=\"" + join(author) + "\"" + " where ISBN=\"" + isbn.toString() + "\"";
                DBQuery.Update(sql);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static boolean setbookISBN(ISBN isbn, ISBN newisbn) {
        Book find1 = getBookByIsbn(isbn);
        Book find2 = getBookByIsbn(newisbn);
        try {
            if (find1 == null)
                throw new Exception("没找到这本书");
            else if (find2 != null)
                throw new Exception("此ISBN已有");
            else {
                String sql = "update Book set ISBN=\"" + newisbn + "\"" + " where ISBN=\"" + isbn + "\"";
                DBQuery.Update(sql);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setbookPrice(ISBN isbn, BigDecimal price) {
        Book find = getBookByIsbn(isbn);
        try {
            if (find == null)
                throw new NullPointerException("没找到这本书");
            else{
                String sql = "update Book set Price=\"" + price.toString() + "\"" + " where ISBN=\"" + isbn.toString() + "\"";
                DBQuery.Update(sql);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void setbookRemain(ISBN isbn, int remain) {
        try {
            if (getBookByIsbn(isbn) == null)
                throw new Exception("没有这本书");
            else if (remain < 0)
                throw new Exception("库存设置不能为负数");
            else{
                String sql = "update Book set remain='" + remain + "' where ISBN=\"" + isbn.toString() + "\"";
                DBQuery.Update(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setbookSum(ISBN isbn, int sum) {
        try {
            if (getBookByIsbn(isbn) == null)
                throw new Exception("没有这本书");
            else if (sum < 0)
                throw new Exception("总量设置不能为负数");
            else{
                String sql = "update Book set sumb='" + sum + "' where ISBN=\"" + isbn.toString() + "\"";
                DBQuery.Update(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}




