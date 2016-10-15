package com.javasilev.photonotes.models.request;

import com.google.gson.annotations.SerializedName;

public class LatLongRect {

	@SerializedName("minLatLng")
	private MinLatLng mMinLatLng;

	@SerializedName("maxLatLng")
	private MaxLatLng mMaxLatLng;

	public LatLongRect() {
	}

	public LatLongRect(MinLatLng minLatLng, MaxLatLng maxLatLng) {
		mMinLatLng = minLatLng;
		mMaxLatLng = maxLatLng;
	}

	public MinLatLng getMinLatLng() {
		return mMinLatLng;
	}

	public void setMinLatLng(MinLatLng minLatLng) {
		mMinLatLng = minLatLng;
	}

	public MaxLatLng getMaxLatLng() {
		return mMaxLatLng;
	}

	public void setMaxLatLng(MaxLatLng maxLatLng) {
		mMaxLatLng = maxLatLng;
	}
}
