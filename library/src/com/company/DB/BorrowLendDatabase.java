package com.company.DB;

import com.company.Lib.ISBN;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BorrowLendDatabase {
    public class bookstatus{
        private ISBN isbn;
        private int dayofyear;
        private int year;
        private int day;
        private int month;
        private int status = 0;//0为正常在借，1为已归还，2为超期未还，3为超期归还
        private int dayofreturn;
        private boolean isxued = false;

        public void setIsxued(boolean isxued){
            this.isxued = isxued;
        }

        public boolean getIsxued(){
            return isxued;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCalender() {
            return dayofyear;
        }

        public int getStatus() {
            return status;
        }

        public ISBN getIsbn() {
            return isbn;
        }

        public int getDay() {
            return day;
        }

        public int getDayofyear() {
            return dayofyear;
        }

        public int getMonth() {
            return month;
        }

        public int getYear() {
            return year;
        }

        public int getDayofreturn() {
            return dayofreturn;
        }

        public void setDayofreturn(int dayofreturn) {
            this.dayofreturn = dayofreturn;
        }


        public bookstatus(ISBN isbn, Calendar calendar, int status){
            this.dayofyear = calendar.get(Calendar.DAY_OF_YEAR);
            this.status = status;
            this.isbn = isbn;
            this.year = calendar.get(Calendar.YEAR);
            this.month = calendar.get(Calendar.MONTH) + 1;
            this.day = calendar.get(Calendar.DAY_OF_MONTH);
            this.dayofreturn = this.dayofyear + 30;
        }
    }
    private static HashMap<String, List<bookstatus>> BorrowLend = new HashMap<>();

    public static HashMap<String, List<bookstatus>> getBorrowLend() {
        return BorrowLend;
    }


}
