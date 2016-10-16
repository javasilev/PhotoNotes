package com.javasilev.photonotes.db;

import java.util.Date;
import java.util.List;

import com.javasilev.photonotes.models.Note;

import io.realm.Realm;

/**
 * Created by Aleksei Vasilev.
 */

public class NoteDataSource {
	private Realm mRealm;

	private static NoteDataSource sInstance;

	public static NoteDataSource getInstance() {
		if (sInstance == null) {
			synchronized (NoteDataSource.class) {
				if (sInstance == null) {
					sInstance = new NoteDataSource();
				}
			}
		}
		return sInstance;
	}

	private NoteDataSource() {
	}

	public Note createOrUpdateNote(long id, Date date, String name, String text) {

		if (id == 0) {
			id = getNextKey();
		}

		mRealm.beginTransaction();

		Note note = new Note(id, date, name, text);

		mRealm.copyToRealmOrUpdate(note);

		mRealm.commitTransaction();

		return note;
	}

	public Note getNote(long noteId) {
		return mRealm.where(Note.class).equalTo("id", noteId).findFirst();
	}

	public void deleteNote(long noteId) {
		mRealm.beginTransaction();
		mRealm.where(Note.class).equalTo("id", noteId).findFirst().deleteFromRealm();
		mRealm.commitTransaction();
	}

	public List<Note> getAllNotes() {
		return mRealm.where(Note.class).findAll();
	}

	public List<Note> findNotes(String searchQuery) {
		return mRealm.where(Note.class)
				.contains("name", searchQuery)
				.or().contains("text", searchQuery)
				.findAll();
	}

	public void init() {
		mRealm = Realm.getDefaultInstance();
	}

	public void close() {
		mRealm.close();
	}

	private long getNextKey() {
		Number maxId = mRealm.where(Note.class).max("id");

		return maxId == null ? 1 : maxId.longValue() + 1;
	}
}
