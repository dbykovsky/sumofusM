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

package com.support.android.designlibdemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.adapters.CampaignRecyclerViewAdapter;
import com.support.android.designlibdemo.models.Campaign;
import com.support.android.designlibdemo.models.CampaignParse;

import java.util.ArrayList;
import java.util.List;

public class CampaignsFragment extends Fragment {

    String imageUrl = "http://sumofus.org/wp-content/uploads/2015/10/38b73ede-6a13-433c-9c76-a567ccfea8b1.jpg";
    String oneMOreImage = "http://www.drayan.com/images/painting/small_sunrise.jpg";
    String shortDescriptoin = "The third-largest palm oil corporation in the world is exploiting refugees and clearing rainforests";
    String longDescriptoin = "Standard Chartered, a massive international bank, is about to bankroll a Malaysian palm oil producer responsible for horrific slave-labour conditions and widespread environmental destruction.";

    private List<Campaign> campaigns;

    private CampaignRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateCampaignsParse();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_campaigns_list, container, false);



        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new CampaignRecyclerViewAdapter(getActivity(), campaigns);
        recyclerView.setAdapter(adapter);
    }




/*    //This is a helper method when backend is not working
    public List<Campaign> populateCampaigns() {
        campaigns = new ArrayList<>();
        Campaign camp;
        //ParseFile parseFile = null;
        for (int i = 0; i <= 10; i++) {
            if(i==4){
                camp = new Campaign(oneMOreImage, shortDescriptoin, longDescriptoin, "blah");
            }else {
                camp = new Campaign(imageUrl, shortDescriptoin, longDescriptoin, "bla");
            }

            campaigns.add(camp);
        }
        return campaigns;
    }*/



    public List<Campaign> populateCampaignsParse() {
        campaigns = new ArrayList<>();

        ParseQuery<CampaignParse> query = ParseQuery.getQuery(CampaignParse.class);
        query.findInBackground(new FindCallback<CampaignParse>() {
            @Override
            public void done(List<CampaignParse> list, ParseException e) {
                for (CampaignParse c : list) {
                    Campaign camp = new Campaign(c);
                    campaigns.add(camp);
                    Log.d("DEBUG:", c.getOverview());
                }
                adapter.notifyDataSetChanged();
            }
        });

        return campaigns;
    }
}
