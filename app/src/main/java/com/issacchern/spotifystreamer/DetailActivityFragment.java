package com.issacchern.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;



public class DetailActivityFragment extends Fragment {

    //private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private static final String CURRENT_TRACK_RESULTS_KEY = "Current tracks" ;
    private String mString;
    private List<Track> tracks;
    private boolean check_internet;

    private CustomAdapter customAdapter;
    private ArrayList<IndividualItem> individualItems = new ArrayList<>();

    public DetailActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(CURRENT_TRACK_RESULTS_KEY, individualItems);

        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if(savedInstanceState == null){

            customAdapter = new CustomAdapter(getActivity(), individualItems);
            ListView listView = (ListView) rootView.findViewById(R.id.listView_top_ten_tracks);
            listView.setAdapter(customAdapter);

            Intent intent = getActivity().getIntent();

            if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
                mString = intent.getStringExtra(Intent.EXTRA_TEXT);

                TopTrackSpotifyTask topTrack = new TopTrackSpotifyTask();
                topTrack.execute();

            }

        }
        else{
            individualItems = savedInstanceState.getParcelableArrayList(CURRENT_TRACK_RESULTS_KEY);
            customAdapter = new CustomAdapter(getActivity(), individualItems);
            ListView listView = (ListView) rootView.findViewById(R.id.listView_top_ten_tracks);
            listView.setAdapter(customAdapter);


        }

        return rootView;




    }

    public class TopTrackSpotifyTask extends AsyncTask<Void, Void, List<Track>>{


        @Override
        protected void onPostExecute(List<Track> mTrack) {

            if(!check_internet){
                Toast.makeText(getActivity(),  getResources().getString(R.string.connection_error),
                        Toast.LENGTH_SHORT).show();
                return;
            }


            if(mTrack != null){
                customAdapter.clear();
                for(Track track : mTrack) {
                    customAdapter.add(new IndividualItem(track.name,track.album.name,
                            track.album.images.get(0).url));
                }
            }
            else{
                Toast.makeText(getActivity(),  getResources().getString(R.string.no_track),
                        Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected List<Track> doInBackground(Void... params) {


            try{
                SpotifyApi spotifyApi = new SpotifyApi();
                SpotifyService spotifyService = spotifyApi.getService();
                Map<String, Object> options = new HashMap<>();
                options.put(SpotifyService.COUNTRY, Locale.getDefault().getCountry());
                Tracks results = spotifyService.getArtistTopTrack(mString, options);
                tracks = results.tracks;

                check_internet = true;

            } catch(RetrofitError ex){
                check_internet =false;

            }

            return tracks;







        }
    }
}
