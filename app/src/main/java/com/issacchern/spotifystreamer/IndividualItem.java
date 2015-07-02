package com.issacchern.spotifystreamer;

/**
 * Created by Issac on 6/28/2015.
 */
public class IndividualItem {
    String name;
    String description;
    int image;

    public IndividualItem(String name, int image){
        this.name = name;
        this.image = image;
    }

    public IndividualItem(String name, String description, int image){
        this.name = name;
        this.description = description;
        this.image = image;
    }
}
