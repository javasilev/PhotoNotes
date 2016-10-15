package com.javasilev.photonotes.models.response;

import com.google.gson.annotations.SerializedName;

public class TextAnnotation {

	@SerializedName("locale")
	private String mLocale;

	@SerializedName("description")
	private String mContent;

	@SerializedName("boundingPoly")
	private BoundingPoly mBoundingPoly;

	public TextAnnotation() {
	}

	public TextAnnotation(String locale, String content, BoundingPoly boundingPoly) {
		mLocale = locale;
		mContent = content;
		mBoundingPoly = boundingPoly;
	}

	public String getLocale() {
		return mLocale;
	}

	public void setLocale(String locale) {
		mLocale = locale;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		mContent = content;
	}

	public BoundingPoly getBoundingPoly() {
		return mBoundingPoly;
	}

	public void setBoundingPoly(BoundingPoly boundingPoly) {
		mBoundingPoly = boundingPoly;
	}
}
