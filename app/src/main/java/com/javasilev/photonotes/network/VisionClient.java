package com.javasilev.photonotes.network;

import com.javasilev.photonotes.models.request.VisionRequest;
import com.javasilev.photonotes.models.response.VisionResponse;
import com.javasilev.photonotes.urls.VisionApiUrls;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by Aleksei Vasilev.
 */

public interface VisionClient {

	@POST(VisionApiUrls.Vision.GET_TEXT)
	Call<VisionResponse> getVisionResponse(
			@Body VisionRequest body,
			@Field("fields") String responseFields, // "responses(error,textAnnotations)"
			@Field("key") String apiKey);
}
