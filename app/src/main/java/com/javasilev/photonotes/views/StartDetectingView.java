package com.javasilev.photonotes.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by Aleksei Vasilev.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface StartDetectingView extends MvpView {

	@StateStrategyType(SkipStrategy.class)
	void startCamera();
	@StateStrategyType(SkipStrategy.class)
	void startGallery();

	void showProgress();
	void hideProgress();

	void showError(String errorMessage);
	void hideError();

//	@StateStrategyType(SkipStrategy.class)
	void processResult(long itemId);

	void onCancelClick();
}
