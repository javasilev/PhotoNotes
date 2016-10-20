package com.javasilev.photonotes.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.javasilev.photonotes.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aleksei Vasilev.
 */

public class NoteActivity extends AppCompatActivity {
	public static final String EXTRA_NOTE_ID = "extra_note_id";

	private static final int CONTAINER = R.id.activity_note_frame_layout_note_container;

	@BindView(R.id.activity_main_toolbar)
	Toolbar mActionBar;

	private long mNoteId;
	private NoteFragment mFragment;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		ButterKnife.bind(this);

		setSupportActionBar(mActionBar);
		setTitle(getString(R.string.edit_note));
	}

	@Override
	protected void onStart() {
		super.onStart();

		mNoteId = getIntent().getLongExtra(EXTRA_NOTE_ID, 0);

		mFragment = NoteFragment.newInstance(mNoteId);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(CONTAINER, mFragment)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
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
				mFragment.saveNote();
				return super.onOptionsItemSelected(item);
			case R.id.activity_notes_menu_delete:
				delete();
				onBackPressed();
				return super.onOptionsItemSelected(item);
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void delete() {
		mFragment.deleteNote(mNoteId);
	}
}
