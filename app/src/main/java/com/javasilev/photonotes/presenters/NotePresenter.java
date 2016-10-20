package com.javasilev.photonotes.presenters;

import com.javasilev.photonotes.data.RealmDataSource;
import com.javasilev.photonotes.models.Note;

/**
 * Created by Aleksei Vasilev.
 */

public class NotePresenter {
	private RealmDataSource mDataSource = RealmDataSource.getInstance();

	public Note createOrUpdate(Note note) {
		return mDataSource.createOrUpdate(note);
	}

	public Note getNote(long noteId) {
		return mDataSource.getItem(noteId);
	}

	public void deleteNote(long noteId) {
		mDataSource.deleteItem(noteId);
	}
}
