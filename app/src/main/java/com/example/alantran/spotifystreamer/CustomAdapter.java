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
 * Created by jacob on 7/3/15.
 */

public class CustomAdapter extends ArrayAdapter<ArtistModel> {
    private static final String LOG_TAG = CustomAdapter.class.getSimpleName();

    public CustomAdapter(Context context, List<ArtistModel> artist) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, artist);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        ArtistModel artist = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_artist, parent, false);
        }

        //ImageView iconView = (ImageView) convertView.findViewById(R.id.list_item_icon);
        //iconView.setImageResource(androidFlavor.image);
        if (artist.image != null) {
            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_item_artist_imageview);
            Picasso.with(getContext())
                    .load(artist.image)
                    .resize(200, 200)
                    .into(imageView);
        }
        TextView artistNameView = (TextView) convertView.findViewById(R.id.list_item_artist_textview);
        artistNameView.setText(artist.name);

        return convertView;
    }
}