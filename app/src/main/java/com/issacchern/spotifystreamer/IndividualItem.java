package com.issacchern.spotifystreamer;

/**
 * Created by Issac on 6/28/2015.
 */
public class IndividualItem {
    String name;
    String description;
    String imageURL;

    public IndividualItem(String name, String imageURL){
        this.name = name;
        this.imageURL = imageURL;
    }

    public IndividualItem(String name, String description, String imageURL){
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
    }
}
