package com.javasilev.photonotes.presenters;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.javasilev.photonotes.R;
import com.javasilev.photonotes.data.RealmDataSource;
import com.javasilev.photonotes.models.Note;
import com.javasilev.photonotes.models.response.Error;
import com.javasilev.photonotes.models.response.Response;
import com.javasilev.photonotes.models.response.TextAnnotation;
import com.javasilev.photonotes.views.StartDetectingView;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;

/**
 * Created by Aleksei Vasilev.
 */
@InjectViewState
public class StartDetectingPresenter extends MvpPresenter<StartDetectingView> implements Observer<List<Response>> {
	public static final int CAMERA = R.id.fragment_start_detecting_button_camera;
	private static final int GALLERY = R.id.fragment_start_detecting_button_gallery;

	private String mItemName;
	private Context mContext;
	private VisionController mVisionController;
	private RealmDataSource mDataSource = RealmDataSource.getInstance();

	public void userClickControl(int controlId) {
		switch (controlId) {
			case CAMERA:
				getViewState().startCamera();
				break;
			case GALLERY:
				getViewState().startGallery();
				break;
		}
	}

	public void startDetecting(Context context, Uri uri, String itemName) {
		getViewState().showProgress();

		mItemName = itemName;
		mVisionController = new VisionController(this, mContext = context);
		mVisionController.load(uri);
	}

	public void userCancelDetecting() {
		mVisionController.stop();
		getViewState().hideProgress();
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		getViewState().hideProgress();
		if (e instanceof UnknownHostException) {
			getViewState().showError(mContext.getString(R.string.check_inet));
		} else if (e instanceof HttpException){
			int errorCode = ((HttpException) e).code();
			if (errorCode == 400 || errorCode == 403) {
				getViewState().showError(mContext.getString(R.string.wrong_api));
			}
		} else {
			getViewState().showError(e.getMessage());
		}
	}

	@Override
	public void onNext(List<Response> responses) {
		getViewState().hideProgress();
		if (responses != null && responses.size() > 0) {
			Response response = responses.get(0);
			Error error = response.getError();
			if (error != null) {
				onError(new Exception(error.getMessage()));
				return;
			}

			List<TextAnnotation> textAnnotations = response.getTextAnnotations();

			if (textAnnotations != null && textAnnotations.size() > 0) {
				TextAnnotation result = textAnnotations.get(0);
				String content = result.getContent();

				Note note = mDataSource.createOrUpdate(new Note(0, new Date(), mItemName, content));

				getViewState().processResult(note.getId());
			} else {
				getViewState().showError(mContext.getString(R.string.nothing_detected));
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mVisionController != null) {
			mVisionController.stop();
		}
	}
}
