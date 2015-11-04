package com.support.android.designlibdemo.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.utils.DeviceDimensionsHelper;

import java.util.ArrayList;


/**
 * Created by dbykovskyy on 10/27/15.
 */
public class PhotogalleryAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> imageUrls;

    public PhotogalleryAdapter(Context context, ArrayList<String> iUrls) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageUrls = iUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        Picasso.with(mContext).load(imageUrls.get(position)).placeholder(R.drawable.image_holder_new).
                resize(DeviceDimensionsHelper.getDisplayWidth(mContext), 0).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}


