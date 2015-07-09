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

        ViewHolder holder = new ViewHolder();

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        holder.iconView = (ImageView) view.findViewById(R.id.list_item_image_view);

        try{
            Picasso.with(getContext()).load(individualItem.imageURL).resize(100,100).into(holder.iconView);

        }catch(IllegalArgumentException e){
            holder.iconView.setImageResource(R.drawable.image1);
        }

        holder.nameView = (TextView) view.findViewById(R.id.list_item_text_view);
        holder.nameView.setText(individualItem.name);
        holder.descriptionView = (TextView) view.findViewById(R.id.list_item_description_text_view);
        holder.descriptionView.setText(individualItem.description);
        view.setTag(holder);

        return view;


    }

    static class ViewHolder{
        ImageView iconView;
        TextView nameView;
        TextView descriptionView;
    }
}
