package com.support.android.designlibdemo.ParseApplication;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;
import com.support.android.designlibdemo.models.CampaignParse;
import com.support.android.designlibdemo.models.User;

/**
 * Created by andres on 10/23/15.
 */
public class ParseApp extends Application {
   // public static final String YOUR_APPLICATION_ID = "LCPA3eHqb1CyiP2pmZGdDXw6I54pZKNCoxTGAsTt";
   // public static final String YOUR_CLIENT_KEY = "hweFBlma45UrDKmcRNSlcdG7Fidp6lST0Siu8tcx";

    @Override
    public void onCreate() {
        super.onCreate();

        //if we don't have this statement it will crash the APP
        if (!ParseCrashReporting.isCrashReportingEnabled()) {

            setupParse();

        }}

    public void setupParse() {
        Log.i("MyActivity", "setupParse()  ... ");
        // Local Datastore.
        Parse.enableLocalDatastore(this);

        // Register your parse models here
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(CampaignParse.class);

        //Parse.initialize(this, getApplicationContext().getString(R.string.parse_app_id), getApplicationContext().getString(R.string.parse_client_key));
        Parse.initialize(this);

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);

    }
}