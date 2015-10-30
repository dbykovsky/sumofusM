package com.support.android.designlibdemo.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.dialogs.CameraDialog;
import com.support.android.designlibdemo.utils.BitmapScaler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    private static final int TAKE_PHOTO_CODE = 1;
    private static final int PICK_PHOTO_CODE = 2;
    private static final int CROP_PHOTO_CODE = 3;
    private Uri photoUri;
    private Bitmap photoBitmap;

    final int[] selection = new int[1];

    // UI references.
    private EditText usernameEditText;
    private EditText useremail;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;
    private Button photoButton;
    private ImageView ivProfilePicProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        // Set up the signup form.
        usernameEditText = (EditText) findViewById(R.id.username_edit_text);
        useremail = (EditText)findViewById(R.id.useremail_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        passwordAgainEditText = (EditText) findViewById(R.id.password_again_edit_text);
        passwordAgainEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.edittext_action_signup ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    signup();
                    return true;
                }
                return false;
            }
        });

        // Set up the submit button click handler
        Button mActionButton = (Button) findViewById(R.id.action_button);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                signup();
            }
        });

        //Add picture to signup page
        ivProfilePicProfile = (ImageView) findViewById(R.id.ivProfilePicProfile);


        photoButton = (Button) findViewById(R.id.photo_button);
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

    }

    private void signup() {
        final String username = usernameEditText.getText().toString().trim();
        final  String password = passwordEditText.getText().toString().trim();
        final String uemail = useremail.getText().toString().trim();
        final String passwordAgain = passwordAgainEditText.getText().toString().trim();

        // Validate the sign up data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));

        if (username.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_username));
        }

        if (uemail.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_useremail));
        }

        if (password.length() == 0) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_password));
        }
        if (!password.equals(passwordAgain)) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
        }
        validationErrorMessage.append(getString(R.string.error_end));

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setMessage(getString(R.string.progress_signup));
        dialog.show();



        //ADD IMAGE Hardcoded for now. Work in Progress
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.user_image_placeholder);
        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();

       final ParseFile file = new ParseFile("profilePicture"+ ".jpg", image);
        //file.saveInBackground();
        // Save the meal and return
        file.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // setResult(Activity.RESULT_OK);
                    // finish();
                    // Set up a new Parse user
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setEmail(uemail);
                    user.setPassword(password);
                    user.put("profilePicture", file);
                    user.saveInBackground();

                    //if user dont want to upload image we will sent, the image showsn as default..


                    // Call the Parse signup method
                    user.signUpInBackground(new

                                                    SignUpCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            dialog.dismiss();
                                                            if (e != null) {
                                                                // Show the error message
                                                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                            } else {
                                                                // Start an intent for the dispatch activity
                                                                Intent intent = new Intent(SignUpActivity.this, DispatchActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(intent);
                                                            }


                                                        }
                                                    });
                } else {

                    Log.i("SumOfUs USER info", "caca");
                }
            }

        });









    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == TAKE_PHOTO_CODE) {

                photoBitmap = BitmapFactory.decodeFile(photoUri.getPath());
                Bitmap resizedImage = BitmapScaler.scaleToFitWidth(photoBitmap, 300);
                ivProfilePicProfile.getAdjustViewBounds();
                ivProfilePicProfile.setScaleType(ImageView.ScaleType.FIT_XY);
                ivProfilePicProfile.setImageBitmap(resizedImage);
                //ivUserPic.setImageResource(currentUser.getString("zipcode"));
                //********** Update parse with image

                // Convert bitmap to a byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();

                //Add date to image
                Date targetTime = new Date();
                // Create the ParseFile with an image
                final ParseFile file = new ParseFile("posted_by_user_" + targetTime + ".jpg", image);

                // Set up a new Parse user
                ParseUser user = new ParseUser();
                user.put("profilePicture", file);
                user.saveInBackground();

            } else if (requestCode == PICK_PHOTO_CODE) {

                photoUri = data.getData();
                try {
                    photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap resizedImage = BitmapScaler.scaleToFitWidth(photoBitmap, 300);
                ivProfilePicProfile.getAdjustViewBounds();
                ivProfilePicProfile.setScaleType(ImageView.ScaleType.FIT_XY);
                ivProfilePicProfile.setImageBitmap(resizedImage);

                // Convert bitmap to a byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                // Create the ParseFile with an image

                //Add date to image
                Date targetTime = new Date();
                final ParseFile file = new ParseFile("posted_by_user_" + targetTime + ".jpg", image);

                // Set up a new Parse user
                ParseUser user = new ParseUser();

                user.put("profilePicture", file);
                user.saveInBackground();

                //getImagesUploadedByUserForCampaign(campaign.getObjectId());
            } else if (requestCode == CROP_PHOTO_CODE) {
                photoBitmap = data.getParcelableExtra("data");

                ivProfilePicProfile.getAdjustViewBounds();
                ivProfilePicProfile.setScaleType(ImageView.ScaleType.FIT_XY);
                ivProfilePicProfile.setImageBitmap(photoBitmap);
                Toast.makeText(this, "I just took a picture", Toast.LENGTH_LONG).show();

            }
        }
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
}