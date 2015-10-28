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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParsePush;
import com.parse.ParseUser;
import com.support.android.designlibdemo.dialogs.CameraDialog;
import com.support.android.designlibdemo.dialogs.DonationDialog;
import com.support.android.designlibdemo.dialogs.FragmentDialogOptionsPicker;
import com.support.android.designlibdemo.fragments.CampaignsFragment;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.fragments.VideosFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ParseUser currentUser;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Populate current UserName
        currentUser = ParseUser.getCurrentUser();
        // Skip if already subscribed
        subscribeUserToChannel();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        ((TextView) nav_header.findViewById(R.id.tv_userNameDrawer)).setText(currentUser.getUsername() + "'s dashboard");
        navigationView.addHeaderView(nav_header);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewCampaign();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new CampaignsFragment(), "Campaigns");
        adapter.addFragment(new VideosFragment(), "Watch");
        adapter.addFragment(new CampaignsFragment(), "I supported");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                Intent profileActivity = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(profileActivity);
                break;
            case R.id.nav_donate:
                //if payment is attached
                if(currentUser.getString("creditNumber")!=null){

                    final int[] selection = new int[1];
                    FragmentManager fm = getSupportFragmentManager();

                    final DonationDialog donationDialog = DonationDialog.newInstance("Please donate:");

                    donationDialog.setOnChoiceClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selection[0] = which;
                        }
                    });

                    donationDialog.setPositiveListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Thank you for your donation of " + FragmentDialogOptionsPicker.donationOptionsPicker[selection[0]], Toast.LENGTH_SHORT).show();
                        }
                    });

                    donationDialog.setCancelClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Well, may be next time", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    });

                    donationDialog.show(fm, "TAG_DIALOG");

                }else
                //if there is NO payment info attached
                {
                    buildDialog(this).show();

                }

                break;
            case R.id.nav_about_us:
                //This is to show modal about us org
                buildDialogAboutUs(this).show();
        }


        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    private void createNewCampaign() {
        Intent i = new Intent(this, NewCampaignActivity.class);
        startActivityForResult(i, 0);
    }

    private void subscribeUserToChannel() {
        ParsePush.subscribeInBackground("NewCampaigns");
    }


    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Payment info:");
        builder.setMessage("It looks like you don't have payment method associated with your profile\n\n" +
                "Would you like to add it now?");
        builder.setIcon(R.mipmap.ic_launcher_sou);
        //builder.setIcon(R.drawable.ic_twitter_bird);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent profileActivity = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(profileActivity);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder;
    }

    public AlertDialog.Builder buildDialogAboutUs(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Welcome to SumOfUs:");
        builder.setMessage(Html.fromHtml(getString(R.string.aboutus)));
        builder.setIcon(R.mipmap.ic_launcher_sou);
        //builder.setIcon(R.drawable.ic_twitter_bird);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        return builder;
    }

}
