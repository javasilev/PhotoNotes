package com.javasilev.photonotes.views;

import java.util.Iterator;
import java.util.List;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

/**
 * Created by Aleksei Vasilev.
 */

@SuppressWarnings("WeakerAccess")
public class RemoveCommandStretegy implements StateStrategy {
	@Override
	public <View extends MvpView> void beforeApply(List<ViewCommand<View>> list, ViewCommand<View> nextCommand) {
		Iterator<ViewCommand<View>> iterator = list.iterator();
		while (iterator.hasNext()) {
			ViewCommand<View> command = iterator.next();
			if (command.getTag().contains(nextCommand.getTag())) {
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
