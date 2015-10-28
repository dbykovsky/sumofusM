package com.support.android.designlibdemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.activities.CampaignDetailActivity;
import com.support.android.designlibdemo.activities.SignPetitionActivity;
import com.support.android.designlibdemo.models.Campaign;
import com.support.android.designlibdemo.utils.DeviceDimensionsHelper;

import java.util.List;

/**
 * Created by dbykovskyy on 10/22/15.
 */
public class CampaignRecyclerViewAdapter extends RecyclerView.Adapter<CampaignRecyclerViewAdapter.ViewHolder> {

    private List<Campaign> mCampaigns;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvShortCampaignDescription;
        public ImageView ivCampaign;
        public Button btTakeAction;

        public ViewHolder(View view) {
            super(view);
            tvShortCampaignDescription = (TextView)view.findViewById(R.id.tvShortDescription);
            ivCampaign = (ImageView)view.findViewById(R.id.ivCampaign);
            btTakeAction = (Button) view.findViewById(R.id.btTakeActionDetail);
        }
    }

    public CampaignRecyclerViewAdapter(Context context, List<Campaign> items) {
        mCampaigns = items;
        mContext = context;
    }


    @Override
    public CampaignRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_item, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Campaign camp =  mCampaigns.get(position);

        holder.tvShortCampaignDescription.setText(camp.getTitle());

        Picasso.with(holder.ivCampaign.getContext()).load(camp.getImageUrl()).placeholder(R.drawable.downloading).
                resize(DeviceDimensionsHelper.getDisplayWidth(holder.ivCampaign.getContext()), 0).into(holder.ivCampaign);

        holder.ivCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.ivCampaign.getContext(), CampaignDetailActivity.class);
                i.putExtra("camp", camp);
                holder.ivCampaign.getContext().startActivity(i);

            }
        });

        holder.btTakeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(holder.btTakeAction.getContext(), SignPetitionActivity.class);
                i.putExtra("camp", camp);
                holder.ivCampaign.getContext().startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mCampaigns.size();
    }
}
