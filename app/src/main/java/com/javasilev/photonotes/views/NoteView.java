package com.javasilev.photonotes.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.javasilev.photonotes.models.Note;

/**
 * Created by Aleksei Vasilev.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface NoteView extends MvpView {
	void setNote(Note note);
}
