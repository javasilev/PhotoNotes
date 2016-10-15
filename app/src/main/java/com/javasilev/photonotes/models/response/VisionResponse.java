package com.javasilev.photonotes.models.response;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Aleksei Vasilev.
 */

public class VisionResponse {

	@SerializedName("responses")
	private List<Response> mResponses;

	public VisionResponse() {
	}

	public VisionResponse(List<Response> responses) {
		mResponses = responses;
	}

	public List<Response> getResponses() {
		return mResponses;
	}

	public void setResponses(List<Response> responses) {
		mResponses = responses;
	}
}
