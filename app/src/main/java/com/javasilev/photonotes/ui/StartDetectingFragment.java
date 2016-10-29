package com.javasilev.photonotes.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.javasilev.photonotes.R;
import com.javasilev.photonotes.presenters.StartDetectingPresenter;
import com.javasilev.photonotes.utils.PermissionUtils;
import com.javasilev.photonotes.views.StartDetectingView;

import butterknife.BindView;
import butterknife.ButterKnife;


import static android.app.Activity.RESULT_OK;

/**
 * Created by Aleksei Vasilev.
 */

public class StartDetectingFragment extends MvpAppCompatFragment implements StartDetectingView {
	public static final String FILE_NAME = "temp.jpg";
	public static final String ALERT_TAG = "alert_tag";

	public static final int GALLERY_REQUEST = 1;
	public static final int CAMERA_PERMISSIONS_REQUEST = 2;
	public static final int CAMERA_REQUEST = 3;

	@InjectPresenter
	StartDetectingPresenter mStartDetectingPresenter;

	@BindView(R.id.fragment_start_detecting_button_camera)
	Button mCameraButton;

	@BindView(R.id.fragment_start_detecting_button_gallery)
	Button mGalleryButton;

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
	}

	@Override
	public void startCamera() {
		if (PermissionUtils.requestPermission(
				getActivity(),
				CAMERA_PERMISSIONS_REQUEST,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.CAMERA)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", getCameraFile()));
			} else {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraFile()));
			}
			startActivityForResult(intent, CAMERA_REQUEST);
		}
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
		ProgressAlertFragment alertFragment = ProgressAlertFragment.newInstance(this);
		alertFragment.show(getFragmentManager(), ALERT_TAG);
	}

	@Override
	public void hideProgress() {
		DialogFragment fragment = (DialogFragment) getFragmentManager().findFragmentByTag(ALERT_TAG);
		if (fragment != null) {
			fragment.dismiss();
		}
	}

	@Override
	public void showError(String errorMessage) {
		new AlertDialog.Builder(getContext())
				.setCancelable(true)
				.setTitle(getString(R.string.error))
				.setMessage(errorMessage)
				.setPositiveButton(getString(R.string.ok), (dialog, which) -> dialog.dismiss())
				.show();
	}

	@Override
	public void processResult(long itemId) {
		Intent intent = new Intent(getContext(), NoteActivity.class);
		intent.putExtra(NoteActivity.EXTRA_NOTE_ID, itemId);
		startActivity(intent);
	}

	@Override
	public void onCancelClick() {
		mStartDetectingPresenter.userCancelDetecting();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (PermissionUtils.permissionGranted(
				requestCode,
				CAMERA_PERMISSIONS_REQUEST,
				grantResults)) {
			mStartDetectingPresenter.userClickControl(StartDetectingPresenter.CAMERA);
		}
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		String noteName;

		if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
			Uri uri = data.getData();
			Cursor returnCursor = getActivity().getContentResolver().query(uri, null, null, null, null);

			if (returnCursor != null) {
				int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
				returnCursor.moveToFirst();
				noteName = returnCursor.getString(nameIndex);
				returnCursor.close();
			} else {
				noteName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
			}

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
