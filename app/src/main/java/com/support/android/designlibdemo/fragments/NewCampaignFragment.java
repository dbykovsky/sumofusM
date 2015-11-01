package com.support.android.designlibdemo.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iangclifton.android.floatlabel.FloatLabel;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.activities.NewCampaignActivity;
import com.support.android.designlibdemo.models.CampaignParse;
import com.support.android.designlibdemo.utils.BitmapScaler;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class NewCampaignFragment extends Fragment {
    private static final int PICK_PHOTO_CODE = 445533;
    private ImageButton photoButton;
    private Button saveButton;
    private Button cancelButton;
    private ImageButton uploadButton;
    private FloatLabel campaignTitle;
    private FloatLabel campaignDescription;
    private FloatLabel campaignOverview;
    private FloatLabel campaignMessage;
    private FloatLabel campaignGoal;
    private FloatLabel campaignUrl;
  //  private TextView campaignImage;
    private Spinner campaignCategory;
    private CampaignParse campaign;

    private int charCount = 0;
    private static final int MaxTitleCount = 20;
    private static final int MaxOverviewCount = 200;
    private static final int MaxMessageCount = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        campaign = ((NewCampaignActivity) getActivity()).getCurrentCampaign();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v;
        v = inflater.inflate(R.layout.fragment_new_campaign, container, false);

        final TextView tvTitleHelp = (TextView) v.findViewById(R.id.tvTitleHelp);
        final TextView tvOverviewHelp = (TextView) v.findViewById(R.id.tvOverviewHelp);
        final TextView tvMessageHelp = (TextView) v.findViewById(R.id.tvMessageHelp);
        final TextView tvGoalHelp = (TextView) v.findViewById(R.id.tvGoalHelp);

        campaignTitle = ((FloatLabel) v.findViewById(R.id.campaign_title));
        campaignTitle.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charCount = s.length();
                if (charCount > MaxTitleCount) {
                    tvTitleHelp.setError("Really?!");
                    tvTitleHelp.setTextColor(Color.RED);
                    tvTitleHelp.setText("That's a long title. " + charCount + "/" + MaxTitleCount + " ");
                }
                else if (charCount > (MaxTitleCount /2) ) {
                    tvTitleHelp.setVisibility(View.VISIBLE);
                    tvTitleHelp.setText(charCount + "/" + MaxTitleCount + " ");
                    tvTitleHelp.setError(null);
                    tvTitleHelp.setTextColor(Color.GRAY);
                }
                else {
                    tvTitleHelp.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        campaignOverview = ((FloatLabel) v.findViewById(R.id.campaign_overview));
        campaignOverview.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charCount = s.length();


                if (charCount > MaxOverviewCount) {
                    tvOverviewHelp.setError("Really?!");
                    tvOverviewHelp.setTextColor(Color.RED);
                    tvOverviewHelp.setText("Really?! " + charCount + "/" + MaxOverviewCount + " ");
                }
                else if (charCount > (MaxOverviewCount /2) ) {
                    tvOverviewHelp.setVisibility(View.VISIBLE);
                    tvOverviewHelp.setText(charCount + "/" + MaxOverviewCount + " ");
                }
                else {
                    tvOverviewHelp.setError(null);
                    tvOverviewHelp.setTextColor(Color.GRAY);
                    tvOverviewHelp.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        campaignDescription = ((FloatLabel) v.findViewById(R.id.campaign_description));
        campaignGoal = ((FloatLabel) v.findViewById(R.id.campaign_goal));
        campaignGoal.getEditText().setMaxLines(1);
        campaignGoal.getEditText().setInputType(0x00000002);
        campaignGoal.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charCount = s.length();

                switch (charCount) {
                    case 4:
                        tvGoalHelp.setVisibility(View.VISIBLE);
                        tvGoalHelp.setError(null);
                        tvGoalHelp.setTextColor(Color.GRAY);
                        tvGoalHelp.setText("Numbers only ");
                        break;
                    case 5:
                        tvGoalHelp.setText("Tens of thousands...");
                        break;
                    case 6:
                        tvGoalHelp.setText("Hundreds of thousands ... ");
                        break;
                    case 7:
                        tvGoalHelp.setError("Really?! ");
                        tvGoalHelp.setTextColor(Color.RED);
                        tvGoalHelp.setText("Millions of users?! ");
                        break;
                    case 8:
                        tvGoalHelp.setError("Really?! ");
                        tvGoalHelp.setTextColor(Color.RED);
                        tvGoalHelp.setText("Really?! We don't have MILLIONS of users?! ");
                        break;
                    default:
                        tvGoalHelp.setText("Numbers only ");
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        campaignMessage = ((FloatLabel) v.findViewById(R.id.campaign_sign_message));
        campaignMessage.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charCount = s.length();
                if (charCount > MaxMessageCount) {
                    tvMessageHelp.setError("Really?! ");
                    tvMessageHelp.setTextColor(Color.RED);
                    tvMessageHelp.setText("Are you writting a book? " + charCount + "/" + MaxMessageCount + " ");
                } else if (charCount > (MaxMessageCount / 2)) {
                    tvMessageHelp.setVisibility(View.VISIBLE);
                    tvMessageHelp.setText(charCount + "/" + MaxMessageCount + " ");
                } else {
                    tvMessageHelp.setError(null);
                    tvMessageHelp.setTextColor(Color.GRAY);
                    tvMessageHelp.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        campaignUrl = ((FloatLabel) v.findViewById(R.id.campaign_url));
        campaignUrl.getEditText().setInputType(0x01);
//        campaignCategory = ((Spinner) v.findViewById(R.id.categories_spinner));
//        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
//                .createFromResource(getActivity(), R.array.category_array,
//                        android.R.layout.simple_spinner_dropdown_item);
//        campaignCategory.setAdapter(spinnerAdapter);

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


                // Add data to the campaign object:
                campaign.setTitle(campaignTitle.getEditText().getText().toString());
                campaign.setOverview(campaignOverview.getEditText().getText().toString());
                campaign.setDescription(campaignDescription.getEditText().getText().toString());
                campaign.setSignMessage(campaignMessage.getEditText().getText().toString());
                String a = campaignGoal.getEditText().getText().toString();
                campaign.setGoal(Integer.parseInt(campaignGoal.getEditText().getText().toString()));
                campaign.setCampaignUrl(campaignUrl.getEditText().getText().toString());
               // campaign.setCategory(campaignCategory.getSelectedItem().toString());
                campaign.setCategory("none");

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

        uploadButton = ((ImageButton) v.findViewById(R.id.upload_button));
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(campaignTitle.getWindowToken(), 0);
                startUpload();
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

    public void startUpload() {
        // Take the user to the gallery app to pick a photo
        Intent photoGalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoGalleryIntent, PICK_PHOTO_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_PHOTO_CODE) {
                Uri photoUri;
                Bitmap photoBitmap = null;
                final ParseFile photoFile;

                photoUri = data.getData();
                try {
                    photoBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),photoUri); //( photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap resizedImage = BitmapScaler.scaleToFitWidth(photoBitmap, 300);
              //  ivCampaignImage.getAdjustViewBounds();
              //  ivCampaignImage.setScaleType(ImageView.ScaleType.FIT_XY);
                //********** Update parse with image
                //ivCampaignImage.setImageBitmap(resizedImage);

                // Convert bitmap to a byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile with resized image
                photoFile = new ParseFile("mainImage_by_user_" + ParseUser.getCurrentUser().getUsername() + ".jpg", image);
                photoFile.saveInBackground(new SaveCallback() {

                    public void done(ParseException e) {
                        if (e != null) {
                            Toast.makeText(getActivity(),
                                    "Error saving: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        } else {

                            // Success. Populate this new campaign in the "Campaigns" listing.
                            campaign.setImageMain(photoFile);
                        }
                    }
                });
            }
        }
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
