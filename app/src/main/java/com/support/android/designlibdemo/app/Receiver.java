package com.support.android.designlibdemo.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;
import com.support.android.designlibdemo.activities.CampaignDetailActivity;
import com.support.android.designlibdemo.activities.DispatchActivity;
import com.support.android.designlibdemo.adapters.CampaignRecyclerViewAdapter;

public class Receiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushOpen(Context context, Intent intent) {
        //To track "App Opens"
        ParseAnalytics.trackAppOpenedInBackground(intent);

        //Here is data you sent
       // Log.i("DEBUG: Data receved from Push notification. ", intent.getExtras().getString("com.parse.Data"));

        Intent i = new Intent(context, DispatchActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
