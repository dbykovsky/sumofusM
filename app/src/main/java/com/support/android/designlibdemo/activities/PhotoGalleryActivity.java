package com.support.android.designlibdemo.activities;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.adapters.PhotogalleryAdapter;
import com.support.android.designlibdemo.utils.DeviceDimensionsHelper;

import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryActivity extends AppCompatActivity {

    private static final String ITENT_TAG= "camp";

    PhotogalleryAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    ArrayList<String> imageUrls = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        imageUrls = getIntent().getExtras().getStringArrayList(ITENT_TAG);

        mCustomPagerAdapter = new PhotogalleryAdapter(this, imageUrls);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }


}

