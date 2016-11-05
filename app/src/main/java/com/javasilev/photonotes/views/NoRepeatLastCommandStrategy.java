package com.javasilev.photonotes.views;

import java.util.List;

import android.text.TextUtils;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

/**
 * Created by Aleksei Vasilev.
 */

public class NoRepeatLastCommandStrategy implements StateStrategy {
	@Override
	public <View extends MvpView> void beforeApply(List<ViewCommand<View>> list, ViewCommand<View> nextCommand) {
		int size = list.size();
		if (size > 0) {
			if (!TextUtils.equals(list.get(size - 1).getTag(), nextCommand.getTag())) {
				list.add(nextCommand);
			}
		}
	}

	@Override
	public <View extends MvpView> void afterApply(List<ViewCommand<View>> list, ViewCommand<View> viewCommand) {
	}
}
