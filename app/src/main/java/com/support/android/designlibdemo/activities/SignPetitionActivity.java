package com.support.android.designlibdemo.activities;

import android.content.Intent;
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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.models.Campaign;

import org.w3c.dom.Text;

import java.lang.CharSequence;

import java.lang.Override;

public class SignPetitionActivity extends AppCompatActivity {

    private Campaign campaign;
    Button btSignPetition;
    EditText evFulName;
    EditText evEmailAddress;
    EditText evZipCode;
    TextView tvPetitionMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_petition);

        //getting intent
        campaign = (Campaign) getIntent().getSerializableExtra("camp");

        //setting up spinner
        Spinner spinner = (Spinner) findViewById(R.id.spCountries);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SignPetitionActivity.this, R.array.country, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        spinner.setAdapter(adapter);

        //setting up the views
       evFulName = (EditText) findViewById(R.id.etFullNameSign);
       evEmailAddress = (EditText)findViewById(R.id.etEmailAddress);
       evZipCode = (EditText)findViewById(R.id.etZipCode);
       tvPetitionMessage = (TextView) findViewById(R.id.tvPetitionName);
       tvPetitionMessage.setText(campaign.getMessage());

        //prepopulating fields
       if(ParseUser.getCurrentUser().getUsername()!=null){
           evFulName.setText(ParseUser.getCurrentUser().getUsername());
       }

        if(ParseUser.getCurrentUser().getEmail()!=null){
            evEmailAddress.setText(ParseUser.getCurrentUser().getEmail());
        }
        if(ParseUser.getCurrentUser().getEmail()!=null){
            evZipCode.setText(ParseUser.getCurrentUser().getString("zipcode"));
        }

        btSignPetition = (Button) findViewById(R.id.btSignPetitoin);

        btSignPetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fields validations
                if( evFulName.getText().length()==0)
                {
                    Toast.makeText(SignPetitionActivity.this, "Full name is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if( evEmailAddress.getText().length()==0)
                {
                    Toast.makeText(SignPetitionActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if( evZipCode.getText().length()==0)
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
                            Intent i = new Intent(SignPetitionActivity.this, MainActivity.class);
                            startActivity(i);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
