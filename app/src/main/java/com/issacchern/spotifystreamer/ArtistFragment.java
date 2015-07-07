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
import kaaes.spotify.webapi.android.models.Image;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistFragment extends Fragment {

    private CustomAdapter customAdapter;
    private boolean artist_list;
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
                                if (searchText.length() > 2) {
                                    updateList();
                                } else {
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

    public class SearchSpotifyTask extends AsyncTask<Void, Void, String []>
    {

        @Override
        protected String [] doInBackground(Void... strings) {

            artist_list = true;


            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();

            ArtistsPager results = service.searchArtists(searchText);
            List<Artist> artists = results.artists.items;




            if(artists.size() == 0){
                artist_list = false;
                return null;
            }

            else{

                String [] resultStr = new String[artists.size()];

                for (int i = 0; i < artists.size(); i++) {
                    Artist artist = artists.get(i);
                    List<Image> images = artist.images;
                    String id = artist.id;
                    String url = "";
                    if (images != null && !images.isEmpty()) {
                         url = artist.images.get(0).url;
                    }


                    Log.i(LOG_TAG, i + " " + artist.name);
                    resultStr[i] = artist.name + "*" + id + "*" + url;

                    //individualItems.add(new IndividualItem(artist.name, "", R.drawable.cold2));
                }

                return resultStr;
            }
        }

        @Override
        protected void onPostExecute(String []  result) {

            if(artist_list == false){
                customAdapter.clear();
                Toast.makeText(getActivity(), "No artist found!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(result != null){

                customAdapter.clear();

                String name;
                String imageURL;

                for(int i = 0; i < result.length; i++){
                    for(int j = 0 ; j < result[i].length(); j++){

                        if(result[i].charAt(j) == '*'){
                            Log.i(LOG_TAG, "" + j);
                            if(j != result[i].length() -1){
                                name = result[i].substring(0,j);
                                imageURL = result[i].substring(j+1,result[i].length());
                                Log.i(LOG_TAG, "name:" + name + " " + "imageURL:" + imageURL);
                                customAdapter.add(new IndividualItem(name, "", imageURL));
                            }
                            else{
                                name = result[i].substring(0,j);
                                imageURL = "http://www.vistacollege.edu/images/graphics/page-warning.png";
                                Log.i(LOG_TAG, "name:" + name + " " + "imageURL: none");
                                customAdapter.add(new IndividualItem(name, "", imageURL));
                            }
                            break;
                        }

                    }

                }

          //      customAdapter = new CustomAdapter(getActivity(), indis);
          /*      Log.i(LOG_TAG, "indis size2 = " + indis.size());

                for(IndividualItem ss : indis){
                    Log.i(LOG_TAG, "onPostExecute");
                    customAdapter.add(ss);
                }
                customAdapter.addAll(indis);
                customAdapter.notifyDataSetChanged();

                */
            }

        }
    }






}
