package com.support.android.designlibdemo.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.adapters.PhotogalleryAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryActivity extends AppCompatActivity {

    private static final String ITENT_TAG= "camp";

    PhotogalleryAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    CirclePageIndicator circleIndicator;
    ArrayList<String> imageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        imageUrls = getIntent().getExtras().getStringArrayList(ITENT_TAG);


        mCustomPagerAdapter = new PhotogalleryAdapter(this, imageUrls);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(mViewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onBackPressed() {
        finish();
    }


}

