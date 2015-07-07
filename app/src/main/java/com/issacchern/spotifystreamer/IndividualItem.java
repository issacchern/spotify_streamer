package com.issacchern.spotifystreamer;

/**
 * Created by Issac on 6/28/2015.
 */
public class IndividualItem {
    String name;
    String description;
    String imageURL;
    String id;

    public IndividualItem(String name, String description, String imageURL, String id){
        this.name = name;
        this.imageURL = imageURL;
        this.description = description;
        this.id = id;
    }

    public IndividualItem(String name, String description, String imageURL){
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
    }
}
