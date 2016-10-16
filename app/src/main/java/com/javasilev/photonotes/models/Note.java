package com.javasilev.photonotes.models;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Aleksei Vasilev.
 */

public class Note extends RealmObject implements Serializable {
	@PrimaryKey
	private long id;

	private Date creationDate;
	private String name;
	private String text;

	public Note() {
	}

	public Note(long id, Date creationDate, String name, String text) {
		this.id = id;
		this.creationDate = creationDate;
		this.name = name;
		this.text = text;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
