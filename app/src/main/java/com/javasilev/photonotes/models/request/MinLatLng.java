package com.javasilev.photonotes.models.request;

import com.google.gson.annotations.SerializedName;

public class MinLatLng {

	@SerializedName("latitude")
	private int mLatitude;

	@SerializedName("longitude")
	private int mLongitude;

	public MinLatLng() {
	}

	public MinLatLng(int latitude, int longitude) {
		mLatitude = latitude;
		mLongitude = longitude;
	}

	public int getLatitude() {
		return mLatitude;
	}

	public void setLatitude(int latitude) {
		mLatitude = latitude;
	}

	public int getLongitude() {
		return mLongitude;
	}

	public void setLongitude(int longitude) {
		mLongitude = longitude;
	}
}
