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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.activities.CheeseDetailActivity;
import com.support.android.designlibdemo.models.Campaign;
import com.support.android.designlibdemo.utils.DeviceDimensionsHelper;

import java.util.ArrayList;
import java.util.List;

public class CampaignsFragment extends Fragment {

    String imageUrl = "http://sumofus.org/wp-content/uploads/2015/10/38b73ede-6a13-433c-9c76-a567ccfea8b1.jpg";
    String oneMOreImage = "http://www.drayan.com/images/painting/small_sunrise.jpg";
    String shortDescriptoin = "The third-largest palm oil corporation in the world is exploiting refugees and clearing rainforests";
    String longDescriptoin = "Standard Chartered, a massive international bank, is about to bankroll a Malaysian palm oil producer responsible for horrific slave-labour conditions and widespread environmental destruction.";

    private List<Campaign> campaigns;

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
        recyclerView.setAdapter(new CampaignRecyclerViewAdapter(getActivity(), populateCampaigns()));
    }


    //This is a helper method when backend is not working
    public List<Campaign> populateCampaigns() {
        campaigns = new ArrayList<>();
        Campaign camp;
        for (int i = 0; i <= 10; i++) {
            if(i==4){
                camp = new Campaign(oneMOreImage, shortDescriptoin, longDescriptoin);
            }else {
                camp = new Campaign(imageUrl, shortDescriptoin, longDescriptoin);
            }

            campaigns.add(camp);
        }
        return campaigns;
    }





// move this to a separate class
    public static class CampaignRecyclerViewAdapter
            extends RecyclerView.Adapter<CampaignRecyclerViewAdapter.ViewHolder> {

        private List<Campaign> mCampaigns;

        public CampaignRecyclerViewAdapter(List<Campaign> campaigns) {
            mCampaigns = campaigns;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public  TextView tvShortCampaignDescription;
            public  ImageView ivCampaign;


            public ViewHolder(View view) {
                super(view);
                //tvShortCampaignDescription = (TextView)view.findViewById(R.id.tvShortDescription);
                tvShortCampaignDescription = (TextView)view.findViewById(R.id.tvShortDescription);
                ivCampaign = (ImageView)view.findViewById(R.id.ivCampaign);
            }

        }

        public Campaign getValueAt(int position) {
            return mCampaigns.get(position);
        }

        public CampaignRecyclerViewAdapter(Context context, List<Campaign> items) {
            mCampaigns = items;
        }


    @Override
    public CampaignRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }


        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Campaign camp =  mCampaigns.get(position);

            holder.tvShortCampaignDescription.setText(camp.getShortDescription());

            Picasso.with(holder.ivCampaign.getContext()).
                    load("http://pngimg.com/upload/rose_PNG652.png").resize(200, 100).
                    into(holder.ivCampaign);

            Picasso.with(holder.ivCampaign.getContext()).load(camp.getImageUrl()).
                    resize(DeviceDimensionsHelper.getDisplayWidth(holder.ivCampaign.getContext()), 0).into(holder.ivCampaign);


            holder.ivCampaign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(holder.ivCampaign.getContext(), CheeseDetailActivity.class);
                   // i.putExtra("camp", camp);
                    holder.ivCampaign.getContext().startActivity(i);

                }
            });


        }

        @Override
        public int getItemCount() {
            return mCampaigns.size();
        }
    }
}
