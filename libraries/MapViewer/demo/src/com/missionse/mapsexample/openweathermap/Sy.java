package com.missionse.mapsexample.openweathermap;

import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import android.os.Parcel;


public class Sy implements Parcelable{

    private static final String FIELD_SUNSET = "sunset";
    private static final String FIELD_COUNTRY = "country";
    private static final String FIELD_MESSAGE = "message";
    private static final String FIELD_SUNRISE = "sunrise";


    @SerializedName(FIELD_SUNSET)
    private int mSunset;
    @SerializedName(FIELD_COUNTRY)
    private String mCountry;
    @SerializedName(FIELD_MESSAGE)
    private double mMessage;
    @SerializedName(FIELD_SUNRISE)
    private int mSunrise;


    public Sy(){

    }

    public void setSunset(int sunset) {
        mSunset = sunset;
    }

    public int getSunset() {
        return mSunset;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setMessage(double message) {
        mMessage = message;
    }

    public double getMessage() {
        return mMessage;
    }

    public void setSunrise(int sunrise) {
        mSunrise = sunrise;
    }

    public int getSunrise() {
        return mSunrise;
    }

    public Sy(Parcel in) {
        mSunset = in.readInt();
        mCountry = in.readString();
        mMessage = in.readDouble();
        mSunrise = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Sy> CREATOR = new Parcelable.Creator<Sy>() {
        public Sy createFromParcel(Parcel in) {
            return new Sy(in);
        }

        public Sy[] newArray(int size) {
        return new Sy[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mSunset);
        dest.writeString(mCountry);
        dest.writeDouble(mMessage);
        dest.writeInt(mSunrise);
    }


    @Override
    public String toString() {
        return "Sy{" +
                "mSunset=" + mSunset +
                ", mCountry='" + mCountry + '\'' +
                ", mMessage=" + mMessage +
                ", mSunrise=" + mSunrise +
                '}';
    }
}