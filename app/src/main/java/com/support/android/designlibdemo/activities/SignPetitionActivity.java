package com.support.android.designlibdemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.support.android.designlibdemo.R;

import java.lang.CharSequence;

import java.lang.Override;

public class SignPetitionActivity extends AppCompatActivity {

    Button btSignPetition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_petition);

        Spinner spinner = (Spinner) findViewById(R.id.spCountries);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SignPetitionActivity.this, R.array.country, android.R.layout.simple_spinner_item);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        spinner.setAdapter(adapter);

        btSignPetition = (Button) findViewById(R.id.btSignPetitoin);

        btSignPetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignPetitionActivity.this, "Thank you for signing the petition", Toast.LENGTH_LONG).show();

                //Take back to fragments
                //Intent i = new Intent(SignPetitionActivity.this, CampaignsActivity.class);
                //startActivity(i);

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
