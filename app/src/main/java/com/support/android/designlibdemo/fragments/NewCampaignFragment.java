package com.support.android.designlibdemo.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.activities.NewCampaignActivity;
import com.support.android.designlibdemo.models.CampaignParse;


public class NewCampaignFragment extends Fragment {
    private ImageButton photoButton;
    private Button saveButton;
    private Button cancelButton;
    private TextView campaignTitle;
    private TextView campaignDescription;
    private TextView campaignOverview;
    private TextView campaignMessage;
    private TextView campaignGoal;
    private TextView campaignUrl;
  //  private TextView campaignImage;
    private Spinner campaignCategory;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v;
        v = inflater.inflate(R.layout.fragment_new_campaign, container, false);

        campaignTitle = ((EditText) v.findViewById(R.id.campaign_title));
        campaignOverview = ((EditText) v.findViewById(R.id.campaign_overview));
        campaignDescription = ((EditText) v.findViewById(R.id.campaign_description));
        campaignGoal = ((EditText) v.findViewById(R.id.campaign_goal));
        campaignMessage = ((EditText) v.findViewById(R.id.campaign_sign_message));
        campaignUrl = ((EditText) v.findViewById(R.id.campaign_url));
      //  campaignImage = ((EditText) v.findViewById(R.id.image_url));

        // The campaignCategory spinner lets people assign a general category for their campaign

        campaignCategory = ((Spinner) v.findViewById(R.id.categories_spinner));
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.category_array,
                        android.R.layout.simple_spinner_dropdown_item);
        campaignCategory.setAdapter(spinnerAdapter);

        photoButton = ((ImageButton) v.findViewById(R.id.photo_button));
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(campaignTitle.getWindowToken(), 0);
                startCamera();

            }
        });

        saveButton = ((Button) v.findViewById(R.id.save_button));
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CampaignParse campaign = ((NewCampaignActivity) getActivity()).getCurrentCampaign();

                // Add data to the campaign object:
                campaign.setTitle(campaignTitle.getText().toString());
                campaign.setOverview(campaignOverview.getText().toString());
                campaign.setDescription(campaignDescription.getText().toString());
                campaign.setSignMessage(campaignMessage.getText().toString());
                campaign.setGoal(Integer.parseInt(campaignGoal.getText().toString()));
                campaign.setCampaignUrl(campaignUrl.getText().toString());
             //   campaign.setImageUrl(campaignImage.getText().toString());
                campaign.setCategory(campaignCategory.getSelectedItem().toString());

                // Associate the campaign with the current user
                campaign.setAuthor();

                // When the user clicks "Save," upload the campaign to Parse
                // If the user added a photo, that data will be
                // added in the CameraFragment

                // Save the campaign and return
                campaign.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            sendPushNotification();

                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();
                        } else {
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Error saving: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        cancelButton = ((Button) v.findViewById(R.id.cancel_button));
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });

       // campaignImagePreview = (ParseImageView) v.findViewById(R.id.campaign_preview_image);
       // campaignImagePreview.setVisibility(View.INVISIBLE);

        return v;
    }

    public void startCamera() {
        Fragment cameraFragment = new CameraFragment();
        FragmentTransaction transaction = getActivity().getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragmentContainer, cameraFragment);
        transaction.addToBackStack("NewCampaignFragment");
        transaction.commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        ParseFile photoFile = ((NewCampaignActivity) getActivity())
                .getCurrentCampaign().getmainImageMain();
        if (photoFile != null) {
            // Allow user to preview photo before saving... Currently Preview Module stops compiling after a few successful builds.
            
           // campaignImagePreview.setParseFile(photoFile);
          /*  campaignImagePreview.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    campaignImagePreview.setVisibility(View.VISIBLE);
                }
            });
            */
        }
    }

    private void sendPushNotification() {
        ParsePush push = new ParsePush();
        push.setChannel("NewCampaigns");
        push.setMessage("New campaign available!");
        push.sendInBackground();
    }

}
