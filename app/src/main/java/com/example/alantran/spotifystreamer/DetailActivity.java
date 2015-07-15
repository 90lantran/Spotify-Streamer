package com.example.alantran.spotifystreamer;

import android.content.Intent;
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


public class DetailActivity extends ActionBarActivity {


    static TopTrackAdapter mTopTrackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

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
    public class DetailFragment extends Fragment {

        public final String LOG_TAG = DetailFragment.class.getSimpleName();

        public DetailFragment() {

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

            mTopTrackAdapter = new TopTrackAdapter(getActivity(), new ArrayList<Track>());

            listView.setAdapter(mTopTrackAdapter);

            FetchTrackTask task = new FetchTrackTask();
            task.execute(artistId,country);

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

            List<Track> tracks = service.getArtistTopTrack(artistId, options).tracks;
            for (Track track : tracks) {
                Log.i(LOG_TAG, track.name);
            }

            return tracks;
        }

        @Override
        protected void onPostExecute(List<Track> tracks) {
            super.onPostExecute(tracks);

            if (tracks != null) {
                mTopTrackAdapter.clear();
            }

            List<Track> updatedTrack = new ArrayList<Track>();
            for (Track track : tracks) {
                updatedTrack.add(track);
            }

            // Corner case
            if(updatedTrack.size() == 0){
                Track track = new Track();
                track.name = "There is no result for your chosen artist";
                updatedTrack.add(track);
            }

            mTopTrackAdapter.addAll(updatedTrack);

        }
    }
}
