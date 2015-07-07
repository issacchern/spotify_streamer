package com.issacchern.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private String mString;

    private CustomAdapter customAdapter;
    private ArrayList<IndividualItem> individualItems = new ArrayList<IndividualItem>();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        customAdapter = new CustomAdapter(getActivity(), individualItems);
        ListView listView = (ListView) rootView.findViewById(R.id.listView_top_ten_tracks);
        listView.setAdapter(customAdapter);

        Intent intent = getActivity().getIntent();

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            mString = intent.getStringExtra(Intent.EXTRA_TEXT);

            TopTrackSpotifyTask topTrack = new TopTrackSpotifyTask();
            topTrack.execute();

        }


        return rootView;




    }

    public class TopTrackSpotifyTask extends AsyncTask<Void, Void, List<Track>>{


        @Override
        protected void onPostExecute(List<Track> mTrack) {
            if(mTrack != null){
                customAdapter.clear();
                for(Track track : mTrack) {
                    customAdapter.add(new IndividualItem(track.name,track.album.name,
                            track.album.images.get(0).url));
                }
            }

        }

        @Override
        protected List<Track> doInBackground(Void... params) {

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.COUNTRY, Locale.getDefault().getCountry());
            Tracks results = spotifyService.getArtistTopTrack(mString, options);
            List<Track> tracks = results.tracks;

            return tracks;


        }
    }
}
