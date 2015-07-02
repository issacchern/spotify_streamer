package com.issacchern.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private String mString;


    private CustomAdapter customAdapter;


    IndividualItem [] individualItem ={
            new IndividualItem("The Scientist" , "In the lab",  R.drawable.cold1),
            new IndividualItem("Yellow" , "This is just yellow", R.drawable.cold2),
            new IndividualItem("Clocks", "Time is drifting away", R.drawable.cold3),
            new IndividualItem("Viva la vida" , "Hoorayyy!", R.drawable.cold4),
            new IndividualItem("A Sky Full of Stars", "What a night", R.drawable.cold5)

    };


  




    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            mString = intent.getStringExtra(Intent.EXTRA_TEXT);

            ((TextView) rootView.findViewById(R.id.detail_text)).setText(mString);

     //       customAdapter = new CustomAdapter(getActivity(), Arrays.asList(individualItem));

     //       ListView listView = (ListView) rootView.findViewById(R.id.listView_top_ten_tracks);
      //      listView.setAdapter(customAdapter);

            //create another list of item

        }


        return rootView;




    }
}