package com.support.android.designlibdemo.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;


import android.view.View;
import android.widget.ListView;

import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.support.android.designlibdemo.adapters.VideoListAdapter;
import com.support.android.designlibdemo.dialogs.FragmentDialogOptionsPicker;
import com.support.android.designlibdemo.utils.ConfigYouT;
import com.support.android.designlibdemo.utils.YouTubeContent;

/**
 * Created by dbykovskyy on 11/3/15.
 */
public class NewVideoFragment extends ListFragment {

    ListView lvVideo;

    public NewVideoFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setListAdapter(new VideoListAdapter(getActivity()));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvVideo = getListView();
        lvVideo.setDivider(null);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        final Context context = getActivity();
        final String DEVELOPER_KEY = ConfigYouT.YOUTUBE_API_KEY;
        final YouTubeContent.YouTubeVideo video = YouTubeContent.ITEMS.get(position);


        final int[] selection = new int[1];

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        final ChoiceVideoDialog choiceVideoDiag = ChoiceVideoDialog.newInstance("Play with:");

        choiceVideoDiag.setOnChoiceClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection[0] = which;
            }
        });

        choiceVideoDiag.setPositiveListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(FragmentDialogOptionsPicker.videoPlayerPicker[selection[0]].contains("YouTube")){

                    if (YouTubeIntents.canResolvePlayVideoIntent(context)) {
                        //Opens the video in the YouTube app
                        startActivity(YouTubeIntents.createPlayVideoIntent(context, video.id));
                    }


                }else {
                    if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(context)) {
                        //Opens in the StandAlonePlayer but in "Light box" mode
                        startActivity(YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                                DEVELOPER_KEY, video.id, 0, true, true));
                    }

                }

            }
        });

        choiceVideoDiag.setCancelClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        choiceVideoDiag.show(fragmentManager, "tag");
    }

}
