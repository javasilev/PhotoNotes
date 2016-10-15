package com.javasilev.photonotes.models.response;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class BoundingPoly {

	@SerializedName("vertices")
	private List<Vertex> mVertices;

	public BoundingPoly() {
	}

	public BoundingPoly(List<Vertex> vertices) {
		mVertices = vertices;
	}

	public List<Vertex> getVertices() {
		return mVertices;
	}

	public void setVertices(List<Vertex> vertices) {
		mVertices = vertices;
	}
}
