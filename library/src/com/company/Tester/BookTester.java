package com.company.Tester;
import com.company.Lib.ISBN;
import com.company.Lib.Book;


import java.math.BigDecimal;

public class BookTester {
    public static void main(String[] args) {
        //创建12本书，其中有一本书的ISBN编号不对
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
        //输出毛选
        book4.print();
        //修改三体的ISBN编号并输出
        book12.setIsbn(new ISBN("7-04-007981-X"));
        book12.print();
        //修改三体的作者并输出
        book12.setAuthor(new String[]{"郑君里","应启珩","杨为理"});
        book12.print();
        //修改三体的价格并输出
        //修改正确价格
        book12.setPrice(new BigDecimal("1213"));
        book12.print();
        //修改错误价格
        book12.setPrice(new BigDecimal("-55"));
        //修改三体的标题并输出
        book12.setTitle("四体");
        book12.print();



    }
}
