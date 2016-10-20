package com.javasilev.photonotes.presenters;

import java.io.IOException;
import java.util.ArrayList;
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
import com.javasilev.photonotes.models.response.Error;
import com.javasilev.photonotes.models.response.Response;
import com.javasilev.photonotes.models.response.VisionResponse;
import com.javasilev.photonotes.network.VisionService;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Aleksei Vasilev.
 */

public class VisionPresenter {
	private Observer<List<Response>> mVisionObserver;
	private Context mContext;

	private Subscription mSubscription;

	public VisionPresenter(Observer<List<Response>> visionObserver, Context context) {
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
		final String apiKey = prefs.getString(mContext.getString(R.string.key_api), "INSERT YOUR API_KEY HERE");

		final String fields = "responses(error,textAnnotations)";

		mSubscription =
				Observable.create(new Observable.OnSubscribe<VisionRequest>() {
					@Override
					public void call(Subscriber<? super VisionRequest> subscriber) {
						subscriber.onNext(createRequest(uri, langHints));
						subscriber.onCompleted();
					}
				})
				.subscribeOn(Schedulers.newThread())
				.map(visionRequest -> {
					VisionResponse response = new VisionResponse();
					try {
						retrofit2.Response retrofitResponse = VisionService.getInstance(mContext).createVisionClient()
								.getVisionResponse(visionRequest, fields, apiKey)
								.execute();
						if (retrofitResponse.code() == 400) {
							response = new VisionResponse(Collections.singletonList(new Response(new Error(400, mContext.getString(R.string.wrong_api)), new ArrayList<>())));
						} else {
							response = (VisionResponse) retrofitResponse.body();
						}
					} catch (IOException e) {
						response = new VisionResponse(Collections.singletonList(new Response(null, new ArrayList<>())));
					}
					return response;
				})
				.map(VisionResponse::getResponses)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(mVisionObserver);
	}

	public boolean isInProcess() {
		return !(mSubscription == null || mSubscription.isUnsubscribed());
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
