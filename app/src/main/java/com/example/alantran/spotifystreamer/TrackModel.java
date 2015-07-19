package com.example.alantran.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alantran on 7/18/15.
 */
public class TrackModel implements Parcelable {
    String name;
    String albumName;
    String albumImage;

    public TrackModel(){

    }

    public TrackModel(String name,String albumName,String albumImage){
        this.name = name;
        this.albumName = albumName;
        this.albumImage = albumImage;
    }

    private TrackModel(Parcel in){
        name = in.readString();
        albumName = in.readString();
        albumImage= in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(albumName);
        dest.writeString(albumImage);
    }

    public final Parcelable.Creator<TrackModel> CREATOR = new Parcelable.Creator<TrackModel>() {

        @Override
        public TrackModel createFromParcel(Parcel source) {
            return new TrackModel(source);
        }

        @Override
        public TrackModel[] newArray(int size) {
            return new TrackModel[size];
        }
    };
}
