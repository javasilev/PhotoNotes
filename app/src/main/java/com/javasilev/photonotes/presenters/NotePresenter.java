package com.javasilev.photonotes.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.javasilev.photonotes.data.RealmDataSource;
import com.javasilev.photonotes.models.Note;
import com.javasilev.photonotes.views.NoteView;

/**
 * Created by Aleksei Vasilev.
 */

@InjectViewState
public class NotePresenter extends MvpPresenter<NoteView> {
	private RealmDataSource mDataSource = RealmDataSource.getInstance();

	public void getNote(long noteId) {
		getViewState().setNote(mDataSource.getItem(noteId));
	}

	public void createOrUpdate(Note note) {
		mDataSource.createOrUpdate(note);
	}

	public void deleteNote(long noteId) {
		mDataSource.deleteItem(noteId);
	}
}
