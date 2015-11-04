package com.support.android.designlibdemo.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.fragments.NewCampaignFragment;
import com.support.android.designlibdemo.models.CampaignParse;


public class NewCampaignActivity extends AppCompatActivity {

    private CampaignParse campaign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        campaign = new CampaignParse();
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //   getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //   requestWindowFeature(Window.FEATURE_ACTION_BAR);
        //   getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        super.onCreate(savedInstanceState);

        // Begin with main data entry view
        setContentView(R.layout.activity_new_campaign);
        //Check Internet connection
        if (isNetworkAvailable()) {
            FragmentManager manager = getFragmentManager();
            Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

            if (fragment == null) {
                fragment = new NewCampaignFragment();
                manager.beginTransaction().add(R.id.fragmentContainer, fragment)
                        .commit();
            }
        } else {
            Toast.makeText(this, "Internet NOT Connected, please turn on your Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_campaign_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            overridePendingTransition(R.anim.out_slide_out_top, R.anim.out_slide_in_bottom);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public CampaignParse getCurrentCampaign() {
        return campaign;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.out_slide_out_top, R.anim.out_slide_in_bottom);
    }
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

    }
}
