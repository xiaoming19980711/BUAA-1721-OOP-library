package com.company;


import com.company.Console.Console;
import com.company.DB.DBQuery;

public class Main {
    public static void main(String[] args) {
        DBQuery.connect();
        Console console = new Console();
        //Library Library = new Library();
        console.signinout();
        DBQuery.close();
    }
}
