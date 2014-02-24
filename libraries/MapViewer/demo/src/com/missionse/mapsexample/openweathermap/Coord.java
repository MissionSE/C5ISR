package com.missionse.mapsexample.openweathermap;

import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import android.os.Parcel;


public class Coord implements Parcelable{

    private static final String FIELD_LON = "lon";
    private static final String FIELD_LAT = "lat";


    @SerializedName(FIELD_LON)
    private double mLon;
    @SerializedName(FIELD_LAT)
    private double mLat;


    public Coord(){

    }

    public void setLon(int lon) {
        mLon = lon;
    }

    public double getLon() {
        return mLon;
    }

    public void setLat(int lat) {
        mLat = lat;
    }

    public double getLat() {
        return mLat;
    }

    public Coord(Parcel in) {
        mLon = in.readDouble();
        mLat = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Coord> CREATOR = new Parcelable.Creator<Coord>() {
        public Coord createFromParcel(Parcel in) {
            return new Coord(in);
        }

        public Coord[] newArray(int size) {
        return new Coord[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mLon);
        dest.writeDouble(mLat);
    }


    @Override
    public String toString() {
        return "Coord{" +
                "mLon=" + mLon +
                ", mLat=" + mLat +
                '}';
    }
}