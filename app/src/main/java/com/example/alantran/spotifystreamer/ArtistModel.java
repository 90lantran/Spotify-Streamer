package com.example.alantran.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alantran on 7/18/15.
 */
public class ArtistModel implements Parcelable {

    String name;
    String id;
    String image;

    public ArtistModel(){

    }

    public ArtistModel(String name, String id, String image){
        this.name = name;
        this.id = id;
        this.image = image;
    }

    private ArtistModel(Parcel in){
        name = in.readString();
        id = in.readString();
        image = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(image);
    }

    public final Parcelable.Creator<ArtistModel> CREATOR = new Parcelable.Creator<ArtistModel>(){

        @Override
        public ArtistModel createFromParcel(Parcel source) {
            return new ArtistModel(source);
        }

        @Override
        public ArtistModel[] newArray(int size) {
            return new ArtistModel[size];
        }
    };
}
