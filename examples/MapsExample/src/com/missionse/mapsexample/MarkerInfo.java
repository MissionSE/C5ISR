package com.missionse.mapsexample;

import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MarkerInfo {
	public static final String TYPE_HOME = "HOME";
	public static final String TYPE_OTHER = "OTHER";
	public static final String TYPE_WORK = "WORK";
	
	private String mAddress;
	private long mContactId;
	private float mIconHue;
	private double mLat;
	private double mLng;
	private String mLookupKey;
	private String mTitle;
	private int mType;

	/**
	 * @param contactId
	 * @param iconHue
	 * @param lat
	 * @param lng
	 * @param lookupKey
	 * @param title
	 * @param type
	 */
	public MarkerInfo(String address, long contactId, float iconHue, double lat, double lng,
			String lookupKey, String title, int type) {
		this.mAddress = address;
		this.mContactId = contactId;
		this.mIconHue = iconHue;
		this.mLat = lat;
		this.mLng = lng;
		this.mLookupKey = lookupKey;
		this.mTitle = title;
		this.mType = type;
	}

	/**
	 * @return the mAddress
	 */
	public String getAddress() {
		return mAddress;
	}

	/**
	 * @return the mContactId
	 */
	public long getContactId() {
		return mContactId;
	}

	/**
	 * @return the mIconHue
	 */
	public float getIconHue() {
		return mIconHue;
	}

	/**
	 * @return the mLat
	 */
	public double getLat() {
		return mLat;
	}

	/**
	 * @return the mLng
	 */
	public double getLng() {
		return mLng;
	}

	/**
	 * @return the mLookupKey
	 */
	public String getLookupKey() {
		return mLookupKey;
	}

	/**
	 * @return the mTitle
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * @return the mType
	 */
	public int getType() {
		return mType;
	}
	
	/**
	 * @param type
	 * @return
	 */
	public static String getTypeAsString(int type) {
		String typeString = null;
		switch (type) {
		case StructuredPostal.TYPE_HOME:
			typeString = TYPE_HOME;
			break;
		case StructuredPostal.TYPE_WORK:
			typeString = TYPE_WORK;
			break;
		case StructuredPostal.TYPE_OTHER:
			typeString = TYPE_OTHER;
			break;
		default:
			break;
		}
		return typeString;
	}

	/**
	 * @return marker options set to this marker info's properties
	 */
	public MarkerOptions getMarkerOptions() {
		return new MarkerOptions()
		.draggable(false)
		.icon(BitmapDescriptorFactory.defaultMarker(mIconHue))
		.position(new LatLng(mLat, mLng))
		.snippet(getTypeAsString(mType))
		.title(mTitle)
		.visible(false);
	}

}