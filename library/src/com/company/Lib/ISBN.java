package com.company.Lib;

public class ISBN implements Cloneable{
    private String Isbn;
    public ISBN(String isbn){
        try {
            if(checkIsbn(isbn))
                this.Isbn=isbn.replace("-","");
            else
                throw new IllegalArgumentException("不合法的ISBN编号");
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

    }
    public static boolean checkIsbn(String isbn){
        if(isbn==null) return false;
        String isbn_del=isbn.replace("-","");
        if(isbn_del.length()==10)
            return checkIsbn10(isbn_del);
        else if(isbn_del.length()==13)
            return checkIsbn13(isbn_del);
        else
            return false;
    }
    static boolean checkIsbn10(String isbn){
        for(int i=0;i<8;i++)
            if( !Character.isDigit(isbn.charAt(i))) return false;
        if(!Character.isDigit(isbn.charAt(9))&&isbn.charAt(9)!='X') return false;
        int re=0;
        for(int i=0;i<9;i++)
            re+=((10-i)*(isbn.charAt(i)-'0'));
        if(isbn.charAt(9)=='X') re+=10;
        else re+=(isbn.charAt(9)-'0');
        if(re%11==0) return true;
        else return false;
    }

    static boolean checkIsbn13(String isbn){
        if(!(isbn.charAt(0)=='9'&&isbn.charAt(1)=='7'&&isbn.charAt(2)=='8')&&!(isbn.charAt(0)=='9'&&isbn.charAt(1)=='7'&&isbn.charAt(2)=='9'))
            return false;
        for(int i=0;i<13;i++)
            if( !Character.isDigit(isbn.charAt(i))) return false;
         int re=0;
         int flag=1;
         for(int i=0;i<13;i++){
             re+=((isbn.charAt(i)-'0')*flag);
             if(flag==1) flag=3;
             else if(flag==3) flag=1;
         }
         if(re%10==0) return true;
         else return false;
    }
    @Override
    public String toString() {
        return Isbn;
    }

    public String getIsbn() {
        return Isbn;
    }

    @Override
    public Object clone(){
        ISBN isbn = null;
        try {
            isbn = (ISBN) super.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return isbn;
    }
}
