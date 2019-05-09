package com.company.Tester;

import com.company.Lib.Book;
import com.company.Lib.ISBN;
import com.company.Lib.Library;

import java.math.BigDecimal;
import java.util.List;

public class LibraryTest {

    public static void main(String[] args) {
        //新建图书馆
       Library Library = new Library();
       //新建12本书,其中有一本书的ISBN号码无效
        Book book1 = new Book(new ISBN("978-7-04-046847-2"),"点子线路",new String[]{"张晓林"},new BigDecimal("25.2"));
        Book book2 = new Book(new ISBN("978-7-5443-6933-6"),"余罪",new String[]{"常书欣"},new BigDecimal("42.255"));
        Book book3 = new Book(new ISBN("978-7-80655-337-4"),"天龙八部",new String[]{"金庸"},new BigDecimal("108.00"));
        Book book4 = new Book(new ISBN("7-01-000922-8"),"毛选",new String[]{"毛泽东"},new BigDecimal("13.00"));
        Book book5 = new Book(new ISBN("7-81077-459-X"),"飞行器环境控制",new String[]{"寿荣中","何慧姗"},new BigDecimal("35.25"));
        Book book6 = new Book(new ISBN("7-04-006442-1"),"弹性力学",new String[]{"杨桂通"},new BigDecimal("17.10"));
        Book book7 = new Book(new ISBN("7-81012-747-0"),"应用计算流体力学",new String[]{"朱自强"},new BigDecimal("20.00"));
        Book book8 = new Book(new ISBN("7-04-007981-x"),"信号与系统",new String[]{"刘子明"},new BigDecimal("31.255"));
        Book book9 = new Book(new ISBN("978-7-04-047328-5"),"嵌入式",new String[]{"张晓林"},new BigDecimal("75.4255"));
        Book book10 = new Book(new ISBN("978-7-5123-5530-9"),"Python",new String[]{"Mark","任发科"},new BigDecimal("198.00"));
        Book book11 = new Book(new ISBN("978-7-80655-335-0"),"倚天屠龙记",new String[]{"金庸"},new BigDecimal("86"));
        Book book12 = new Book(new ISBN("978-7-229-03093-3"),"三体",new String[]{"刘慈欣"},new BigDecimal("38"));
        //向图书馆添加毛选和三体
        Library.addbook(book4);
        Library.addbook(book12,12);
        //用ISBN从图书馆里查找三体
        Book find = Library.getBookByIsbn(new ISBN("978-7-229-03093-3"));
        find.print();
        //用关键字查找力学相关书籍
        Library.addbook(book6);
        Library.addbook(book7);
        List<Book> find1 = Library.getBooksByKeyword("力学");
        for(Book book : find1)
            book.print();

    }
}
