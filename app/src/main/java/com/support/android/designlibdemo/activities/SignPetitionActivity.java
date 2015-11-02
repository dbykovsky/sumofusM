package com.support.android.designlibdemo.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iangclifton.android.floatlabel.FloatLabel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.models.Campaign;

import java.io.IOException;
import java.lang.CharSequence;

import java.lang.Override;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SignPetitionActivity extends AppCompatActivity {

    private Campaign campaign;
    Button btSignPetition;
    com.iangclifton.android.floatlabel.FloatLabel evFulName;
    com.iangclifton.android.floatlabel.FloatLabel evEmailAddress;
    com.iangclifton.android.floatlabel.FloatLabel evZipCode;
    TextView tvPetitionMessage;


    public String[] getGeolocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String[] s = new String[2];

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        if ((latitude < 0.0) || (longitude < 0.0) ) {
            s[0] = "";
            s[1] = "";
        }
        else {
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                s[0] = addresses.get(0).getCountryName();
                s[1] = addresses.get(0).getPostalCode();
            } catch (IOException e) {
                e.printStackTrace();
                s[0] = "";
                s[1] = "";
            }
        }
        return s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_petition);

        String[] geo = getGeolocation();
        String country = geo[0];
        String zip = geo[1];

        if (zip == "null") {
            zip = "";
        }

        //getting intent
        campaign = (Campaign) getIntent().getSerializableExtra("camp");

        //setting up spinner
        Spinner spinner = (Spinner) findViewById(R.id.spCountries);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SignPetitionActivity.this, R.array.country, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        spinner.setAdapter(adapter);

        //Find country in the array and set as default.
        int spinnerPosition = adapter.getPosition(country);
        spinner.setSelection(spinnerPosition);

        //setting up the views
       evFulName = (FloatLabel) findViewById(R.id.etFullNameSign);
       evEmailAddress = (FloatLabel)findViewById(R.id.etEmailAddress);
       evZipCode = (FloatLabel)findViewById(R.id.etZipCode);
       evZipCode.requestFocus();
       tvPetitionMessage = (TextView) findViewById(R.id.tvPetitionMessage);

        //setting petition message
        tvPetitionMessage.setText(campaign.getMessage());

        //prepopulating fields
        evZipCode.setText(zip);

       if(ParseUser.getCurrentUser().getUsername()!=null){
           evFulName.setText(ParseUser.getCurrentUser().getUsername());
       }

        if(ParseUser.getCurrentUser().getEmail()!=null){
            evEmailAddress.setText(ParseUser.getCurrentUser().getEmail());
        }

        btSignPetition = (Button) findViewById(R.id.btSignPetitoin);

        btSignPetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fields validations
                if( evFulName.getEditText().getText().length()==0)
                {
                    Toast.makeText(SignPetitionActivity.this, "Full name is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if( evEmailAddress.getEditText().getText().length()==0)
                {
                    Toast.makeText(SignPetitionActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if( evZipCode.getEditText().getText().length()==0)
                {
                    Toast.makeText(SignPetitionActivity.this, "Zip is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                //updating votes count on parse
                // Create a pointer to an object of class Point with id dlkj83d
                ParseObject campaignParse = ParseObject.createWithoutData("CampaignParse", campaign.getObjectId());
                // Set a new value on count
                campaignParse.put("count", campaign.getGoalCount()+1);
                campaignParse.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(SignPetitionActivity.this, "Thank you for signing the petition", Toast.LENGTH_LONG).show();
                            //Take back to to fragments
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            currentUser.addAllUnique("myCampaigns", Arrays.asList(campaign.getObjectId()));
                            //start main activity
                            currentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                   if(e==null){
                                       Intent i = new Intent(SignPetitionActivity.this, MainActivity.class);
                                       i.putExtra("page",2);
                                       startActivity(i);
                                   }

                                }
                            });

                        } else {
                            Toast.makeText(SignPetitionActivity.this, "Sorry we couldn't take your vote. Try again in a few minutes", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_petition, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


}
