package com.issacchern.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Issac on 6/27/2015.
 */
public class CustomAdapter extends ArrayAdapter<IndividualItem> {

    private static final String LOG_TAG = CustomAdapter.class.getSimpleName();


    public CustomAdapter(Activity context, List<IndividualItem> individualItems){
        //second argument is 0 because we don't necessarily use TextView to populate but we
        //use custom adapter to complete the task
        super(context, 0, individualItems);

    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        //get the element from ArrayAdapter at appropriate position
        IndividualItem individualItem = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.list_item_image_view);

        try{
            Picasso.with(getContext()).load(individualItem.imageURL).resize(100,100).into(iconView);

        }catch(IllegalArgumentException e){
            iconView.setImageResource(R.drawable.image1);
        }

        TextView nameView = (TextView) view.findViewById(R.id.list_item_text_view);
        nameView.setText(individualItem.name);
        TextView descriptionView = (TextView) view.findViewById(R.id.list_item_description_text_view);
        descriptionView.setText(individualItem.description);

        return view;


    }
}
