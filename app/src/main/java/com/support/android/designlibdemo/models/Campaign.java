package com.support.android.designlibdemo.models;

import java.io.Serializable;

/**
 * Created by dbykovskyy on 10/14/15.
 */
public class Campaign implements Serializable{

    private String imageUrl;
    private String shortDescription;
    private String longDescription;


    public Campaign(String iUrl, String sDescription, String lDescription){
        imageUrl=iUrl;
        shortDescription=sDescription;
        longDescription=lDescription;
       // longDescription=campText;

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


    private String campText2 = "Standard Chartered, a massive international bank, is about to bankroll a Malaysian palm oil producer responsible for horrific slave-labour conditions and widespread environmental destruction.\n" +
            "\n" +
            "The third largest palm oil producer in the world, Felda, needs a loan from Standard Chartered to purchase Eagle High Plantations in Indonesia.\n" +
            "\n" +
            "Some of Felda's workers are desperate refugees who relied on people smugglers to get them into Malaysia. Felda gets its workers through labour contractors, which have been linked to confiscating workers' passports and makes them pay for their own equipment. The abuse is ongoing -- contractors have been known to illegally withhold low wages for months on end.\n" +
            "\n" +
            "And that's before we get to the environmental devastation. Reports have linked Eagle High Plantations to clearcutting orangutan habitat and 6,000 hectares of Papua rainforest. Its cultivation of 17,000 hectares of peat causes emissions equivalent to about 400,000 cars per year.\n" +
            "\n" +
            "If we can stop Standard Chartered's loan, we can derail Felda's plans for conflict palm oil.\n" +
            "\n" +
            "Tell Standard Chartered to walk away from this nightmare of a deal.\n" +
            "\n" +
            "SumOfUs members have been changing the way the world looks at conflict palm oil. We've pushed McDonald's and KFC to adopt global responsible palm oil policies and go deforestation-free. We're also pressuring Doritos' parent company PepsiCo. Because of us it's come up with a new palm oil policy that is stronger in some respects, but we'll be continue to campaign hard for the loopholes to be closed.\n" +
            "\n" +
            "We know change is possible with Standard Chartered. Recently, tens of thousands pressured Standard Chartered to cut finance for the controversial Carmichael coal project that would ruin the Great Barrier Reef.\n" +
            "\n" +
            "We won and we can do it again here. Let's dial up the heat on Standard Chartered to make it fold.\n" +
            "\n" +
            "Standard Chartered is evaluating the merits of this deal -- let's give it an offer it has to refuse.";
}
