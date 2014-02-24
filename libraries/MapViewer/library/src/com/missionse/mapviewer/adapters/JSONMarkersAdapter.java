package com.missionse.mapviewer.adapters;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class JSONMarkersAdapter extends DataMarkersAdapter<JSONArray, JSONObject, String>
{
    String mLatitudeKey = "latitude";
    String mLongitudeKey = "longitude";
    String mMarkerKey;

    public JSONMarkersAdapter(Context paramContext, GoogleMap paramGoogleMap, String paramString, int paramInt, DataViewBinder<JSONObject> paramDataViewBinder)
    {
        super(paramContext, paramGoogleMap, paramInt, paramDataViewBinder);
        this.mMarkerKey = paramString;
    }

    @Override
    public int getCount()
    {
        JSONArray localJSONArray = (JSONArray)getData();
        if (localJSONArray != null);
        for (int i = localJSONArray.length(); ; i = 0)
            return i;
    }

    public String getLatitudeKey()
    {
        return this.mLatitudeKey;
    }

    public String getLongitudeKey()
    {
        return this.mLongitudeKey;
    }

    @Override
    public JSONObject getMarkerData(int paramInt)
    {
        return ((JSONArray)getData()).optJSONObject(paramInt);
    }

    public String getMarkerKey()
    {
        return this.mMarkerKey;
    }

    @Override
    public String getMarkerKey(JSONObject paramJSONObject)
    {
        return paramJSONObject.optString(this.mMarkerKey);
    }

    @Override
    public double getMarkerLatitude(JSONObject paramJSONObject)
    {
        return paramJSONObject.optDouble(this.mLatitudeKey);
    }

    @Override
    public double getMarkerLongitude(JSONObject paramJSONObject)
    {
        return paramJSONObject.optDouble(this.mLongitudeKey);
    }

    public void setLatitudeKey(String paramString)
    {
        this.mLatitudeKey = paramString;
    }

    public void setLongitudeKey(String paramString)
    {
        this.mLongitudeKey = paramString;
    }

    public void setMarkerKey(String paramString)
    {
        this.mMarkerKey = paramString;
    }
}