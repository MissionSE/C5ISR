package com.missionse.augmented.utils;

import android.location.Location;

public class GeoUtils {
	
	public static Location addMetersToGps(Location tPosition, int tDx, int tDy){
		//12th(go down) - 14th(come up)  
		//weds night  ??
		double pi = Math.PI;
		
		double lat0 = tPosition.getLatitude();
		double lon0 = tPosition.getLongitude();
		
		double denominator = Math.cos(pi/180.0*lat0);
		
		double lat = lat0 + (180/pi)*(tDy/6378137);
		double lon = lon0 + (180/pi)*(tDx/6378137)/denominator;
		
		Location r = new Location("");
		r.setLatitude(lat);
		r.setLongitude(lon);
		
		return r;
		
	}

}
