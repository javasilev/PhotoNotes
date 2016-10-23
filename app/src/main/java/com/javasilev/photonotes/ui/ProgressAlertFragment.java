package com.javasilev.photonotes.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.javasilev.photonotes.R;
import com.javasilev.photonotes.views.StartDetectingView;

/**
 * Created by Aleksei Vasilev.
 */

public class ProgressAlertFragment extends DialogFragment {

	private static StartDetectingView mCaller;

	public static ProgressAlertFragment newInstance(StartDetectingView caller) {
		mCaller = caller;
		return new ProgressAlertFragment();
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setCancelable(false);
		return new AlertDialog.Builder(getContext())
				.setView(R.layout.fragment_progress_alert)
				.setPositiveButton(getString(R.string.cancel), (dialog, which) -> mCaller.onCancelClick())
				.create();
	}

	@Override
	public void show(FragmentManager manager, String tag) {
		if (StartDetectingFragment.ALERT_TAG.equals(tag)) {
			if (manager.findFragmentByTag(tag) == null) {
				super.show(manager, tag);
			}
		} else {
			super.show(manager, tag);
		}
	}
}
