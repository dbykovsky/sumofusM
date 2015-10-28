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
import com.parse.ParseUser;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.adapters.CampaignRecyclerViewAdapter;
import com.support.android.designlibdemo.models.Campaign;
import com.support.android.designlibdemo.models.CampaignParse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dbykovskyy on 10/27/15.
 */
public class CampaignsSupportedFragment extends Fragment{

    private List<Campaign> campaigns;

    private CampaignRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateCampaignsISignedParse();
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


    public List<Campaign> populateCampaignsISignedParse() {
        campaigns = new ArrayList<>();
        //get all campaigns by a certain user
        ParseUser currentUser = ParseUser.getCurrentUser();
        ArrayList<String> campaignIds = (ArrayList<String>) currentUser.get("myCampaigns");
        //don't even continue
        if(campaignIds==null){
            return campaigns;
        }

        //traverse through id in Campaign class
        for(String campId:campaignIds){
            ParseQuery<CampaignParse> query = ParseQuery.getQuery(CampaignParse.class);
            query.whereEqualTo("objectId",campId);
            query.orderByDescending("updatedAt");
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


        }
        return campaigns;

    }

}
