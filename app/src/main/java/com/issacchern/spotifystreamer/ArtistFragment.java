package com.issacchern.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistFragment extends Fragment {

    private CustomAdapter customAdapter;
    private ArrayList<IndividualItem>  individualItems = new ArrayList<IndividualItem>();;
    private Timer timer = new Timer();
    private String searchText = "in";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    public ArtistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        customAdapter = new CustomAdapter(getActivity(), individualItems);
        ListView listView = (ListView) rootView.findViewById(R.id.listView_artist);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = individualItems.get(position).name;
                Toast.makeText(getActivity(), selectedItem, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, selectedItem);
                startActivity(intent);

            }
        });

        final EditText liveSearch = (EditText) rootView.findViewById(R.id.editText_artist);
        liveSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchText = liveSearch.getText().toString();
                                if(searchText.length() > 2){
                                    updateList();
                                }
                                else{
                                }
                            }
                        });

                    }

                }, 1000);
            }
        });

        return rootView;
    }

    private void updateList(){

        if(searchText.length() > 2){
            SearchSpotifyTask task = new SearchSpotifyTask();
            task.execute();

        }


    }

    public class SearchSpotifyTask extends AsyncTask<Void, Void, ArrayList<IndividualItem>>
    {

        @Override
        protected ArrayList<IndividualItem> doInBackground(Void... strings) {

            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();

            ArtistsPager results = service.searchArtists(searchText);
            List<Artist> artists = results.artists.items;


            if(artists.size() == 0){
                Log.i(LOG_TAG, "Artist not found");
                return null;
            }

            else{

                for (int i = 0; i < artists.size(); i++) {
                    Artist artist = artists.get(i);
                    Log.i(LOG_TAG, i + " " + artist.name);

                    individualItems.add(new IndividualItem(artist.name, "", R.drawable.cold2));
                }
            }

            Log.i(LOG_TAG, "individualitems size = " + individualItems.size());
            return individualItems;
        }

        @Override
        protected void onPostExecute(ArrayList<IndividualItem> indis) {


            if(indis != null){

                customAdapter.clear();
                Log.i(LOG_TAG, "indis size = " + indis.size());
                for(IndividualItem ss : indis){
                    Log.i(LOG_TAG, "onPostExecute");
                    customAdapter.add(ss);
                }
                customAdapter.addAll(indis);
                customAdapter.notifyDataSetChanged();
            }

        }
    }






}
