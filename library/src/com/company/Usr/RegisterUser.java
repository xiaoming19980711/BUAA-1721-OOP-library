package com.company.Usr;

import com.company.Usr.User;

import java.sql.Date;

public class RegisterUser extends User {
    public RegisterUser(String username, String password, int authority, int borrowtime, boolean isbantemp, boolean isbantemp1, Date dayofunlock) {
        super(username, password, authority, borrowtime, isbantemp, isbantemp1, dayofunlock);
    }
}
