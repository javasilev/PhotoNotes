package com.javasilev.photonotes.models.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;

import com.squareup.picasso.Picasso;

/**
 * Created by Aleksei Vasilev.
 */

public class VisionRequestBuilder {
	private Context mContext;
	private Request mRequest;

	public VisionRequestBuilder(Context context) {
		mContext = context;
		mRequest = new Request();
	}

	public VisionRequestBuilder addImage(Uri uri) {
		Bitmap bmp = null;
		try {
			bmp = Picasso.with(mContext)
					.load(uri)
					.resize(1280, 1280)
					.onlyScaleDown()
					.get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		assert bmp != null;
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

		String imageString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
		Image image = new Image(imageString, null);

		mRequest.setImage(image);

		return this;
	}

	public VisionRequestBuilder addFeature(Feature.Type type, int maxResults) {
		Feature feature = new Feature(type, maxResults);

		mRequest.setFeatures(Collections.singletonList(feature));

		return this;
	}

	public VisionRequestBuilder addImageContext(String... langCodes) {
		List<String> languageHints = Arrays.asList(langCodes);
		ImageContext imageContext = new ImageContext(null, languageHints);

		mRequest.setImageContext(imageContext);

		return this;
	}

	public Request create() {
		return mRequest;
	}
}
