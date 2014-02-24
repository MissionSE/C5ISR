package com.missionse.mapsexample.openweathermap;

import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import android.os.Parcel;


public class Weather implements Parcelable{

    private static final String FIELD_ID = "id";
    private static final String FIELD_ICON = "icon";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_MAIN = "main";


    @SerializedName(FIELD_ID)
    private int mId;
    @SerializedName(FIELD_ICON)
    private String mIcon;
    @SerializedName(FIELD_DESCRIPTION)
    private String mDescription;
    @SerializedName(FIELD_MAIN)
    private String mMain;


    public Weather(){

    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setMain(String main) {
        mMain = main;
    }

    public String getMain() {
        return mMain;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Weather){
            return ((Weather) obj).getId() == mId;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return mId;
    }

    public Weather(Parcel in) {
        mId = in.readInt();
        mIcon = in.readString();
        mDescription = in.readString();
        mMain = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        public Weather[] newArray(int size) {
        return new Weather[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mIcon);
        dest.writeString(mDescription);
        dest.writeString(mMain);
    }


    @Override
    public String toString() {
        return "Weather{" +
                "mId=" + mId +
                ", mIcon='" + mIcon + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mMain='" + mMain + '\'' +
                '}';
    }
}