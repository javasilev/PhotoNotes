package com.javasilev.photonotes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.javasilev.photonotes.R;
import com.javasilev.photonotes.db.NoteDataSource;
import com.javasilev.photonotes.models.Note;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aleksei Vasilev.
 */

public class NoteActivity extends AppCompatActivity {
	public static final String EXTRA_NOTE_ID = "extra_note_id";
	public static final String EXTRA_FROM = "extra_from";
	public static final String MAIN = "main";

	@BindView(R.id.activity_note_edit_text_name)
	EditText mNameEditText;

	@BindView(R.id.activity_note_edit_text_content)
	EditText mContentEditText;

	private NoteDataSource mDataSource;

	private Note mNote;
	private String mFrom;

	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mDataSource = NoteDataSource.getInstance();
		mDataSource.init();
		setContentView(R.layout.activity_note);
		ButterKnife.bind(this);

		long noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, 0);
		mFrom = getIntent().getExtras().getString(EXTRA_FROM, "");

		if (noteId != 0) {
			mNote = mDataSource.getNote(noteId);

			if (mNote != null) {
				mNameEditText.setText(mNote.getName());
				mContentEditText.setText(mNote.getText());
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		mDataSource.close();
	}

	@Override
	public void onBackPressed() {
		saveOrUpdate();
		if (MAIN.equals(mFrom)) {
			startActivity(new Intent(this, MainActivity.class));
		}
		super.onBackPressed();
	}

	private void saveOrUpdate() {
		if (mNote != null) {
			mDataSource.createOrUpdateNote(mNote.getId(), mNote.getCreationDate(), mNameEditText.getText().toString(), mContentEditText.getText().toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_notes, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.activity_notes_menu_save:
				saveOrUpdate();
				return super.onOptionsItemSelected(item);
			case R.id.activity_notes_menu_delete:
				delete();
				startActivity(new Intent(NoteActivity.this, NoteListActivity.class));
				return super.onOptionsItemSelected(item);
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void delete() {
		if (mNote != null) {
			mDataSource.deleteNote(mNote.getId());
		}
	}
}
