package com.javasilev.photonotes.async;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.javasilev.photonotes.R;
import com.javasilev.photonotes.models.request.Feature;
import com.javasilev.photonotes.models.request.Request;
import com.javasilev.photonotes.models.request.VisionRequest;
import com.javasilev.photonotes.models.request.VisionRequestBuilder;
import com.javasilev.photonotes.models.response.Response;
import com.javasilev.photonotes.models.response.TextAnnotation;
import com.javasilev.photonotes.models.response.VisionResponse;
import com.javasilev.photonotes.network.VisionClient;
import com.javasilev.photonotes.network.VisionService;

import retrofit2.Call;

/**
 * Created by Aleksei Vasilev.
 */

public class VisionAsyncTask extends LoadAsyncTask<List<TextAnnotation>> {
	private static final String API_KEY = "INSERT KEY HERE";

	public VisionAsyncTask(Context context, ResultListener<List<TextAnnotation>> resultListener) {
		super(context, resultListener);
	}

	@Override
	protected List<TextAnnotation> doInBackground(String... params) {
		mStatus = Status.PROGRESS;

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		Set<String> langHints = prefs.getStringSet(mContext.getString(R.string.key_lang), Collections.singleton("ru"));

		Uri uri = Uri.parse(params[0]);

		String fields = "responses(error,textAnnotations)";

		Request request = new VisionRequestBuilder(mContext)
				.addImage(uri)
				.addFeature(Feature.Type.TEXT_DETECTION, 1)
				.addImageContext(langHints.toArray(new String[langHints.size()]))
				.create();

		VisionRequest visionRequest = new VisionRequest(Collections.singletonList(request));

		VisionClient visionClient = VisionService.getInstance(mContext).createVisionClient();

		Call<VisionResponse> visionResponseCall = visionClient.getVisionResponse(visionRequest, fields, API_KEY);

		try {
			VisionResponse visionResponse = visionResponseCall.execute().body();
			Response response = visionResponse.getResponses().get(0);

			if (response != null) {
				mResultList = response.getTextAnnotations();
			}

			mStatus = Status.COMPLETE;
		} catch (Exception e) {
			mException = e;
			mStatus = Status.ERROR;
		}

		return mResultList;
	}
}
