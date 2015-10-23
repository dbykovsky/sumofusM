package com.support.android.designlibdemo.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/*
* This is the 'current' app user, AKA the "supporter" of a campaign.
*
* Sum of Us defines a Super-user, as the one who can add pictures to campaigns.
* We'll allow an end user to create a campaign and send upload for approval.
*  -- This pending campaign can be supported if found by a direct link.
*  -- The idea is allow a SOU administrator to know whether this campaign is already popular or not
*
* */

@ParseClassName("Supporter")
public class Supporter extends ParseObject {

    public Supporter() {
        // default constructor
    }

    public ParseUser getScreenName() {
        return getParseUser("screenname");
    }

    public void setScreenName(ParseUser screenname) {
        put("screenname", screenname);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public boolean isAdmin() {
        return getBoolean("isAdmin");
    }

    public void setAdmin(Boolean isAdmin) {
        put("isAdmin", isAdmin);
    }


    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile file) {
        put("photo", file);
    }
}
