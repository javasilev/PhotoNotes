package com.javasilev.photonotes.views;

import java.util.List;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.javasilev.photonotes.models.Note;

/**
 * Created by Aleksei Vasilev.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface NoteListView extends MvpView {
	void showProgress();
	void hideProgress();

	void disableList();
	void enableList();

	void setList(List<Note> noteList);

	@StateStrategyType(SkipStrategy.class)
	void showError(String message);
}
