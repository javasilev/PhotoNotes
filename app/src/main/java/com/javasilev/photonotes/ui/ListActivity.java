package com.javasilev.photonotes.ui;

import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.javasilev.photonotes.R;
import com.javasilev.photonotes.adapters.base.CollectionAdapter;
import com.javasilev.photonotes.async.LoadAsyncTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aleksei Vasilev.
 */

public abstract class ListActivity<Data> extends AppCompatActivity implements LoadAsyncTask.ResultListener<List<Data>> {
	@BindView(R.id.activity_list_progress_bar_loading)
	ProgressBar mLoadingBar;

	@BindView(R.id.activity_list_text_view_progress)
	TextView mProgressTextView;

	@BindView(R.id.activity_list_recycler_view_data)
	RecyclerView mDataRecyclerView;

	@BindView(R.id.activity_main_toolbar)
	Toolbar mActionBar;

	protected CollectionAdapter<Data> mAdapter;

	protected LoadAsyncTask<List<Data>> mLoadAsyncTask;

	protected abstract CollectionAdapter<Data> createAdapter();

	protected abstract LoadAsyncTask<List<Data>> createLoadAsyncTask();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		ButterKnife.bind(this);

		setSupportActionBar(mActionBar);

		mAdapter = createAdapter();
		initRecyclerView();

		//noinspection unchecked
		mLoadAsyncTask = (LoadAsyncTask<List<Data>>) getLastCustomNonConfigurationInstance();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mLoadAsyncTask != null) {
			mLoadAsyncTask.cancel(true);
		}
	}

	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		return mLoadAsyncTask;
	}

	@Override
	public void startLoading() {
		switchVisibility(View.VISIBLE);
	}

	@Override
	public void hideLoading() {
		switchVisibility(View.GONE);
	}

	@Override
	public void showProgress(String... progress) {
		mProgressTextView.setText(progress[0]);
	}

	@Override
	public void setItems(List<Data> data) {
		mAdapter.setCollection(data);
	}

	private void initRecyclerView() {
		mDataRecyclerView.setAdapter(mAdapter);
		mDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
	}

	private void switchVisibility(int visibilityState) {
		mLoadingBar.setVisibility(visibilityState);
		mProgressTextView.setVisibility(visibilityState);
	}

	protected void reuseAsynctask() {
		mLoadAsyncTask.setResultListener(this);
	}

	protected void restartAsyncTask() {
		if (mLoadAsyncTask != null) {
			mLoadAsyncTask.cancel(true);
			mLoadAsyncTask.setResultListener(null);
		}
		mLoadAsyncTask = createLoadAsyncTask();
		mLoadAsyncTask.execute();
	}
}
