package com.support.android.designlibdemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.adapters.VideoAdapterRecyclerView;
import com.support.android.designlibdemo.models.Campaign;

import java.util.List;

/**
 * Created by dbykovskyy on 10/22/15.
 */
public class VideosFragment extends Fragment {

    String imageUrl = "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSzNYnOiPfmqnJ4GagBOl83GQiWpESJMrGCLbH7mqTqyHGYE-hp9twG8PM";
    String oneMOreImage = "http://www.focusonnature.be/files/images/Small%20mushroom%203.preview.jpg";
    String shortDescriptoin = "The third-largest palm oil corporation in the world is exploiting refugees and clearing rainforests";
    String longDescriptoin = "Standard Chartered, a massive international bank, is about to bankroll a Malaysian palm oil producer responsible for horrific slave-labour conditions and widespread environmental destruction.";

    private List<Campaign> campaigns;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_video_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager lManager = new LinearLayoutManager(recyclerView.getContext());
        lManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lManager);
        recyclerView.setAdapter(new VideoAdapterRecyclerView(getActivity(), getVideoIds()));
    }


    public String [] getVideoIds(){
        return new String[] {"c7Pcfz214qc", "8umhb-h9spY", "C3i2Kes-zRk", "GtGokeer-b8", "EtRy5PbD4JY"};
    }


}
