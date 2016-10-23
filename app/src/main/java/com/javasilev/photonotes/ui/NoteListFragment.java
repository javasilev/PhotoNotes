package com.javasilev.photonotes.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.javasilev.photonotes.R;
import com.javasilev.photonotes.adapters.NoteAdapter;
import com.javasilev.photonotes.adapters.base.CollectionAdapter;
import com.javasilev.photonotes.models.Note;
import com.javasilev.photonotes.presenters.NoteListPresenter;
import com.javasilev.photonotes.views.NoteListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aleksei Vasilev.
 */

public class NoteListFragment extends MvpAppCompatFragment implements NoteListView, CollectionAdapter.OnItemClickListener<Note> {
	public static final String NOTE_LIST_PRESENTER_TAG = "NoteListPresenterTag";

	@BindView(R.id.fragment_list_progress_bar_loading)
	ProgressBar mLoadingBar;

	@BindView(R.id.fragment_list_text_view_progress)
	TextView mProgressTextView;

	@BindView(R.id.fragment_list_recycler_view_data)
	RecyclerView mDataRecyclerView;

	@BindView(R.id.fragment_list_text_view_empty)
	TextView mEmptyText;

	@InjectPresenter(type = PresenterType.GLOBAL, tag = NOTE_LIST_PRESENTER_TAG)
	NoteListPresenter mPresenter;

	private NoteAdapter mAdapter;

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
	}

	@Override
	public void onStart() {
		super.onStart();
		startLoading();
	}

	private void startLoading() {
		mPresenter.loadNotes();
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
		mPresenter.userDeleteNote(itemId);
		setEmptyState(mAdapter.getItemCount() == 0);
	}

	@Override
	public void showProgress() {
		mLoadingBar.setVisibility(View.VISIBLE);
		mProgressTextView.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		mLoadingBar.setVisibility(View.GONE);
		mProgressTextView.setVisibility(View.GONE);
	}

	@Override
	public void disableList() {
		mDataRecyclerView.setEnabled(false);
	}

	@Override
	public void enableList() {
		mDataRecyclerView.setEnabled(true);
	}

	@Override
	public void setList(List<Note> noteList) {
		mAdapter.setCollection(noteList);
		setEmptyState(noteList.size() == 0);
	}

	@Override
	public void showError(String message) {

	}

	private void setEmptyState(boolean empty) {
		mEmptyText.setVisibility(empty ? View.VISIBLE : View.GONE);
	}

	private void initRecyclerView() {
		mDataRecyclerView.setAdapter(mAdapter);
		mDataRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
	}

	public NoteListPresenter getPresenter() {
		return mPresenter;
	}
}
