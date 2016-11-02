package com.javasilev.photonotes.ui;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.javasilev.photonotes.R;

/**
 * Created by Aleksei Vasilev.
 */

public class PrefsFragment extends PreferenceFragmentCompat {
	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.settings, rootKey);
	}
}
