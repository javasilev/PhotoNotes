package com.javasilev.photonotes.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.javasilev.photonotes.R;
import com.javasilev.photonotes.models.Note;
import com.javasilev.photonotes.presenters.NotePresenter;
import com.javasilev.photonotes.views.NoteView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aleksei Vasilev.
 */

public class NoteFragment extends MvpAppCompatFragment implements NoteView {
	public static final String ARG_ID = "id";

	@BindView(R.id.fragment_note_edit_text_name)
	EditText mNameEditText;

	@BindView(R.id.fragment_note_edit_text_content)
	EditText mContentEditText;

	@InjectPresenter
	NotePresenter mPresenter;

	private long mId;
	private Note mNote;

	public static NoteFragment newInstance(long id) {
		Bundle args = new Bundle();
		args.putLong(ARG_ID, id);

		NoteFragment fragment = new NoteFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		if (args != null) {
			mId = args.getLong(ARG_ID);
			mPresenter.getNote(mId);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_note, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
	}

	@Override
	public void onPause() {
		super.onPause();
		saveNote();
	}

	public String getName() {
		return mNote.getName();
	}

	public String getText() {
		return mNote.getText();
	}

	public void saveNote() {
		if (mNote != null) {
			Note note = new Note(
					mNote.getId(),
					mNote.getCreationDate(),
					mNameEditText.getText().toString(),
					mContentEditText.getText().toString()
			);
			mPresenter.createOrUpdate(note);
		}
	}

	public void deleteNote(long id) {
		mNote = null;
		mPresenter.deleteNote(id);
	}

	@Override
	public void setNote(Note note) {
		mNote = note;
		mNameEditText.setText(mNote.getName());
		mContentEditText.setText(mNote.getText());
	}
}
