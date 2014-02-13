package com.missionse.mapsexample;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.missionse.mapsexample.model.MyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MyItemReader {

    /*
     * This matches only once in whole input,
     * so Scanner.next returns whole InputStream as a String.
     * http://stackoverflow.com/a/5445161/2183804
     */
    private static final String REGEX_INPUT_BOUNDARY_BEGINNING = "\\A";

    public static List<MyItem> read(Context context, InputStream inputStream) throws JSONException {
        Random random = new Random(1986);
        List<MyItem> items = new ArrayList<MyItem>();
        String json = new Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next();
        JSONArray array = new JSONArray(json);
        TypedArray photos = context.getResources().obtainTypedArray(R.array.item_photo_values);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
//            String name = object.getString("name");
            String name = "name";
            int photoIndex = random.nextInt(photos.length()-1);
            items.add(new MyItem(new LatLng(lat, lng), name, photos.getResourceId(photoIndex, R.drawable.photo_1)));
        }
        return items;
    }
}

