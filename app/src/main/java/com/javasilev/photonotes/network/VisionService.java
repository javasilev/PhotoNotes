package com.javasilev.photonotes.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.javasilev.photonotes.urls.VisionApiUrls;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aleksei Vasilev.
 */

public class VisionService {
	private static VisionService sInstance;

	private final Retrofit mRetrofit;

	public static VisionService getInstance(Context context) {
		if (sInstance == null) {
			synchronized (VisionService.class) {
				if (sInstance == null) {
					sInstance = new VisionService(context);
				}
			}
		}

		return sInstance;
	}

	private VisionService(Context context) {
		mRetrofit = new Retrofit.Builder()
				.baseUrl(VisionApiUrls.getApiBaseUrl(context))
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.client(createOkHttpClient())
				.build();
	}

	@NonNull
	private OkHttpClient createOkHttpClient() {
		HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
		httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

		return new OkHttpClient.Builder()
				.addInterceptor(httpLoggingInterceptor)
				.build();
	}

	public VisionClient createVisionClient() {
		return mRetrofit.create(VisionClient.class);
	}
}
