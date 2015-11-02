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
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.adapters.CampaignRecyclerViewAdapter;
import com.support.android.designlibdemo.models.Campaign;
import com.support.android.designlibdemo.models.CampaignParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dbykovskyy on 10/27/15.
 */
public class CampaignsSupportedFragment extends Fragment{

    private List<Campaign> campaigns;

    private CampaignRecyclerViewAdapter adapter;
    private TextView tv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateCampaignsISignedParse();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_campaigns_list, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerview);
        tv = (TextView) view.findViewById(R.id.empty_view);
        //setupRecyclerView(rv);
        setupRecyclerView2(rv);
        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new CampaignRecyclerViewAdapter(getActivity(), campaigns);
        recyclerView.setAdapter(adapter);
    }


    private void checkAdapterIsEmpty () {

        if (adapter.getItemCount() == 0) {
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    protected void setupRecyclerView2(RecyclerView recyclerView) {

        //recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new CampaignRecyclerViewAdapter(getActivity(), campaigns);
        recyclerView.setAdapter(adapter);

        adapter = new CampaignRecyclerViewAdapter(getActivity(), campaigns);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkAdapterIsEmpty();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);
        checkAdapterIsEmpty();
    }




    private ArrayList<String> convertArray(JSONArray campaignIds) {
        ArrayList<String> list = new ArrayList<String>();
        JSONArray jsonArray = (JSONArray)campaignIds;
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i=0;i<len;i++){
                try {
                    list.add(jsonArray.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }



    public List<Campaign> populateCampaignsISignedParse() {
        campaigns = new ArrayList<>();
        //get all campaigns by a certain user
        ParseUser currentUser = ParseUser.getCurrentUser();
        ArrayList<String> campaignIds; // = (ArrayList<String>) currentUser.get("myCampaigns");

        campaignIds = convertArray(currentUser.getJSONArray("myCampaigns"));

        //don't even continue
        if(campaignIds.size() < 1){
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
