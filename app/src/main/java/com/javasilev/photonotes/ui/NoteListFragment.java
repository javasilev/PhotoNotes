package com.javasilev.photonotes.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.javasilev.photonotes.R;
import com.javasilev.photonotes.adapters.NoteAdapter;
import com.javasilev.photonotes.adapters.base.CollectionAdapter;
import com.javasilev.photonotes.models.Note;
import com.javasilev.photonotes.presenters.NoteListPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * Created by Aleksei Vasilev.
 */

public class NoteListFragment extends Fragment implements Observer<List<Note>>, CollectionAdapter.OnItemClickListener<Note> {
	@BindView(R.id.fragment_list_progress_bar_loading)
	ProgressBar mLoadingBar;

	@BindView(R.id.fragment_list_text_view_progress)
	TextView mProgressTextView;

	@BindView(R.id.fragment_list_recycler_view_data)
	RecyclerView mDataRecyclerView;

	private NoteAdapter mAdapter;
	private NoteListPresenter mPresenter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);

		mAdapter = new NoteAdapter(getContext(), this);
		initRecyclerView();

		mPresenter = new NoteListPresenter(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		startLoading();
	}

	private void startLoading() {
		mPresenter.loadNotes();
		switchVisibility(View.VISIBLE);
	}

	@Override
	public void onItemClick(Note item) {
		Intent intent = new Intent(getContext(), NoteActivity.class);
		intent.putExtra(NoteActivity.EXTRA_NOTE_ID, item.getId());
		startActivity(intent);
	}

	@Override
	public void onDelete(int position, long itemId) {
		mAdapter.removeItem(position);
		mPresenter.deleteNote(itemId);
	}

	@Override
	public void onCompleted() {
		switchVisibility(View.GONE);
	}

	@Override
	public void onError(Throwable e) {

	}

	@Override
	public void onNext(List<Note> notes) {
		mAdapter.setCollection(notes);
	}

	private void initRecyclerView() {
		mDataRecyclerView.setAdapter(mAdapter);
		mDataRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
	}

	private void switchVisibility(int visibilityState) {
		mLoadingBar.setVisibility(visibilityState);
		mProgressTextView.setVisibility(visibilityState);
	}
}
