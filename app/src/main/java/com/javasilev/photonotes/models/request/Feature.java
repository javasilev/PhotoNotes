package com.javasilev.photonotes.models.request;

import com.google.gson.annotations.SerializedName;

public class Feature {

	@SerializedName("type")
	private Type mType;

	@SerializedName("maxResults")
	private int mMaxResults;

	public Feature() {
	}

	public Feature(Type type, int maxResults) {
		mType = type;
		mMaxResults = maxResults;
	}

	public Type getType() {
		return mType;
	}

	public void setType(Type type) {
		mType = type;
	}

	public int getMaxResults() {
		return mMaxResults;
	}

	public void setMaxResults(int maxResults) {
		mMaxResults = maxResults;
	}

	public enum Type {
		TEXT_DETECTION
	}
}
