package com.support.android.designlibdemo.app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParsePush;

public class ParseUser extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.


        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this);

        com.parse.ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
