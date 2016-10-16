package com.javasilev.photonotes.async;

import android.content.Context;
import android.os.AsyncTask;

import com.javasilev.photonotes.R;

/**
 * Created by Aleksei Vasilev.
 */

@SuppressWarnings({"unchecked", "FieldCanBeLocal", "WeakerAccess"})
public abstract class LoadAsyncTask<Data> extends AsyncTask<String, String, Data> {
	protected ResultListener mResultListener;
	protected Context mContext;
	protected Data mResultList;
	protected Exception mException;
	protected Status mStatus = Status.NOT_STARTED;
	protected String mCurrentProgress = "";

	public LoadAsyncTask(Context context, ResultListener<Data> resultListener) {
		mContext = context;
		mResultListener = resultListener;

		switchStatus();
	}

	private void switchStatus() {
		switch (mStatus) {
			case PROGRESS:
				mResultListener.startLoading();
				mResultListener.showProgress(mCurrentProgress);
				break;
			case COMPLETE:
				mResultListener.setItems(mResultList);
				break;
			case CANCELLED:
			case ERROR:
				mResultListener.showError(mException.getMessage());
				break;
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mResultListener != null) {
			mResultListener.startLoading();
			mResultListener.showProgress(mContext.getString(R.string.processing));
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		if (mResultListener != null) {
			mResultListener.showProgress(values);
		}
	}

	@Override
	protected void onPostExecute(Data data) {
		super.onPostExecute(data);

		mResultList = data;

		if (mResultListener != null) {
			finishLoading(data);

			if (mException != null) {
				mResultListener.showError(mException.getMessage());
			}
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		mStatus = Status.CANCELLED;
	}

	private void finishLoading(Data data) {
		assert mResultListener != null;
		mResultListener.hideLoading();
		if (!isCancelled()) {
			mResultListener.setItems(data);
		}
	}

	public void setResultListener(ResultListener resultListener) {
		mResultListener = resultListener;
		if (mResultListener != null) {
			switchStatus();
		}
	}

	public interface ResultListener<Data> {
		void startLoading();

		void hideLoading();

		void showProgress(String... progress);

		void showError(String error);

		void setItems(Data data);
	}

	@SuppressWarnings("WeakerAccess")
	public enum Status {
		NOT_STARTED,
		PROGRESS,
		CANCELLED,
		COMPLETE,
		ERROR
	}
}
