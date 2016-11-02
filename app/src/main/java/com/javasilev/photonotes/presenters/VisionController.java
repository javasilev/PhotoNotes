package com.javasilev.photonotes.presenters;

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
import com.javasilev.photonotes.models.response.VisionResponse;
import com.javasilev.photonotes.network.VisionService;
import com.javasilev.photonotes.utils.KeyManager;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Aleksei Vasilev.
 */

@SuppressWarnings("WeakerAccess")
public class VisionController {
	private static final String TAG = "PN_VisionContrlr";

	private Observer<List<Response>> mVisionObserver;
	private Context mContext;

	private Subscription mSubscription;

	public VisionController(Observer<List<Response>> visionObserver, Context context) {
		mVisionObserver = visionObserver;
		mContext = context;
	}

	public void stop() {
		if (mSubscription != null) {
			mSubscription.unsubscribe();
		}
	}

	public void load(final Uri uri) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		final Set<String> langHints = prefs.getStringSet(mContext.getString(R.string.key_lang), Collections.singleton("ru"));
		final String apiKey = KeyManager.getKey();

		final String fields = "responses(error,textAnnotations)";

		mSubscription = Observable.fromCallable(() -> createRequest(uri, langHints))
				.subscribeOn(Schedulers.io())
				.flatMap(visionRequest -> VisionService.getInstance(mContext).createVisionClient().getVisionResponse(visionRequest, fields, apiKey))
				.map(VisionResponse::getResponses)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(mVisionObserver);
	}

	private VisionRequest createRequest(Uri uri, Set<String> langHints) {
		Request request = new VisionRequestBuilder(mContext)
				.addImage(uri)
				.addFeature(Feature.Type.TEXT_DETECTION, 1)
				.addImageContext(langHints.toArray(new String[langHints.size()]))
				.create();
		return new VisionRequest(Collections.singletonList(request));
	}
}
