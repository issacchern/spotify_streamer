package com.issacchern.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistFragment extends Fragment {

    ArrayAdapter<String> artistAdapter;

    private CustomAdapter customAdapter;


    IndividualItem [] individualItem ={
            new IndividualItem("Coldplay" , R.drawable.cold1),
            new IndividualItem("Hotplay" , R.drawable.cold2),
            new IndividualItem("Mildplay", R.drawable.cold3),
            new IndividualItem("Buffaloplay" , R.drawable.cold4),
            new IndividualItem("Barbequeplay", R.drawable.cold5)

    };

    IndividualItem [] individualItem2 ={
            new IndividualItem("Coldplay", "Yellow is the hit", R.drawable.cold1),
            new IndividualItem("Hotplay", "Greenday", R.drawable.cold2),
            new IndividualItem("Mildplay", "Darkness Soul", R.drawable.cold3),
            new IndividualItem("Buffaloplay", "Cobra strikes", R.drawable.cold4),
            new IndividualItem("Barbequeplay", "Emos and shits", R.drawable.cold5)

    };


    public ArtistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        customAdapter = new CustomAdapter(getActivity(), Arrays.asList(individualItem));

        ListView listView = (ListView) rootView.findViewById(R.id.listView_artist);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = individualItem[position].name;
                Toast.makeText(getActivity(), selectedItem, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, selectedItem);
                startActivity(intent);

            }
        });

        EditText liveSearch = (EditText) rootView.findViewById(R.id.editText_artist);
        liveSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {


                Toast.makeText(getActivity(), "Item is changed!", Toast.LENGTH_SHORT).show();



                //String text = liveSearch.getText().toString().toLowerCase(Locale.getDefault());
                //customAdapter.filter(text);

            }
        });




        return rootView;
    }

}
