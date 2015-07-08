package com.issacchern.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistFragment extends Fragment {

    private CustomAdapter customAdapter;
    private ArrayList<IndividualItem>  individualItems = new ArrayList<IndividualItem>();
    private Timer timer = new Timer();
    private String searchKeyword = "";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private List<Artist> artists;


    public ArtistFragment() {
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

                if(isNetworkAvailable()){
                    String selectedItem = individualItems.get(position).id;
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, selectedItem);
                    startActivity(intent);

                }else{
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet),
                            Toast.LENGTH_SHORT).show();

                }



            }
        });

        final SearchView searchText = (SearchView) rootView.findViewById(R.id.searchText);

        searchText.setIconifiedByDefault(false);
        searchText.setQueryHint(getResources().getString(R.string.artist_search_hint));
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchKeyword = searchText.getQuery().toString();
                if (isNetworkAvailable()) {
                    SearchSpotifyTask task = new SearchSpotifyTask();
                    task.execute(searchText.getQuery().toString());
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet),
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        /*

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
                                    SearchSpotifyTask task = new SearchSpotifyTask();
                                    task.execute();
                                }
                            }
                        });

                    }

                }, 1000);
            }
        });

        */

        return rootView;
    }


    public class SearchSpotifyTask extends AsyncTask<String, Void, List<Artist>>
    {

        @Override
        protected List<Artist> doInBackground(String ... strings) {

            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();

            try {
                ArtistsPager searchResults = service.searchArtists(strings[0]);
                Pager<Artist> results = searchResults.artists;
                artists = results.items;
            } catch(RetrofitError ex){
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_error),
                        Toast.LENGTH_SHORT).show();
            }


            if(artists.size() == 0){
                return null;
            }

            return artists;

        }

        @Override
        protected void onPostExecute(List<Artist> artistList) {

            if(artistList != null){
                customAdapter.clear();
                for(Artist art : artistList) {
                    if(art.images != null && !art.images.isEmpty()){
                        customAdapter.add(new IndividualItem(art.name, "Followers: " +
                                art.followers.total,art.images.get(0).url, art.id));
                    }
                    else{
                        customAdapter.add(new IndividualItem(art.name,
                                "Followers: " + art.followers.total,
                                "http://www.vistacollege.edu/images/graphics/page-warning.png",
                                art.id));
                    }
                }
            }

            else{
                customAdapter.clear();
                Toast.makeText(getActivity(), "No artist found!", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
