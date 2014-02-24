package com.missionse.mapsexample.openweathermap;

import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import android.os.Parcel;


public class Main implements Parcelable{

    private static final String FIELD_TEMP = "temp";
    private static final String FIELD_TEMP_MAX = "temp_max";
    private static final String FIELD_PRESSURE = "pressure";
    private static final String FIELD_HUMIDITY = "humidity";
    private static final String FIELD_SEA_LEVEL = "sea_level";
    private static final String FIELD_TEMP_MIN = "temp_min";
    private static final String FIELD_GRND_LEVEL = "grnd_level";


    @SerializedName(FIELD_TEMP)
    private double mTemp;
    @SerializedName(FIELD_TEMP_MAX)
    private double mTempMax;
    @SerializedName(FIELD_PRESSURE)
    private double mPressure;
    @SerializedName(FIELD_HUMIDITY)
    private int mHumidity;
    @SerializedName(FIELD_SEA_LEVEL)
    private double mSeaLevel;
    @SerializedName(FIELD_TEMP_MIN)
    private double mTempMin;
    @SerializedName(FIELD_GRND_LEVEL)
    private double mGrndLevel;


    public Main(){

    }

    public void setTemp(double temp) {
        mTemp = temp;
    }

    public double getTemp() {
        return mTemp;
    }

    public void setTempMax(double tempMax) {
        mTempMax = tempMax;
    }

    public double getTempMax() {
        return mTempMax;
    }

    public void setPressure(double pressure) {
        mPressure = pressure;
    }

    public double getPressure() {
        return mPressure;
    }

    public void setHumidity(int humidity) {
        mHumidity = humidity;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public void setSeaLevel(double seaLevel) {
        mSeaLevel = seaLevel;
    }

    public double getSeaLevel() {
        return mSeaLevel;
    }

    public void setTempMin(double tempMin) {
        mTempMin = tempMin;
    }

    public double getTempMin() {
        return mTempMin;
    }

    public void setGrndLevel(double grndLevel) {
        mGrndLevel = grndLevel;
    }

    public double getGrndLevel() {
        return mGrndLevel;
    }

    public Main(Parcel in) {
        mTemp = in.readDouble();
        mTempMax = in.readDouble();
        mPressure = in.readDouble();
        mHumidity = in.readInt();
        mSeaLevel = in.readDouble();
        mTempMin = in.readDouble();
        mGrndLevel = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Main> CREATOR = new Parcelable.Creator<Main>() {
        public Main createFromParcel(Parcel in) {
            return new Main(in);
        }

        public Main[] newArray(int size) {
        return new Main[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mTemp);
        dest.writeDouble(mTempMax);
        dest.writeDouble(mPressure);
        dest.writeInt(mHumidity);
        dest.writeDouble(mSeaLevel);
        dest.writeDouble(mTempMin);
        dest.writeDouble(mGrndLevel);
    }


    @Override
    public String toString() {
        return "Main{" +
                "mTemp=" + mTemp +
                ", mTempMax=" + mTempMax +
                ", mPressure=" + mPressure +
                ", mHumidity=" + mHumidity +
                ", mSeaLevel=" + mSeaLevel +
                ", mTempMin=" + mTempMin +
                ", mGrndLevel=" + mGrndLevel +
                '}';
    }
}