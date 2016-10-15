package com.javasilev.photonotes.models.request;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Request {

	@SerializedName("image")
	private Image mImage;

	@SerializedName("features")
	private List<Feature> mFeatures;

	@SerializedName("imageContext")
	private ImageContext mImageContext;

	public Request() {
	}

	public Request(Image image, List<Feature> features, ImageContext imageContext) {
		mImage = image;
		mFeatures = features;
		mImageContext = imageContext;
	}

	public Image getImage() {
		return mImage;
	}

	public void setImage(Image image) {
		mImage = image;
	}

	public List<Feature> getFeatures() {
		return mFeatures;
	}

	public void setFeatures(List<Feature> features) {
		mFeatures = features;
	}

	public ImageContext getImageContext() {
		return mImageContext;
	}

	public void setImageContext(ImageContext imageContext) {
		mImageContext = imageContext;
	}
}
