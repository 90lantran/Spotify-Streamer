package com.example.alantran.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.RetrofitError;


public class DetailActivity extends ActionBarActivity {


    static TopTrackAdapter mTopTrackAdapter;
    static ArrayList<TrackModel> trackList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String artistName = intent.getStringExtra("artistName");

        getSupportActionBar().setSubtitle(artistName);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment()).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        public final String LOG_TAG = DetailFragment.class.getSimpleName();

        public DetailFragment() {

        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (savedInstanceState == null || !savedInstanceState.containsKey("tracks")) {
                trackList = new ArrayList<TrackModel>();
            } else {
                trackList = savedInstanceState.getParcelableArrayList("tracks");
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            if (trackList != null) {
                outState.putParcelableArrayList("tracks", trackList);
            }
            super.onSaveInstanceState(outState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.track_list, container, false);

            Log.i(LOG_TAG, "In Detail Fragment ");
            Intent intent = getActivity().getIntent();
            String artistId = intent.getStringExtra("artistId");
            String country = intent.getStringExtra("country");

            ListView listView = (ListView) rootView.findViewById(R.id.track_list_listView);

            mTopTrackAdapter = new TopTrackAdapter(getActivity(), trackList);

            listView.setAdapter(mTopTrackAdapter);

            FetchTrackTask task = new FetchTrackTask();
            if (savedInstanceState == null && isNetworkAvailable()) {
                task.execute(artistId, country);
            }
            return rootView;
        }
    }



    public static class FetchTrackTask extends AsyncTask<String, Void, List<Track>> {
        private final String LOG_TAG = FetchTrackTask.class.getSimpleName();

        @Override
        protected List<Track> doInBackground(String... params) {

            String artistId = params[0];
            String country = params[1];

            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();

            Map<String, Object> options = new HashMap<String, Object>();
            options.put(SpotifyService.OFFSET, 0);
            options.put(SpotifyService.LIMIT, 10);
            options.put(SpotifyService.COUNTRY, country);
            List<Track> tracks = null;
            try {
                tracks = service.getArtistTopTrack(artistId, options).tracks;
            } catch (RetrofitError error){
                if (error.getResponse().getStatus() == 400)
                    throw new RuntimeException("Bad request");
            }
            finally {
                return tracks;
            }

        }

        @Override
        protected void onPostExecute(List<Track> tracks) {
            super.onPostExecute(tracks);
            List<TrackModel>  updatedTrack = new ArrayList<TrackModel>();
            if (tracks != null) {
                mTopTrackAdapter.clear();
                for (Track track : tracks) {
                    updatedTrack.add(new TrackModel(track.name, track.album.name, track.album.images.get(0).url));
                }
            }

            // Corner case
            if(updatedTrack.size() == 0){
                TrackModel track = new TrackModel();
                track.name = "There is no result for your chosen artist";
                updatedTrack.add(track);
            }

            mTopTrackAdapter.addAll(updatedTrack);
        }
    }
}
