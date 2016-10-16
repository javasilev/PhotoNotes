package com.javasilev.photonotes.async;

import java.util.List;

import android.content.Context;

import com.javasilev.photonotes.db.NoteDataSource;
import com.javasilev.photonotes.models.Note;

/**
 * Created by Aleksei Vasilev.
 */

public class NotesAsyncTask extends LoadAsyncTask<List<Note>> {

	public NotesAsyncTask(Context context, ResultListener<List<Note>> resultListener) {
		super(context, resultListener);
	}

	@Override
	protected List<Note> doInBackground(String... params) {
		NoteDataSource dataSource = NoteDataSource.getInstance();
		return dataSource.getAllNotes();
	}
}
