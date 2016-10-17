package com.javasilev.photonotes.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.javasilev.photonotes.R;

/**
 * Created by Aleksei Vasilev.
 */

public class AboutActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);

	}

	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.activity_about);

		setSupportActionBar((Toolbar) findViewById(R.id.activity_main_toolbar));

		setTitle(R.string.about);
	}
}
