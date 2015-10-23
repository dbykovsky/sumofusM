package com.support.android.designlibdemo.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/*
* This is the 'current' app user.
* An user can be a standard user or an administrator
*
* Sum of Us defines a Super-user, as one that can add pictures to campaings. We'll eventually implement that feature.
* */

@ParseClassName("User")
public class User extends ParseObject {

    public String getName() {
        return "";
    }

}