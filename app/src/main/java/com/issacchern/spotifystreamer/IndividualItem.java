package com.issacchern.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;


public class IndividualItem implements Parcelable {
    String name;
    String description;
    String imageURL;
    String id;

    private IndividualItem(Parcel input){
        name = input.readString();
        description = input.readString();
        imageURL = input.readString();
        id = input.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imageURL);
        dest.writeString(id);

    }

    public static final Parcelable.Creator<IndividualItem> CREATOR =
            new Parcelable.Creator<IndividualItem>(){
                public IndividualItem createFromParcel(Parcel in){
                    return new IndividualItem(in);
                }

                public IndividualItem[] newArray(int size){
                    return new IndividualItem[size];
                }
            };
}
