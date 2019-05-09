package com.company.Usr;

import com.company.Usr.Admin;

import java.sql.Date;

public class SuperAdmin extends Admin {
    public SuperAdmin(String username, String password, int authority, int borrowtime, boolean isbantemp, boolean isbantemp1, Date dayofunlock) {
        super(username, password, authority, borrowtime, isbantemp, isbantemp1, dayofunlock);
    }
}
