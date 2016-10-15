package com.javasilev.photonotes.models.request;

import com.google.gson.annotations.SerializedName;

public class Image {

	@SerializedName("content")
	private String mContent;

	@SerializedName("source")
	private Source mSource;

	public Image() {
	}

	public Image(String content, Source source) {
		mContent = content;
		mSource = source;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		mContent = content;
	}

	public Source getSource() {
		return mSource;
	}

	public void setSource(Source source) {
		mSource = source;
	}
}
