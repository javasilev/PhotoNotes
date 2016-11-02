package com.javasilev.photonotes.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by Aleksei Vasilev.
 */


public interface MainActivityView extends MvpView {

	String REMOVE_COMMAND_TAG = "REMOVE_COMMAND_TAG";

	void start();

	void setStartDetectingFragment();

	void setNoteListFragment();

	void setPrefsFragment();

	void setAboutFragment();

	@StateStrategyType(value = AddToEndSingleStrategy.class, tag = REMOVE_COMMAND_TAG)
	void openDrawer();

	@StateStrategyType(value = RemoveCommandStretegy.class, tag = REMOVE_COMMAND_TAG)
	void closeDrawer();

	void back();
}
