package com.javasilev.photonotes.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.javasilev.photonotes.R;
import com.javasilev.photonotes.presenters.StartDetectingPresenter;
import com.javasilev.photonotes.utils.FileNameHelper;
import com.javasilev.photonotes.views.StartDetectingView;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;


import static android.app.Activity.RESULT_OK;
import static com.javasilev.photonotes.R.string.error;

/**
 * Created by Aleksei Vasilev.
 */

public class StartDetectingFragment extends MvpAppCompatFragment implements StartDetectingView {
	public static final String FILE_NAME = "temp.jpg";
	public static final String PRESENTER_TAG = "presenter_tag";

	public static final int GALLERY_REQUEST = 1;
	public static final int CAMERA_REQUEST = 2;

	@InjectPresenter(type = PresenterType.GLOBAL, tag = PRESENTER_TAG)
	StartDetectingPresenter mStartDetectingPresenter;

	@BindView(R.id.fragment_start_detecting_button_camera)
	Button mCameraButton;

	@BindView(R.id.fragment_start_detecting_button_gallery)
	Button mGalleryButton;

	private AlertDialog mProgressDialog;
	private AlertDialog mErrorDialog;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_start_detecting, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);

		mCameraButton.setOnClickListener(v -> mStartDetectingPresenter.userClickControl(v.getId()));
		mGalleryButton.setOnClickListener(v -> mStartDetectingPresenter.userClickControl(v.getId()));

		mProgressDialog = new AlertDialog.Builder(getContext())
				.setCancelable(false)
				.setView(R.layout.fragment_progress_alert)
				.setPositiveButton(getString(R.string.cancel), (dialog, which) -> mStartDetectingPresenter.userCancelDetecting())
				.create();

		mErrorDialog = new AlertDialog.Builder(getContext())
				.setCancelable(true)
				.setTitle(getString(error))
				.setPositiveButton(getString(R.string.ok), (dialog, which) -> dialog.dismiss())
				.create();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}

		if (mErrorDialog != null && mErrorDialog.isShowing()) {
			mErrorDialog.dismiss();
			mErrorDialog = null;
		}
	}

	@Override
	public void startCamera() {
		RxPermissions.getInstance(getContext())
				.request(Manifest.permission.CAMERA,
						Manifest.permission.READ_EXTERNAL_STORAGE,
						Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.subscribe(granted -> {
					if (granted) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
									getContext(), getContext().getApplicationContext().getPackageName() + ".provider", getCameraFile()
							                                                                   ));
						} else {
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraFile()));
						}
						startActivityForResult(intent, CAMERA_REQUEST);
					} else {
						showError(getString(R.string.permissions_are_required));
					}
				});
	}

	@Override
	public void startGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, getString(R.string.select)), GALLERY_REQUEST);
	}

	@Override
	public void showProgress() {
		mProgressDialog.show();
	}

	@Override
	public void hideProgress() {
		if (mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void showError(String errorMessage) {
		mErrorDialog.setMessage(errorMessage);
		mErrorDialog.show();
	}

	@Override
	public void processResult(long itemId) {
		Intent intent = new Intent(getContext(), NoteActivity.class);
		intent.putExtra(NoteActivity.EXTRA_NOTE_ID, itemId);
		startActivity(intent);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		String noteName;
		if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
			Uri uri = data.getData();
			noteName = FileNameHelper.getFileNameFromUri(uri, getContext());
			mStartDetectingPresenter.startDetecting(getContext(), uri, noteName);
		} else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			noteName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
			mStartDetectingPresenter.startDetecting(getContext(), Uri.fromFile(getCameraFile()), noteName);
		}
	}

	private File getCameraFile() {
		File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		return new File(dir, FILE_NAME);
	}
}
