/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.support.android.designlibdemo.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.models.Campaign;
import com.support.android.designlibdemo.utils.BitmapScaler;
import com.support.android.designlibdemo.utils.CustomProgress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CampaignDetailActivity extends AppCompatActivity {

    private static final int TAKE_PHOTO_CODE = 1;
    private static final int PICK_PHOTO_CODE = 2;
    private static final int CROP_PHOTO_CODE = 3;

    TextView tvCampaignText;
    ImageView ivCampaignImage;
    TextView tvGoal;
    CustomProgress customProgress;

    private Uri photoUri;
    private Bitmap photoBitmap;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        customProgress = (CustomProgress)findViewById(R.id.pbGoal);
        ivCampaignImage = (ImageView) findViewById(R.id.ivCampaighnImage);
        tvCampaignText = (TextView) findViewById(R.id.tvCampaignDetails);
        tvGoal = (TextView)findViewById(R.id.tvCampaignGoal);

        //getting intent
        Campaign campaign = (Campaign) getIntent().getSerializableExtra("camp");


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setTitle(campaign.getShortDescription());
        collapsingToolbar.getCollapsedTitleGravity();
        loadBackdrop(campaign.getImageUrl(), ivCampaignImage);

        //setting up progress bar
        customProgress.setProgressColor(getResources().getColor(R.color.green_500));
        customProgress.setProgressBackgroundColor(getResources().getColor(R.color.green_200));
        customProgress.setMaximumPercentage(calculatePercentage(campaign.getGoal(), campaign.getGoalCount()));
        customProgress.useRoundedRectangleShape(30.0f);
        customProgress.setShowingPercentage(true);
        //set text above progress
        tvCampaignText.setText(campaign.getLongDescription());

        //set goal text
        tvGoal.setText("Campaign goal: "+ String.valueOf(campaign.getGoal()));
    }

    private void loadBackdrop(final String imageUrl, final ImageView iView) {
        Picasso.with(this).load(imageUrl).into(iView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actions_detal_campaign, menu);
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PHOTO_CODE) {
                //cropPhoto(photoUri);

                photoBitmap = BitmapFactory.decodeFile(photoUri.getPath());
                Bitmap resizedImage =  BitmapScaler.scaleToFitWidth(photoBitmap, 300);
                ivCampaignImage.getAdjustViewBounds();
                ivCampaignImage.setScaleType(ImageView.ScaleType.FIT_XY);
                ivCampaignImage.setImageBitmap(resizedImage);

            } else if (requestCode == PICK_PHOTO_CODE) {

                photoUri = data.getData();
                try {
                    photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap resizedImage =  BitmapScaler.scaleToFitWidth(photoBitmap, 300);
                ivCampaignImage.getAdjustViewBounds();
                ivCampaignImage.setScaleType(ImageView.ScaleType.FIT_XY);
                ivCampaignImage.setImageBitmap(resizedImage);

                //cropPhoto(photoUri);
            } else if (requestCode == CROP_PHOTO_CODE) {
                photoBitmap = data.getParcelableExtra("data");

                ivCampaignImage.getAdjustViewBounds();
                ivCampaignImage.setScaleType(ImageView.ScaleType.FIT_XY);
                ivCampaignImage.setImageBitmap(photoBitmap);

                Toast.makeText(this, "I just took a picture", Toast.LENGTH_LONG).show();

            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_take_photo:
            {
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoUri = Uri.fromFile(getOutputMediaFile()); // create a file to save the image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); // set the image file name
                // Start the image capture intent to take photo
                startActivityForResult(intent, TAKE_PHOTO_CODE);
            }
            break;
            case R.id.action_use_existing:
            {
                // Take the user to the gallery app
                Intent photoGalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoGalleryIntent , PICK_PHOTO_CODE);
            }
            break;
            case  R.id.action_share:
            {
                //setupFacebookShareIntent();
                setupShareIntent();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setupShareIntent() {
        // Fetch Bitmap Uri locally
        ImageView ivImage = (ImageView)findViewById(R.id.ivCampaighnImage);
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(ivImage);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Title Of Test Post");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.sumofus.org");
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "send"));
        } else {
            Toast.makeText(this, "Some error occured during sharing", Toast.LENGTH_LONG).show();

        }

    }
    public void setupFacebookShareIntent() {
        ShareDialog shareDialog;
        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("SumOfUs")
                .setContentDescription(
                        "\"Title Of Test Post\"")
                .setContentUrl(Uri.parse("http://www.sumofus.org"))
                .build();

        shareDialog.show(linkContent);

        //MessageDialog.show(this, linkContent);
    }




    //this is to create picture filename
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "sumofus");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            return null;
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }



    // Returns the URI path to the Bitmap displayed in specified ImageView

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }

        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;

    }

    private float calculatePercentage(int goal, int count){
        double g = goal;
        double c = count;
        float percentage = (float) 0;
        if (count>0){
            if(goal==count){
                percentage= (float) 1;
            }else{
                percentage = (float)((c/g));
            }

        }
        return percentage;
    }
}
