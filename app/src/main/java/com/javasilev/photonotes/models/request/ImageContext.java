package com.javasilev.photonotes.models.request;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ImageContext {

	@SerializedName("latLongRect")
	private LatLongRect mLatLongRect;

	@SerializedName("languageHints")
	private List<String> mLanguageHints;

	public ImageContext() {
	}

	public ImageContext(LatLongRect latLongRect, List<String> languageHints) {
		mLatLongRect = latLongRect;
		mLanguageHints = languageHints;
	}

	public LatLongRect getLatLongRect() {
		return mLatLongRect;
	}

	public void setLatLongRect(LatLongRect latLongRect) {
		mLatLongRect = latLongRect;
	}

	public List<String> getLanguageHints() {
		return mLanguageHints;
	}

	public void setLanguageHints(List<String> languageHints) {
		mLanguageHints = languageHints;
	}
}
