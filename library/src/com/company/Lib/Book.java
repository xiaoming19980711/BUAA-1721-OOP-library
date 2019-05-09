package com.company.Lib;

import com.company.Exception.ISBNInvalidException;
import com.company.Exception.PriceInvalidException;
import com.company.Exception.RemainException;
import com.company.Exception.SumbException;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Book implements Cloneable {
    private ISBN isbn;
    private String title;
    private String[] author;
    private BigDecimal price = new BigDecimal("-1");
    private int remain = 0;
    private int sumb = 0;


    public Book(Book book) {
        try {
            this.isbn = book.getIsbn();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.price = book.getPrice();
            this.remain = book.getRemain();
            this.sumb = book.getSumb();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static boolean isleagal(Book book) {
        return !((book.getPrice().compareTo(BigDecimal.ZERO) == -1) || !(ISBN.checkIsbn(book.getIsbn().toString())));
    }

    public Book(ISBN isbn, String title, String[] author, BigDecimal price, int remain, int sumb) {
        this(isbn, title, author, price);
        setRemain(remain);
        setSumb(sumb);
    }

    public Book(ISBN isbn, String title, String[] author, BigDecimal price) {
        setIsbn(isbn);
        setTitle(title);
        setAuthor(author);
        setPrice(price);
    }

    public int getRemain() {
        return remain;
    }

    public int getSumb() {
        return sumb;
    }

    public void setRemain(int remain) {
        try{
            if(remain < 0)
                throw new RemainException("剩余量不对");
            else
                this.remain = remain;
        }catch (RemainException e){
            e.printStackTrace();
        }
    }

    public void setSumb(int sumb) {
        try{
            if(sumb < 0)
                throw new SumbException("剩余量不对");
            else
                this.sumb = sumb;
        }catch (SumbException e){
            e.printStackTrace();
        }

    }

    public BigDecimal getPrice() {
        return price;
    }

    public ISBN getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String[] getAuthor() {
        return author;
    }

    public void setAuthor(String[] author) {
        this.author = author.clone();
    }

    public void setIsbn(ISBN isbn) {
        if (ISBN.checkIsbn(isbn.getIsbn())) {
            this.isbn = isbn;
        } else {
            try {
                throw new ISBNInvalidException("错误的ISBN编号");
            } catch (ISBNInvalidException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) == -1) {
            try {
                throw new PriceInvalidException("价格不能小于0");
            } catch (PriceInvalidException e) {
                e.printStackTrace();
            }
        } else
            this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void print() {
        try {
            if (isbn == null)
                throw new NullPointerException("不合法的ISBN号码");
            else
                System.out.println("这本书的ISBN编号是: " + isbn);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            if (title == null)
                throw new NullPointerException("无标题信息");
            else
                System.out.println("这本书的标题是: " + title);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        try {
            if (author == null)
                throw new NullPointerException("无作者信息");
            else {
                System.out.print("这本书的作者是: ");
                for (String item : author)
                    System.out.print(item + " ");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        System.out.println();


        try {
            if (price.compareTo(BigDecimal.ZERO) == -1)
                throw new NullPointerException("无价格信息");
            else
                System.out.println("这本书的价格是: " + price);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        System.out.println("这本书的剩余量是: " + getRemain());
        System.out.println("这本书的总量是: " + getSumb());
    }

    @Override
    public Object clone() {
        Book book = null;
        try {
            book = (Book) super.clone();
            book.isbn = (ISBN) this.getIsbn().clone();
            book.author = this.getAuthor().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return book;
    }


}
