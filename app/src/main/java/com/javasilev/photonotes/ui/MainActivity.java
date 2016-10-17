package com.javasilev.photonotes.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.javasilev.photonotes.R;
import com.javasilev.photonotes.async.LoadAsyncTask;
import com.javasilev.photonotes.async.VisionAsyncTask;
import com.javasilev.photonotes.db.NoteDataSource;
import com.javasilev.photonotes.models.Note;
import com.javasilev.photonotes.models.response.TextAnnotation;
import com.javasilev.photonotes.utils.PermissionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoadAsyncTask.ResultListener<List<TextAnnotation>> {
	private NoteDataSource mDataSource;

	public static final String FILE_NAME = "temp.jpg";

	public static final int GALLERY_REQUEST = 1;
	public static final int CAMERA_PERMISSIONS_REQUEST = 2;
	public static final int CAMERA_REQUEST = 3;

	@BindView(R.id.activity_main_button_camera)
	ImageButton mCameraButton;

	@BindView(R.id.activity_main_button_gallery)
	ImageButton mGalleryButton;

	@BindView(R.id.activity_main_progress_bar_loading)
	ProgressBar mLoadingProgressBar;

	@BindView(R.id.activity_main_text_view_progress)
	TextView mProgressTextView;

	@BindView(R.id.activity_main_button_cancel_loading)
	Button mCancelButton;

	@BindView(R.id.activity_main_toolbar)
	Toolbar mActionBar;

	@BindView(R.id.activity_main_linear_layout_main)
	LinearLayout mMainView;

	private VisionAsyncTask mAsyncTask;

	private String mNoteName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		setSupportActionBar(mActionBar);

		mCameraButton.setOnClickListener(this);
		mGalleryButton.setOnClickListener(this);
		mCancelButton.setOnClickListener(this);

		mAsyncTask = (VisionAsyncTask) getLastCustomNonConfigurationInstance();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mDataSource = NoteDataSource.getInstance();
		mDataSource.init();
		if (mAsyncTask != null) {
			mAsyncTask.setResultListener(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.activity_main_menu_all_notes:
				startActivity(new Intent(MainActivity.this, NoteListActivity.class));
				return super.onOptionsItemSelected(item);
			case R.id.activity_main_menu_prefs:
				startActivity(new Intent(MainActivity.this, PrefsActivity.class));
				return super.onOptionsItemSelected(item);
			case R.id.activity_main_menu_about:
				//
				return super.onOptionsItemSelected(item);
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		mDataSource.close();
		if (mAsyncTask != null) {
			mAsyncTask.setResultListener(null);
		}
	}

	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		return mAsyncTask;
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	private File getCameraFile() {
		File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		return new File(dir, FILE_NAME);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.activity_main_button_camera:
				startCamera();
				break;
			case R.id.activity_main_button_gallery:
				startGalleryChooser();
				break;
			case R.id.activity_main_button_cancel_loading:
				mAsyncTask.cancel(true);
				hideLoading();
		}
	}

	public void startGalleryChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select a photo"), GALLERY_REQUEST);
	}

	public void startCamera() {
		if (PermissionUtils.requestPermission(
				this,
				CAMERA_PERMISSIONS_REQUEST,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.CAMERA)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraFile()));
			startActivityForResult(intent, CAMERA_REQUEST);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (PermissionUtils.permissionGranted(
				requestCode,
				CAMERA_PERMISSIONS_REQUEST,
				grantResults)) {
			startCamera();
		}
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
			Uri uri = data.getData();
			Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);

			assert returnCursor != null;
			int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
			returnCursor.moveToFirst();
			mNoteName = returnCursor.getString(nameIndex);

			returnCursor.close();
			restartAsyncTask(uri.toString());
		} else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			mNoteName = "Note" + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			restartAsyncTask(Uri.fromFile(getCameraFile()).toString());
		}
	}

	@Override
	public void setItems(List<TextAnnotation> textAnnotations) {
		TextAnnotation result;
		if (textAnnotations == null || textAnnotations.size() == 0) {
			result = new TextAnnotation("", "", null);
		} else {
			result = textAnnotations.get(0);
		}

		String content = result.getContent();

		if (content.isEmpty()) {
			final Snackbar snackbar = Snackbar.make(mMainView, R.string.nothing_detected, Snackbar.LENGTH_INDEFINITE);
			snackbar.setAction(R.string.ok, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							snackbar.dismiss();
						}
					}).show();
		} else {
			Note note = mDataSource.createOrUpdateNote(0, new Date(), mNoteName, content);

			Intent intent = new Intent(MainActivity.this, NoteActivity.class);
			intent.putExtra(NoteActivity.EXTRA_NOTE_ID, note.getId());
			intent.putExtra(NoteActivity.EXTRA_FROM, NoteActivity.MAIN);
			startActivity(intent);
		}
	}

	@Override
	public void startLoading() {
		toggleEnable(false);
		toggleShow(View.VISIBLE);
	}

	@Override
	public void hideLoading() {
		toggleEnable(true);
		toggleShow(View.GONE);
	}

	@Override
	public void showProgress(String... progress) {
		mProgressTextView.setText(getString(R.string.processing));
	}

	@Override
	public void showError(String error) {
		final Snackbar snackbar = Snackbar.make(mMainView, R.string.error + ": " + error, Snackbar.LENGTH_INDEFINITE);
		snackbar.setAction(R.string.ok, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				snackbar.dismiss();
			}
		}).show();
	}

	private void toggleShow(int visibility) {
		mLoadingProgressBar.setVisibility(visibility);
		mProgressTextView.setVisibility(visibility);
		mCancelButton.setVisibility(visibility);
	}

	private void toggleEnable(boolean enabled) {
		mGalleryButton.setEnabled(enabled);
		mCameraButton.setEnabled(enabled);
	}

	protected void restartAsyncTask(String... params) {
		if (mAsyncTask != null) {
			mAsyncTask.cancel(true);
			mAsyncTask.setResultListener(null);
		}
		mAsyncTask = new VisionAsyncTask(this, this);
		mAsyncTask.execute(params);
	}
}
