package com.javasilev.photonotes.network;

import com.javasilev.photonotes.models.request.VisionRequest;
import com.javasilev.photonotes.models.response.VisionResponse;
import com.javasilev.photonotes.urls.VisionApiUrls;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Aleksei Vasilev.
 */

public interface VisionClient {

	@POST(VisionApiUrls.Vision.GET_TEXT)
	Observable<VisionResponse> getVisionResponse(
			@Body VisionRequest body,
			@Query("fields") String responseFields, // "responses(error,textAnnotations)"
			@Query("key") String apiKey);
}
