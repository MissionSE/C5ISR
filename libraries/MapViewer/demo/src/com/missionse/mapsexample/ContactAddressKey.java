package com.missionse.mapsexample;


public class ContactAddressKey {
	
	private String mAddress;
	private String mName;
	private int mType;

	public ContactAddressKey(int type, String name, String address) {
		mType = type;
		mName = name;
		mAddress = address;
	}
	
	public ContactAddressKey(MarkerInfo markerInfo) {
		mType = markerInfo.getType();
		mName = markerInfo.getTitle();
		mAddress = markerInfo.getAddress();
	}

	/**
	 * @return the mAddress
	 */
	public String getmAddress() {
		return mAddress;
	}

	/**
	 * @return the mName
	 */
	public String getmName() {
		return mName;
	}

	/**
	 * @return the mType
	 */
	public int getmType() {
		return mType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ContactAddressKey other = (ContactAddressKey) obj;
		if (mAddress == null) {
			if (other.mAddress != null) {
				return false;
			}
		} else if (!mAddress.equals(other.mAddress)) {
			return false;
		}
		if (mName == null) {
			if (other.mName != null) {
				return false;
			}
		} else if (!mName.equals(other.mName)) {
			return false;
		}
		if (mType != other.mType) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mAddress == null) ? 0 : mAddress.hashCode());
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
		result = prime * result + mType;
		return result;
	}
}