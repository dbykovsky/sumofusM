package com.support.android.designlibdemo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseUser;

/**
 * Created by andres on 10/20/15.
 */
public class DispatchActivity extends Activity {

    public DispatchActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if there is current user info
        if (ParseUser.getCurrentUser() != null) {
            // Start an intent for the logged in activity
            startActivity(new Intent(this, MainActivity.class));
            Log.i("MyActivity", "going to CampaignsActivity " );
        } else {
            // Start and intent for the logged out activity
            startActivity(new Intent(this, Welcome_LoginActivity_or_Signup.class));
            Log.i("MyActivity", "going to Welcome_LoginActivity_or_Signup ");
        }
    }
}