package com.javasilev.photonotes.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.javasilev.photonotes.R;

/**
 * Created by Aleksei Vasilev.
 */

public class PrefsActivity extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
}
