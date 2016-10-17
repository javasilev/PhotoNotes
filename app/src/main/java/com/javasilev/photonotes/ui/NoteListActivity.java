package com.javasilev.photonotes.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.javasilev.photonotes.R;
import com.javasilev.photonotes.adapters.NoteAdapter;
import com.javasilev.photonotes.adapters.base.CollectionAdapter;
import com.javasilev.photonotes.async.LoadAsyncTask;
import com.javasilev.photonotes.async.NotesAsyncTask;
import com.javasilev.photonotes.db.NoteDataSource;
import com.javasilev.photonotes.models.Note;

/**
 * Created by Aleksei Vasilev.
 */

public class NoteListActivity extends ListActivity<Note> implements CollectionAdapter.OnItemClickListener<Note> {
	private NoteDataSource mDataSource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDataSource = NoteDataSource.getInstance();

		setTitle(getString(R.string.saved_notes));
	}

	private void initList() {
		mAdapter.setCollection(mDataSource.getAllNotes());
	}

	@Override
	protected void onStart() {
		super.onStart();
		mDataSource.init();
		initList();

		mDataRecyclerView.setItemAnimator(new DefaultItemAnimator());

		ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
				mDataSource.deleteNote(mAdapter.getItem(viewHolder.getAdapterPosition()).getId());
				mAdapter.removeItem(viewHolder.getAdapterPosition());
			}
		};

		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
		itemTouchHelper.attachToRecyclerView(mDataRecyclerView);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mDataSource.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_list_notes, menu);
		final MenuItem searchItem = menu.findItem(R.id.activity_list_notes_menu_action_search);
		final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				mAdapter.setCollection(mDataSource.findNotes(query));
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				mAdapter.setCollection(mDataSource.findNotes(newText));
				return false;
			}
		});

		searchView.setOnCloseListener(new SearchView.OnCloseListener() {
			@Override
			public boolean onClose() {
				mAdapter.setCollection(mDataSource.getAllNotes());
				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.activity_list_notes_menu_start:
				startActivity(new Intent(NoteListActivity.this, MainActivity.class));
				return super.onOptionsItemSelected(item);
			case R.id.activity_list_notes_menu_prefs:
				startActivity(new Intent(NoteListActivity.this, PrefsActivity.class));
				return super.onOptionsItemSelected(item);
			case R.id.activity_list_notes_menu_about:
				startActivity(new Intent(NoteListActivity.this, AboutActivity.class));
				return super.onOptionsItemSelected(item);
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected CollectionAdapter<Note> createAdapter() {
		return new NoteAdapter(this, this);
	}

	@Override
	protected LoadAsyncTask<List<Note>> createLoadAsyncTask() {
		return new NotesAsyncTask(this, this);
	}

	@Override
	public void showError(String error) {

	}

	@Override
	public void onItemClick(Note item) {
		Intent intent = new Intent(this, NoteActivity.class);
		intent.putExtra(NoteActivity.EXTRA_NOTE_ID, item.getId());
		startActivity(intent);
	}
}
