package com.javasilev.photonotes.presenters;

import java.util.List;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.javasilev.photonotes.data.RealmDataSource;
import com.javasilev.photonotes.models.Note;
import com.javasilev.photonotes.views.NoteListView;

import rx.Observer;

/**
 * Created by Aleksei Vasilev.
 */

@InjectViewState
public class NoteListPresenter extends MvpPresenter<NoteListView> implements Observer<List<Note>> {
	private RealmDataSource mDataSource = RealmDataSource.getInstance();
	private NoteListController mController = new NoteListController(this);

	public void loadNotes() {
		getViewState().showProgress();
		getViewState().disableList();
		mController.loadNotes();
	}

	public void findNotes(String query) {
		getViewState().showProgress();
		getViewState().disableList();
		mController.findNotes(query);
	}

	public void userDeleteNote(long id) {
		mDataSource.deleteItem(id);
	}

	@Override
	public void onCompleted() {
		getViewState().hideProgress();
		getViewState().enableList();
	}

	@Override
	public void onError(Throwable e) {
		getViewState().showError(e.getMessage());
	}

	@Override
	public void onNext(List<Note> notes) {
		getViewState().setList(notes);
	}
}
