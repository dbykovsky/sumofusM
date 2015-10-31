package com.support.android.designlibdemo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.dialogs.CameraDialog;
import com.support.android.designlibdemo.utils.BitmapScaler;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class UserProfileActivity extends AppCompatActivity {
    final String userProfilePhotoUrl = "https://scontent.fsnc1-1.fna.fbcdn.net/hprofile-xfp1/v/t1.0-1/c0.0.480.480/p480x480/1470006_10202041396829567_1353019578_n.jpg?oh=2b848931575064640d24719cfd9a0eeb&oe=568F11E8";
    private static final int MY_SCAN_REQUEST_CODE = 765;

    private String creditCardNumber=null;
    private String creditCardExperation=null;

    private static final int TAKE_PHOTO_CODE = 1;
    private static final int PICK_PHOTO_CODE = 2;
    private static final int CROP_PHOTO_CODE = 3;
    ImageView ivUserProfile, ivUserPic;
    TextView userName;
    EditText userEmail;
    EditText userPhoneNumber;
    EditText userSite;
    TextView tvCreditCardNumber;
    TextView tvCreditCardExperation;
    Button btAddCrefirCard;
    private Uri photoUri;
    private Bitmap photoBitmap;

    final int[] selection = new int[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ivUserPic = (ImageView) findViewById(R.id.ivProfilePicProfile);
        userName = (TextView) findViewById(R.id.tv_userNameDrawer);
        userEmail = (EditText) findViewById(R.id.tv_userEmail);
        userPhoneNumber = (EditText) findViewById(R.id.tv_userPhone);
        userSite = (EditText) findViewById(R.id.tv_userSite);
        tvCreditCardNumber = (TextView) findViewById(R.id.tv_cc_number);
        tvCreditCardExperation = (TextView) findViewById(R.id.tv_cc_experation);
        btAddCrefirCard = (Button) findViewById(R.id.bt_addCreditCard);

        final ParseUser currentUser = ParseUser.getCurrentUser();

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
        int i = 0;
        String contributions = ", welcome!"+"\n";
        if (ar != null ) {
            i = ar.length();
        }
        if (i>1) {
           // ivUserProfile.setImageResource(R.drawable.profileactivist);
            contributions = ", you have supported"+"\n" + i + " campaigns. Thank you!";
        }
        else
        {
            // ivUserProfile.setImageResource(R.drawable.iconbaby);
        }

        //Populate current UserName
        Log.i("SumOfUs USER info", currentUser.getUsername());
        userName.setText(currentUser.getUsername() + contributions);
        userEmail.setText(currentUser.getEmail());
        userPhoneNumber.setText(currentUser.getString("phoneNumber"));
        userSite.setText(currentUser.getString("webSite"));

        //Load image from Parse
        ParseFile image = (ParseFile) currentUser.getParseFile("profilePicture");

//call the function
        if(image!=null){
            image.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ivUserPic.setImageBitmap(bmp);

                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }

        //Camera
        Button photoButton = (Button) findViewById(R.id.photo_button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();
                final CameraDialog dialog = CameraDialog.newInstance("Add a new picture:");

                dialog.setOnChoiceClickListener(new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selection[0] = which;
                    }
                });

                dialog.setPositiveListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (selection[0] == 0) {
                            // create Intent to take a picture and return control to the calling application
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            photoUri = Uri.fromFile(getOutputMediaFile()); // create a file to save the image
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); // set the image file name
                            // Start the image capture intent to take photo
                            startActivityForResult(intent, TAKE_PHOTO_CODE);
                        } else {
                            // Take the user to the gallery app to pick a photo
                            Intent photoGalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(photoGalleryIntent, PICK_PHOTO_CODE);

                        }
                        dialog.dismiss();

                    }
                });

                dialog.setCancelClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }

                });

                dialog.show(fm, "TAG_DIALOG");

            }

        });

        //Update User's profile
        userEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //TextView myOutputBox = (TextView) findViewById(R.id.myOutputBox);
                //myOutputBox.setText(s);
                //Save changes to Parse one the user stop editing
                //Save on backPress()
                String newEmail = s.toString();
                Log.i("SumOfUs USER info", newEmail);
                currentUser.setEmail(newEmail);

            }
        });

        //Update User's phone
        userPhoneNumber.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //TextView myOutputBox = (TextView) findViewById(R.id.myOutputBox);
                //myOutputBox.setText(s);
                //Save changes to Parse one the user stop editing
                //Save on backPress()
                String newPhone = s.toString();
              //  currentUser.put("profilePicture", file);
                Log.i("SumOfUs USER info", newPhone);
                currentUser.put("phoneNumber",newPhone);

            }
        });

        //Update User's Website
        userSite.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //TextView myOutputBox = (TextView) findViewById(R.id.myOutputBox);
                //myOutputBox.setText(s);
                //Save changes to Parse one the user stop editing
                //Save on backPress()
                String webSite = s.toString();
                //  currentUser.put("profilePicture", file);
                Log.i("SumOfUs USER info", webSite);
                currentUser.put("webSite",webSite);

            }
        });

        currentUser.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // myObjectSavedSuccessfully();
                    Log.i("SumOfUs USER info", "Saveeee");
                } else {
                    //myObjectSaveDidNotSucceed();
                    Log.i("SumOfUs USER info", "myObjectSaveDidNotSucceed");
                }
            }
        });

    }

    //this is to create picture filename
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "sumofus");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null;
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return mediaFile;
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == TAKE_PHOTO_CODE) {

                photoBitmap = BitmapFactory.decodeFile(photoUri.getPath());
                Bitmap resizedImage = BitmapScaler.scaleToFitWidth(photoBitmap, 300);
                ivUserPic.getAdjustViewBounds();
                ivUserPic.setScaleType(ImageView.ScaleType.FIT_XY);
                //ivUserPic.setImageResource(currentUser.getString("zipcode"));
                //********** Update parse with image

                // Convert bitmap to a byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();

                Date targetTime = new Date();
                // Create the ParseFile with an image
                final ParseFile file = new ParseFile(targetTime+"_"+ParseUser.getCurrentUser().getUsername() + ".jpg", image);

                //posting an image file with campaign id to Parse to Images object
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.put("profilePicture", file);
                currentUser.saveInBackground();
                // getImagesUploadedByUserForCampaign(campaign.getObjectId());

            } else if (requestCode == PICK_PHOTO_CODE) {

                photoUri = data.getData();
                try {
                    photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap resizedImage = BitmapScaler.scaleToFitWidth(photoBitmap, 300);
                ivUserPic.getAdjustViewBounds();
                ivUserPic.setScaleType(ImageView.ScaleType.FIT_XY);
                //********** Update parse with image
                //ivCampaignImage.setImageBitmap(resizedImage);

                // Convert bitmap to a byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();

                Date targetTime = new Date();
                // Create the ParseFile with an image
                final ParseFile file = new ParseFile(targetTime+"_"+ParseUser.getCurrentUser().getUsername() + ".jpg", image);

                //posting an image file with campaign id to Parse to Images object
                // ParseObject photoPost = new ParseObject("Images");
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.put("profilePicture", file);
                currentUser.saveInBackground();
                //getImagesUploadedByUserForCampaign(campaign.getObjectId());
            } else if (requestCode == CROP_PHOTO_CODE) {
                photoBitmap = data.getParcelableExtra("data");

                ivUserPic.getAdjustViewBounds();
                ivUserPic.setScaleType(ImageView.ScaleType.FIT_XY);
                ivUserPic.setImageBitmap(photoBitmap);
                Toast.makeText(this, "I just took a picture", Toast.LENGTH_LONG).show();

            }
        }

        if (resultCode == MY_SCAN_REQUEST_CODE) {
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
                    Toast.makeText(this, "My experation is" + creditCardExperation, Toast.LENGTH_SHORT).show();
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            } else {
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
    }

    @Override
    protected void onResume() {
        super.onResume();

    }




    //Show alert dialog if network is not awailable
    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
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

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.out_slide_in_left, R.anim.out_slide_out_right);
    }


}