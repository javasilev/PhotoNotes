package com.javasilev.photonotes.views;

import java.util.Iterator;
import java.util.List;

import android.text.TextUtils;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

/**
 * Created by Aleksei Vasilev.
 */

class RemoveCommandStretegy implements StateStrategy {
	@Override
	public <View extends MvpView> void beforeApply(List<ViewCommand<View>> list, ViewCommand<View> nextCommand) {
		Iterator<ViewCommand<View>> iterator = list.iterator();
		while (iterator.hasNext()) {
			ViewCommand<View> command = iterator.next();
			if (TextUtils.equals(command.getTag(), nextCommand.getTag())) {
				iterator.remove();
			}
		}
		list.add(nextCommand);
	}

	@Override
	public <View extends MvpView> void afterApply(List<ViewCommand<View>> list, ViewCommand<View> viewCommand) {
		list.remove(viewCommand);
	}
}
