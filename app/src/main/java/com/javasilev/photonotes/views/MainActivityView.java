package com.javasilev.photonotes.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by Aleksei Vasilev.
 */

public interface MainActivityView extends MvpView {

	String REMOVE_PREFIX_TAG = "REMOVE";
	String REMOVE_START_DETECTING_TAG = "REMOVE_START_DETECTING_TAG";
	String REMOVE_NOTE_LIST_TAG = "REMOVE_NOTE_LIST_TAG";
	String REMOVE_PREFS_TAG = "REMOVE_PREFS_TAG";
	String REMOVE_ABOUT_TAG = "REMOVE_ABOUT_TAG";
	String CLOSE_DRAWER_TAG = "CLOSE_DRAWER_TAG";
	String CLOSE_PREFIX_TAG = "CLOSE";

	void start();

	@StateStrategyType(value = NoRepeatLastCommandStrategy.class, tag = REMOVE_START_DETECTING_TAG)
	void openStartDetectingScreen();

	@StateStrategyType(value = NoRepeatLastCommandStrategy.class, tag = REMOVE_NOTE_LIST_TAG)
	void openNoteListScreen();

	@StateStrategyType(value = NoRepeatLastCommandStrategy.class, tag = REMOVE_PREFS_TAG)
	void openPrefsScreen();

	@StateStrategyType(value = NoRepeatLastCommandStrategy.class, tag = REMOVE_ABOUT_TAG)
	void openAboutScreen();

	@StateStrategyType(value = AddToEndSingleStrategy.class, tag = CLOSE_DRAWER_TAG)
	void openDrawer();

	@StateStrategyType(value = RemoveCommandStretegy.class, tag = CLOSE_PREFIX_TAG)
	void closeDrawer();

	@StateStrategyType(value = RemoveCommandStretegy.class, tag = REMOVE_PREFIX_TAG)
	void back();
}
