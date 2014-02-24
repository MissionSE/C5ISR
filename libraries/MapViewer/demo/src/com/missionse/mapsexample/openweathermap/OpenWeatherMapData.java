package com.missionse.mapsexample.openweathermap;

import java.util.ArrayList;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import android.os.Parcel;


public class OpenWeatherMapData implements Parcelable{

    private static final String FIELD_ID = "id";
    private static final String FIELD_COD = "cod";
    private static final String FIELD_WIND = "wind";
    private static final String FIELD_SYS = "sys";
    private static final String FIELD_BASE = "base";
    private static final String FIELD_DT = "dt";
    private static final String FIELD_COORD = "coord";
    private static final String FIELD_WEATHER = "weather";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_MAIN = "main";
    private static final String FIELD_CLOUDS = "clouds";


    @SerializedName(FIELD_ID)
    private int mId;
    @SerializedName(FIELD_COD)
    private int mCod;
    @SerializedName(FIELD_WIND)
    private Wind mWind;
    @SerializedName(FIELD_SYS)
    private Sy mSys;
    @SerializedName(FIELD_BASE)
    private String mBase;
    @SerializedName(FIELD_DT)
    private int mDt;
    @SerializedName(FIELD_COORD)
    private Coord mCoord;
    @SerializedName(FIELD_WEATHER)
    private List<Weather> mWeathers;
    @SerializedName(FIELD_NAME)
    private String mName;
    @SerializedName(FIELD_MAIN)
    private Main mMain;
    @SerializedName(FIELD_CLOUDS)
    private Cloud mCloud;


    public OpenWeatherMapData(){

    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public void setCod(int cod) {
        mCod = cod;
    }

    public int getCod() {
        return mCod;
    }

    public void setWind(Wind wind) {
        mWind = wind;
    }

    public Wind getWind() {
        return mWind;
    }

    public void setSy(Sy sy) {
        mSys = sy;
    }

    public Sy getSy() {
        return mSys;
    }

    public void setBase(String base) {
        mBase = base;
    }

    public String getBase() {
        return mBase;
    }

    public void setDt(int dt) {
        mDt = dt;
    }

    public int getDt() {
        return mDt;
    }

    public void setCoord(Coord coord) {
        mCoord = coord;
    }

    public Coord getCoord() {
        return mCoord;
    }

    public void setWeathers(List<Weather> weathers) {
        mWeathers = weathers;
    }

    public List<Weather> getWeathers() {
        return mWeathers;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setMain(Main main) {
        mMain = main;
    }

    public Main getMain() {
        return mMain;
    }

    public void setCloud(Cloud cloud) {
        mCloud = cloud;
    }

    public Cloud getCloud() {
        return mCloud;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof OpenWeatherMapData){
            return ((OpenWeatherMapData) obj).getId() == mId;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return mId;
    }

    public OpenWeatherMapData(Parcel in) {
        mId = in.readInt();
        mCod = in.readInt();
        mWind = in.readParcelable(Wind.class.getClassLoader());
        mSys = in.readParcelable(Sy.class.getClassLoader());
        mBase = in.readString();
        mDt = in.readInt();
        mCoord = in.readParcelable(Coord.class.getClassLoader());
        mWeathers = new ArrayList<Weather>();
        in.readTypedList(mWeathers, Weather.CREATOR);
        mName = in.readString();
        mMain = in.readParcelable(Main.class.getClassLoader());
        mCloud = in.readParcelable(Cloud.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<OpenWeatherMapData> CREATOR = new Parcelable.Creator<OpenWeatherMapData>() {
        public OpenWeatherMapData createFromParcel(Parcel in) {
            return new OpenWeatherMapData(in);
        }

        public OpenWeatherMapData[] newArray(int size) {
        return new OpenWeatherMapData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mCod);
        dest.writeParcelable(mWind, flags);
        dest.writeParcelable(mSys, flags);
        dest.writeString(mBase);
        dest.writeInt(mDt);
        dest.writeParcelable(mCoord, flags);
        dest.writeTypedList(mWeathers);
        dest.writeString(mName);
        dest.writeParcelable(mMain, flags);
        dest.writeParcelable(mCloud, flags);
    }

    @Override
    public String toString() {
        return "OpenWeatherMapData{" +
                "mId=" + mId +
                ", mCod=" + mCod +
                ", mWind=" + mWind +
                ", mSys=" + mSys +
                ", mBase='" + mBase + '\'' +
                ", mDt=" + mDt +
                ", mCoord=" + mCoord +
                ", mWeathers=" + mWeathers +
                ", mName='" + mName + '\'' +
                ", mMain=" + mMain +
                ", mCloud=" + mCloud +
                '}';
    }


}