package com.javasilev.photonotes.presenters;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.javasilev.photonotes.R;
import com.javasilev.photonotes.views.MainActivityView;

/**
 * Created by Aleksei Vasilev.
 */

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> implements NavigationView.OnNavigationItemSelectedListener {
	private static final String TAG = "PN_MainPresenter";

	@Override
	protected void onFirstViewAttach() {
		super.onFirstViewAttach();
		start();
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.nav_start_detecting:
				getViewState().closeDrawer();
				getViewState().openStartDetectingScreen();
				return true;
			case R.id.nav_note_list:
				getViewState().closeDrawer();
				openNoteListScreen();
				return true;
			case R.id.nav_note_prefs:
				getViewState().closeDrawer();
				getViewState().openPrefsScreen();
				return true;
			case R.id.nav_about:
				getViewState().closeDrawer();
				getViewState().openAboutScreen();
				return true;
		}

		return false;
	}

	public void openDrawer() {
		getViewState().openDrawer();
	}

	public void closeDrawer() {
		getViewState().closeDrawer();
	}

	public void back() {
		getViewState().back();
	}

	public void openNoteListScreen() {
		getViewState().openNoteListScreen();
	}

	private void start() {
		getViewState().start();
	}
}
