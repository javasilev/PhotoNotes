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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.javasilev.photonotes.R;
import com.javasilev.photonotes.models.Note;
import com.javasilev.photonotes.models.response.TextAnnotation;
import com.javasilev.photonotes.presenters.NoteListPresenter;
import com.javasilev.photonotes.presenters.NotePresenter;
import com.javasilev.photonotes.presenters.VisionPresenter;
import com.javasilev.photonotes.utils.PermissionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Observer<List<TextAnnotation>> {
	public static final String MAIN_FRAGMENT_TAG = "main";

	public static final String FILE_NAME = "temp.jpg";

	public static final int GALLERY_REQUEST = 1;
	public static final int CAMERA_PERMISSIONS_REQUEST = 2;
	public static final int CAMERA_REQUEST = 3;
	public static final int CONTAINER = R.id.activity_main_frame_layout_container;

	@BindView(R.id.activity_main_progress_bar_loading)
	ProgressBar mLoadingProgressBar;

	@BindView(R.id.activity_main_text_view_progress)
	TextView mProgressTextView;

	@BindView(R.id.activity_main_button_cancel_loading)
	Button mCancelButton;

	@BindView(R.id.activity_main_toolbar)
	Toolbar mActionBar;

	@BindView(CONTAINER)
	FrameLayout mContainerFrameLayout;

	private VisionPresenter mVisionPresenter;
	private NotePresenter mNotePresenter;
	private Observer<Boolean> mBooleanObserver;

	private String mNoteName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		setSupportActionBar(mActionBar);

		getSupportFragmentManager()
				.beginTransaction()
				.replace(CONTAINER, new MainFragment(), MAIN_FRAGMENT_TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commitNow();

		//noinspection unchecked
		mBooleanObserver = (Observer<Boolean>) getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);

		mNotePresenter = new NotePresenter();

		mVisionPresenter = (VisionPresenter) getLastCustomNonConfigurationInstance();
		if (mVisionPresenter != null && mVisionPresenter.isInProcess()) {
			startLoading();
		} else {
			mVisionPresenter = new VisionPresenter(this, this);
		}

		mCancelButton.setOnClickListener(this);
	}

	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		return mVisionPresenter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

		final MenuItem searchItem = menu.findItem(R.id.activity_main_menu_action_search);
		final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		NoteListFragment fragment = new NoteListFragment();
		NoteListPresenter presenter = new NoteListPresenter(fragment);

		searchView.setOnSearchClickListener(v -> getSupportFragmentManager()
				.beginTransaction()
				.replace(CONTAINER, fragment)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.addToBackStack(null)
				.commit());

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				find(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				find(newText);
				return false;
			}

			private void find(String query) {
				presenter.findNotes(query);
			}
		});

		searchView.setOnCloseListener(() -> {
			presenter.loadNotes();
			return false;
		});

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.activity_main_menu_all_notes:
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.activity_main_frame_layout_container, new NoteListFragment())
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.addToBackStack(null)
						.commit();
				return super.onOptionsItemSelected(item);
			case R.id.activity_main_menu_prefs:
				startActivity(new Intent(MainActivity.this, PrefsActivity.class));
				return super.onOptionsItemSelected(item);
			case R.id.activity_main_menu_about:
				startActivity(new Intent(MainActivity.this, AboutActivity.class));
				return super.onOptionsItemSelected(item);
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		getSupportFragmentManager().popBackStack();
	}

	private File getCameraFile() {
		File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		return new File(dir, FILE_NAME);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.fragment_main_button_camera:
				startCamera();
				break;
			case R.id.fragment_main_button_gallery:
				startGalleryChooser();
				break;
			case R.id.activity_main_button_cancel_loading:
				mVisionPresenter.stop();
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
			mVisionPresenter.load(uri);
			startLoading();
		} else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			mNoteName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
			mVisionPresenter.load(Uri.fromFile(getCameraFile()));
			startLoading();
		}
	}

	public void startLoading() {
		toggleEnable(false);
		toggleShow(View.VISIBLE);
	}

	private void hideLoading() {
		toggleEnable(true);
		toggleShow(View.GONE);
	}

	private void toggleShow(int visibility) {
		mLoadingProgressBar.setVisibility(visibility);
		mProgressTextView.setVisibility(visibility);
		mCancelButton.setVisibility(visibility);
	}

	private void toggleEnable(boolean enabled) {
		Observable.create(new Observable.OnSubscribe<Boolean>() {
			@Override
			public void call(Subscriber<? super Boolean> subscriber) {
				subscriber.onNext(enabled);
			}
		})
		.subscribeOn(Schedulers.newThread())
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(mBooleanObserver);
	}

	@Override
	public void onCompleted() {
		hideLoading();
	}

	@Override
	public void onError(Throwable e) {
		new AlertDialog.Builder(this)
				.setCancelable(true)
				.setTitle(getString(R.string.error))
				.setMessage(e.getMessage())
				.setPositiveButton(getString(R.string.ok), (dialog, which) -> dialog.dismiss())
				.show();
	}

	@Override
	public void onNext(List<TextAnnotation> textAnnotations) {
		if (textAnnotations != null && textAnnotations.size() != 0) {
			TextAnnotation result = textAnnotations.get(0);
			String content = result.getContent();

			Note note = mNotePresenter.createOrUpdate(new Note(0, new Date(), mNoteName, content));

			Intent intent = new Intent(MainActivity.this, NoteActivity.class);
			intent.putExtra(NoteActivity.EXTRA_NOTE_ID, note.getId());
			intent.putExtra(NoteActivity.EXTRA_FROM, NoteActivity.MAIN);
			startActivity(intent);
		}
	}
}
