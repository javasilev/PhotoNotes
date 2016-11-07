package com.javasilev.photonotes.ui;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.javasilev.photonotes.R;
import com.javasilev.photonotes.presenters.MainActivityPresenter;
import com.javasilev.photonotes.views.MainActivityView;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends MvpAppCompatActivity implements MainActivityView {
	public static final int CONTAINER = R.id.activity_main_frame_layout_container;
	private static final String TAG = "PN_MainActivity";
	private static final String FRAGMENT_TAG = "fragment";
	private static final String SEARCH_QUERY = "search_query";
	private static final String IS_EXPANDED = "is_expanded";

	@BindView(R.id.activity_main_toolbar)
	Toolbar mToolbar;

	@BindView(R.id.activity_main_drawer)
	DrawerLayout mDrawerLayout;

	@BindView(R.id.activity_main_navigation_view)
	NavigationView mNavigationView;

	@InjectPresenter
	MainActivityPresenter mPresenter;

	private SearchView mSearchView;
	private MenuItem mSearchItem;
	private String mQueryString;
	private boolean mIsExpanded;

	private StartDetectingFragment mStartFragment = new StartDetectingFragment();
	private NoteListFragment mNoteListFragment = new NoteListFragment();
	private PrefsFragment mPrefsFragment = new PrefsFragment();
	private AboutFragment mAboutFragment = new AboutFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		if (savedInstanceState != null) {
			mQueryString = savedInstanceState.getString(SEARCH_QUERY);
			mIsExpanded = savedInstanceState.getBoolean(IS_EXPANDED);
		}

		mNoteListFragment.onCreate(null);

		setSupportActionBar(mToolbar);

		final ActionBar ab = getSupportActionBar();
		assert ab != null;
		ab.setHomeAsUpIndicator(R.drawable.ic_menu);
		ab.setDisplayHomeAsUpEnabled(true);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				mPresenter.openDrawer();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				mPresenter.closeDrawer();
			}
		};
		mDrawerLayout.addDrawerListener(toggle);

		mNavigationView.setNavigationItemSelectedListener(mPresenter);

		getSupportFragmentManager().addOnBackStackChangedListener(this::doOnBackStackChanged);

		FragmentManager manager = getSupportFragmentManager();
		for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
			manager.popBackStack();
		}
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

		mSearchItem = menu.findItem(R.id.activity_main_menu_action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);

		mSearchView.setOnSearchClickListener(v -> mPresenter.openNoteListScreen());

		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
				mNoteListFragment.findNotes(query);
			}
		});

		mSearchView.setOnCloseListener(() -> {
			mNoteListFragment.loadNotes();
			return false;
		});

		if (mIsExpanded) {
			mSearchItem.expandActionView();
			if (!TextUtils.isEmpty(mQueryString)) {
				mSearchView.setQuery(mQueryString, true);
				mSearchView.clearFocus();
			}
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(SEARCH_QUERY, mSearchView.getQuery().toString());
		outState.putBoolean(IS_EXPANDED, mSearchItem.isActionViewExpanded());
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mPresenter.closeDrawer();
		} else {
			if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG) instanceof StartDetectingFragment) {
				finish();
			} else {
				mPresenter.back();
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		for (Fragment fragment : fragments) {
			if (fragment != null) {
				fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
			}
		}
	}

	@Override
	public void start() {
		getSupportFragmentManager().beginTransaction()
				.replace(CONTAINER, mStartFragment, FRAGMENT_TAG)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	@Override
	public void openStartDetectingScreen() {
		setFragment(mStartFragment);
	}

	@Override
	public void openNoteListScreen() {
		setFragment(mNoteListFragment);
	}

	@Override
	public void openPrefsScreen() {
		setFragment(mPrefsFragment);
	}

	@Override
	public void openAboutScreen() {
		setFragment(mAboutFragment);
	}

	@Override
	public void openDrawer() {
		mDrawerLayout.openDrawer(GravityCompat.START);
	}

	@Override
	public void closeDrawer() {
		mDrawerLayout.closeDrawers();
	}

	@Override
	public void back() {
		super.onBackPressed();
	}

	private void setFragment(Fragment fragment) {
		Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
		if (!fragment.equals(fragmentByTag)) {
			getSupportFragmentManager().beginTransaction()
					.replace(CONTAINER, fragment, FRAGMENT_TAG)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.addToBackStack(null)
					.commit();
		}
	}

	private void doOnBackStackChanged() {
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
		if (fragment instanceof StartDetectingFragment) {
			mNavigationView.setCheckedItem(R.id.nav_start_detecting);
		} else if (fragment instanceof NoteListFragment) {
			mNavigationView.setCheckedItem(R.id.nav_note_list);
		} else if (fragment instanceof PrefsFragment) {
			mNavigationView.setCheckedItem(R.id.nav_note_prefs);
		} else {
			mNavigationView.setCheckedItem(R.id.nav_about);
		}
	}
}
