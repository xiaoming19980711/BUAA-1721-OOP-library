package com.company.Usr;


import java.sql.Date;

public class Admin extends User {
    public Admin(String username, String password, int authority, int borrowtime, boolean isbantemp, boolean isbantemp1, Date dayofunlock) {
        super(username, password, authority, borrowtime, isbantemp, isbantemp1, dayofunlock);
    }
}
