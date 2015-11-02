package com.support.android.designlibdemo.models;


import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("CampaignParse")
public class CampaignParse extends ParseObject {

    public CampaignParse () {
        // Default constructor
    }

    private final String defaultUrl = "http://sumofus.org";

    private ArrayList<String> getMySupported() {
        ArrayList<String> list = new ArrayList<String>();
        JSONArray jsonArray = (JSONArray)getSupportedCampaigns();
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

    public Boolean getIsSupported() {
        int i = (getMySupported().size())-1;
        String oId = getObjectId();
        String sup;

        while (i >= 0) {
            sup = getMySupported().get(i);
            if (sup.equalsIgnoreCase(oId)) {
                Log.d("DEBUG:  SUPPORTED: " , getMySupported().get(i) + " EXPECTING: " + oId);
                return true;
            }
            else
            {
                i--;
            }
        }
        return false;
    }

    private JSONArray getSupportedCampaigns() {
        JSONArray ar = ParseUser.getCurrentUser().getJSONArray("myCampaigns");
        return ar;
    }

    public String getTitle() {
        String text = getString("title");
        return str(text);
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getOverview() {
        String text = getString("overview");
        return str(text);
    }

    public void setOverview(String overview) {
        put("overview", overview);
    }

    public String getDescription() {
        String text = getString("description");
        return str(text);
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getAuthor() {
        String author = getString("author");
        return author;
    }

    public void setAuthor() {
        put("author", ParseUser.getCurrentUser());
    }


    public String getSignMessage() {
        String text = getString("message");
        return str(text);
    }

    public void setSignMessage(String message) {
        put("message", message);
    }


    public void setCategory(String category) {
        put("category", category);
    }

    public String getCategory() {
        String text = getString("category");
        return str(text);
    }


    public int getGoal() {
        int goal = getInt("goal");
        return goal;
    }

    public void setGoal(int goal) {
        put("goal", goal);
    }



    public int getCurrentSignatureCount() {
        int currentSignatureCount = getInt("count");
        return currentSignatureCount;
    }

    public void addCurrentSignatureCount(int count) {
        put("count", count);
    }


    public String getCampaignUrl() {
        String text = getString("campaignUrl");
        return strUrl(text);
    }

    public void setCampaignUrl(String campaignUrl) {
        put("campaignUrl", campaignUrl);
    }

    public JSONArray getAllImageUrls() {
        JSONArray text = getJSONArray("imageUrl");
        return text;
    }

    public void setImageUrl(String imageUrl) {
        addUnique("imageUrl", imageUrl);
    }

    public void setOneImage(ParseFile image) {
        addUnique("image", image);
    }


    public ParseFile getmainImageMain() {
        ParseFile imageMain = getParseFile("imageMain");
        return imageMain;
    }

    public void setImageMain(ParseFile imageMainFile) {
        put("imageMain", imageMainFile);
    }




    public List<Campaign> getMedia() {
        return getList("media");
    }

    public static void findInBackground(int order,
                                        final GetCallback<Supporter> callback) {
        ParseQuery<Supporter> roomQuery = ParseQuery.getQuery(Supporter.class);
        roomQuery.whereEqualTo("order", order);
        roomQuery.getFirstInBackground(new GetCallback<Supporter>() {
            @Override
            public void done(Supporter user, ParseException e) {
                if (e == null) {
                    Supporter status = new Supporter();
                    callback.done(status, null);
                } else {
                    callback.done(null, e);
                }
            }
        });
    }

    // Method to check if string is empty, return '';
    private String str(String text) {
        if (text == null) {
            text = "";
        }
        return text;
    }

    // Method to check if string is empty, return default URL;
    private String strUrl(String text) {
        if (text == null) {
            text = defaultUrl;
        }
        return text;
    }

}
