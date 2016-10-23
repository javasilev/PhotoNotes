package com.javasilev.photonotes.presenters;

import java.util.List;

import com.javasilev.photonotes.data.RealmDataSource;
import com.javasilev.photonotes.models.Note;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Aleksei Vasilev.
 */

public class NoteListController {
	private RealmDataSource mDataSource = RealmDataSource.getInstance();
	private Observer<List<Note>> mNoteObserver;

	public NoteListController(Observer<List<Note>> noteObserver) {
		mNoteObserver = noteObserver;
	}

	public void loadNotes() {
		load(null);
	}

	public void findNotes(final String searchQuery) {
		load(searchQuery);
	}

	private void load(final String query) {
		Observable.create(
				new Observable.OnSubscribe<List<Note>>() {
					@Override
					public void call(Subscriber<? super List<Note>> subscriber) {
						try {
							List<Note> notes;
							if (query != null) {
								notes = mDataSource.find(query);
							} else {
								notes = mDataSource.getAll();
							}
							subscriber.onNext(notes);
						} catch (Exception e) {
							subscriber.onError(e);
						}

						subscriber.onCompleted();
					}
				})
				.subscribeOn(AndroidSchedulers.mainThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(mNoteObserver);
	}

	public void deleteNote(long noteId) {
		mDataSource.deleteItem(noteId);
	}
}
