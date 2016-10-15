package com.javasilev.photonotes.models.response;

import com.google.gson.annotations.SerializedName;

public class Error {

	@SerializedName("code")
	private int mCode;

	@SerializedName("message")
	private String mMessage;

	public Error() {
	}

	public Error(int code, String message) {
		mCode = code;
		mMessage = message;
	}

	public int getCode() {
		return mCode;
	}

	public void setCode(int code) {
		mCode = code;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String message) {
		mMessage = message;
	}
}
