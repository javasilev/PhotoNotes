package com.javasilev.photonotes.models.response;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Response {

	@SerializedName("error")
	private Error mError;

	@SerializedName("textAnnotations")
	private List<TextAnnotation> mTextAnnotations;

	public Response() {
	}

	public Response(Error error, List<TextAnnotation> textAnnotations) {
		mError = error;
		mTextAnnotations = textAnnotations;
	}

	public Error getError() {
		return mError;
	}

	public void setError(Error error) {
		mError = error;
	}

	public List<TextAnnotation> getTextAnnotations() {
		return mTextAnnotations;
	}

	public void setTextAnnotations(List<TextAnnotation> textAnnotations) {
		mTextAnnotations = textAnnotations;
	}
}
