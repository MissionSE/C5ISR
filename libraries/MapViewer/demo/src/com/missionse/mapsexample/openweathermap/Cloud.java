package com.missionse.mapsexample.openweathermap;

import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import android.os.Parcel;


public class Cloud implements Parcelable{

    private static final String FIELD_ALL = "all";


    @SerializedName(FIELD_ALL)
    private int mAll;


    public Cloud(){

    }

    public void setAll(int all) {
        mAll = all;
    }

    public int getAll() {
        return mAll;
    }

    public Cloud(Parcel in) {
        mAll = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Cloud> CREATOR = new Parcelable.Creator<Cloud>() {
        public Cloud createFromParcel(Parcel in) {
            return new Cloud(in);
        }

        public Cloud[] newArray(int size) {
        return new Cloud[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mAll);
    }


    @Override
    public String toString() {
        return "Cloud{" +
                "mAll=" + mAll +
                '}';
    }
}