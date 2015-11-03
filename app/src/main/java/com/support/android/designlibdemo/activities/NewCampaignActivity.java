package com.support.android.designlibdemo.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        }
        else {
            Toast.makeText(this, "Internet NOT Connected, please turn on your Internet", Toast.LENGTH_SHORT).show();
        }
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
