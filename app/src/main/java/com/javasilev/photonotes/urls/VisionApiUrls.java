package com.javasilev.photonotes.urls;

import android.content.Context;

import com.javasilev.photonotes.R;

/**
 * Created by Aleksei Vasilev.
 */

public final class VisionApiUrls {
	static final String API = "/v1";

	public static class Vision {
		static final String BASE = API + "/images";

		public static final String GET_TEXT = BASE + ":annotate";
	}

	public static String getApiBaseUrl(Context context) {
		return context.getString(R.string.base_api_url);
	}
}
