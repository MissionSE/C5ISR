package com.missionse.mapsexample.openweathermap;

import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import android.os.Parcel;


public class Wind implements Parcelable{

    private static final String FIELD_DEG = "deg";
    private static final String FIELD_SPEED = "speed";


    @SerializedName(FIELD_DEG)
    private double mDeg;
    @SerializedName(FIELD_SPEED)
    private double mSpeed;


    public Wind(){

    }

    public void setDeg(double deg) {
        mDeg = deg;
    }

    public double getDeg() {
        return mDeg;
    }

    public void setSpeed(double speed) {
        mSpeed = speed;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public Wind(Parcel in) {
        mDeg = in.readDouble();
        mSpeed = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Wind> CREATOR = new Parcelable.Creator<Wind>() {
        public Wind createFromParcel(Parcel in) {
            return new Wind(in);
        }

        public Wind[] newArray(int size) {
        return new Wind[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mDeg);
        dest.writeDouble(mSpeed);
    }


    @Override
    public String toString() {
        return "Wind{" +
                "mDeg=" + mDeg +
                ", mSpeed=" + mSpeed +
                '}';
    }
}