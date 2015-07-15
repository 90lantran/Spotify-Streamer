package com.example.alantran.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    List<String> artistsName = new ArrayList<String>();
    List<String> images = new ArrayList<String>();
    CustomAdapter mArtistAdapter;


    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        SearchView mSearchView = (SearchView) rootView.findViewById(R.id.main_searchview);
        String query = mSearchView.getQuery().toString();
        mSearchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        FetchArtistsTask task = new FetchArtistsTask();
                        task.execute(query);

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );


        // Create some dummy data for the ListView.  Here's a sample weekly forecast
       // mArtistAdapter = new CustomAdapter(getActivity(), new ArrayList<ListOfArtists>());
        mArtistAdapter = new CustomAdapter(getActivity(), new ArrayList<Artist>());

        ListView listView = (ListView) rootView.findViewById(R.id.main_listview);
        listView.setAdapter(mArtistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Artist chosenArtist = mArtistAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("artistName", chosenArtist.name)
                        .putExtra("artistId",chosenArtist.id)
                        .putExtra("country","US");
                startActivity(intent);
            }
        });

        return rootView;
    }


    public class FetchArtistsTask extends AsyncTask<String, Void, List<Artist>>  {

        private final String LOG_TAG = FetchArtistsTask.class.getSimpleName();

        @Override


        protected List<Artist> doInBackground(String... query) {

            if (query.length == 0) {
                return null;
            }

            String searchString = query[0];

            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();

            ArtistsPager results = service.searchArtists(searchString);
            List<Artist> artists = results.artists.items;
            return artists;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            super.onPostExecute(artists);

            if (artists != null) {
                mArtistAdapter.clear();
            }

            Log.i("Artist", String.valueOf(artists.size()));
            List<Artist> updateList = new ArrayList<Artist>();

            for (Artist artist : artists){
                if (artist.images.size() != 0){
                    updateList.add(artist);
                }
            }

            // Corner case
            if(updateList.size() == 0){
                Artist artist = new Artist();
                artist.name = "There is no result for your search";
                updateList.add(artist);
            }

            mArtistAdapter.addAll(updateList);

        }
    }

}