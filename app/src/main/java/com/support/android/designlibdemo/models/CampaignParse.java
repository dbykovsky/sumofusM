package com.support.android.designlibdemo.models;


import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.List;

@ParseClassName("CampaignParse")
public class CampaignParse extends ParseObject {

    public CampaignParse () {
        // Default constructor
    }

    private final String defaultUrl = "http://sumofus.org";

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
        String text = getString("url");
        return strUrl(text);
    }

    public void setCampaignUrl(String url) {
        put("url", url);
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
