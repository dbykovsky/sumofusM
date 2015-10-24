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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.models.Campaign;
import com.support.android.designlibdemo.utils.CustomProgress;

public class CampaignDetailActivity extends AppCompatActivity {

    private static final int TAKE_PHOTO_CODE = 1;
    private static final int PICK_PHOTO_CODE = 2;
    private static final int CROP_PHOTO_CODE = 3;
    private static final int POST_PHOTO_CODE = 4;

    TextView tvCampaignText;
    ImageView ivCampaignImage;
    TextView tvGoal;

    CustomProgress customProgress;

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
