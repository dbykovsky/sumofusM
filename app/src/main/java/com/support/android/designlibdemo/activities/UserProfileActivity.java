package com.support.android.designlibdemo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.support.android.designlibdemo.R;

import org.json.JSONArray;
import org.w3c.dom.Text;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class UserProfileActivity extends AppCompatActivity {
    final String userProfilePhotoUrl = "https://scontent.fsnc1-1.fna.fbcdn.net/hprofile-xfp1/v/t1.0-1/c0.0.480.480/p480x480/1470006_10202041396829567_1353019578_n.jpg?oh=2b848931575064640d24719cfd9a0eeb&oe=568F11E8";
    private static final int MY_SCAN_REQUEST_CODE = 765;

    private String creditCardNumber=null;
    private String creditCardExperation=null;


    ImageView ivUserProfile;
    TextView userName;
    EditText userEmail;
    EditText userPhoneNumber;
    TextView tvCreditCardNumber;
    TextView tvCreditCardExperation;
    Button btAddCrefirCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ivUserProfile = (ImageView) findViewById(R.id.ivProfilePicProfile);
        userName = (TextView) findViewById(R.id.tv_userNameDrawer);
        userEmail = (EditText) findViewById(R.id.tv_userEmail);
        userPhoneNumber = (EditText) findViewById(R.id.tv_userPhone);
        tvCreditCardNumber = (TextView) findViewById(R.id.tv_cc_number);
        tvCreditCardExperation = (TextView) findViewById(R.id.tv_cc_experation);
        btAddCrefirCard = (Button) findViewById(R.id.bt_addCreditCard);

        ParseUser currentUser = ParseUser.getCurrentUser();

        //if user has CC attached, then show it and hide ADD CC BUTTON
        if(currentUser.getString("creditNumber")!=null){
            tvCreditCardNumber.setText(currentUser.getString("creditNumber"));
            tvCreditCardExperation.setText(currentUser.getString("creditExpr"));
        }else
        {
            btAddCrefirCard.setVisibility(View.VISIBLE);
            btAddCrefirCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onScanPress();
                }
            });
        }

        //making user profile photo oval
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(this.getResources().getColor(R.color.grey_200))
                .borderWidthDp(3)
                .cornerRadiusDp(100)
                .oval(false)
                .build();

        //setting user profile photo
       /* Picasso.with(this)
                .load(userProfilePhotoUrl).resize(400, 400)
                .transform(transformation)
                .into(ivUserProfile);

*/
        JSONArray ar = currentUser.getJSONArray("myCampaigns");

        int i = ar.length();
        String contributions = ", welcome!";

        if (i>1) {
            ivUserProfile.setImageResource(R.drawable.profileactivist);
            contributions = ", you have supported " + i + " campaigns. Thank you!";
        }
        else
        {
            ivUserProfile.setImageResource(R.drawable.iconbaby);
        }

        //Populate current UserName
        Log.i("SumOfUs USER info", currentUser.getUsername());
        userName.setText(currentUser.getUsername() + contributions);
        userEmail.setText(currentUser.getEmail());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            //return true;
            LogOut();
        }

        return super.onOptionsItemSelected(item);
    }

    public void LogOut() {
        ParseUser.logOut();
        Intent intent = new Intent(UserProfileActivity.this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onScanPress() {
        Intent scanIntent = new Intent(this, CardIOActivity.class);
        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


      //  if (resultCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                creditCardNumber = scanResult.getRedactedCardNumber();

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";

                    creditCardExperation = scanResult.expiryMonth + "/" + scanResult.expiryYear;
                    Toast.makeText(this, "My experation is"+creditCardExperation, Toast.LENGTH_SHORT).show();
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            //display in the text view
            tvCreditCardNumber.setText(creditCardNumber);
            tvCreditCardExperation.setText(creditCardExperation);
            //upload to parse for current user

        // Set up a new Parse user
        ParseUser user = ParseUser.getCurrentUser();
        user.put("creditNumber", creditCardNumber);
        user.put("creditExpr", creditCardExperation);
        // Call the Parse save method
        user.saveInBackground();
        //disable add button
        btAddCrefirCard.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    //Show alert dialog if network is not awailable
    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c,android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        builder.setTitle("Payment method");
        builder.setMessage("Would you like to add a payment \n" +
                "since no payment attached");
        builder.setIcon(R.mipmap.ic_launcher_sou);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        return builder;
    }


}
