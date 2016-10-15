package com.javasilev.photonotes.models.request;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Aleksei Vasilev.
 */

public class VisionRequest {

	@SerializedName("requests")
	private List<Request> mRequests;
}
