package com.example.alantran.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by alantran on 7/13/15.
 */
public class TopTrackAdapter extends ArrayAdapter<TrackModel> {
    private static final String LOG_TAG = CustomAdapter.class.getSimpleName();

    public TopTrackAdapter(Context context, List<TrackModel> tracks) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, tracks);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        TrackModel track = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail, parent, false);
        }

        if (track.albumImage != null) {
            ImageView imageView = (ImageView) convertView.findViewById(R.id.fragment_detail_image);
            Picasso.with(getContext())
                    .load(track.albumImage)
                    .resize(200, 200)
                    .into(imageView);
        }
        TextView songName = (TextView) convertView.findViewById(R.id.fragment_detail_songName_textView);
        songName.setText(track.name);

        TextView albumName = (TextView) convertView.findViewById(R.id.fragment_detail_album_textView);
        albumName.setText(track.albumName);

        return convertView;
    }
}
