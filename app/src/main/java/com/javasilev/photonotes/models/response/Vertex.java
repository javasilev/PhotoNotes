package com.javasilev.photonotes.models.response;

import com.google.gson.annotations.SerializedName;

public class Vertex {

	@SerializedName("x")
	private int mX;

	@SerializedName("y")
	private int mY;

	public Vertex() {
	}

	public Vertex(int x, int y) {
		mX = x;
		mY = y;
	}

	public int getX() {
		return mX;
	}

	public void setX(int x) {
		mX = x;
	}

	public int getY() {
		return mY;
	}

	public void setY(int y) {
		mY = y;
	}
}
