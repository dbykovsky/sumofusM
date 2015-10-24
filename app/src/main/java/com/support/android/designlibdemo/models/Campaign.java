package com.support.android.designlibdemo.models;

import com.parse.ParseFile;

import java.io.Serializable;

/**
 * Created by dbykovskyy on 10/14/15.
 */
public class Campaign implements Serializable{

    private String imageUrl;
    private String shortDescription;
    private String longDescription;
    private ParseFile imageMain;
    private String imageMainUrl;
    private String objectId;



    private String message;


    private String title;

    public Campaign(String sTitle, String sMessage, String sShortDescription, String sLongDescription, String sObjId, ParseFile fMainUrl){

        title = sTitle;
        message = sMessage;
        shortDescription=sShortDescription;
        longDescription=sLongDescription;
        imageUrl = getImageMainUrl(fMainUrl);
        objectId = sObjId;

    }

    public Campaign() {
        setupDefaultValues();
    }


    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageMainUrl(ParseFile pf) {
        if(pf!=null){
            return pf.getUrl();
        }
        return null;
    }

    public void setImageMainUrl(String imageMainUrl) {
        this.imageMainUrl = imageMainUrl;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public ParseFile getImageMain() {
        return imageMain;
    }

    public void setImageMain(ParseFile imageMain) {
        this.imageMain = imageMain;
    }

    private void setupDefaultValues() {
        // Add hardcoded campaigns
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
