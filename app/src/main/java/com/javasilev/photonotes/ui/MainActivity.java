package com.javasilev.photonotes.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.javasilev.photonotes.R;
import com.javasilev.photonotes.adapters.MainPagerAdapter;
import com.javasilev.photonotes.models.Note;
import com.javasilev.photonotes.presenters.NoteListPresenter;
import com.javasilev.photonotes.views.NoteListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends MvpAppCompatActivity implements NoteListView {
	public static final int CONTAINER = R.id.activity_main_viewpager;

	public static final int NOTES_POSITION = 1;

	@BindView(R.id.activity_main_toolbar)
	Toolbar mToolbar;

	@BindView(CONTAINER)
	ViewPager mViewPager;

	@BindView(R.id.activity_main_tabs)
	TabLayout mTabLayout;

	@InjectPresenter(type = PresenterType.GLOBAL, tag = NoteListFragment.NOTE_LIST_PRESENTER_TAG)
	NoteListPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		setSupportActionBar(mToolbar);

		setupViewPager(mViewPager);
		mTabLayout.setupWithViewPager(mViewPager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

		final MenuItem searchItem = menu.findItem(R.id.activity_main_menu_action_search);
		final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		searchView.setOnSearchClickListener(v -> mViewPager.setCurrentItem(NOTES_POSITION));

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
				mPresenter.findNotes(query);
			}
		});

		searchView.setOnCloseListener(() -> {
			mPresenter.loadNotes();
			return false;
		});

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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

	private void setupViewPager(ViewPager viewPager) {
		MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new StartDetectingFragment(), getString(R.string.Start_OCR));
		adapter.addFragment(new NoteListFragment(), getString(R.string.all_notes));
		viewPager.setAdapter(adapter);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void showProgress() {

	}

	@Override
	public void hideProgress() {

	}

	@Override
	public void disableList() {

	}

	@Override
	public void enableList() {

	}

	@Override
	public void setList(List<Note> noteList) {

	}

	@Override
	public void showError(String message) {

	}
}
