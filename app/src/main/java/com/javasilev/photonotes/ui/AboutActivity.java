package com.javasilev.photonotes.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.javasilev.photonotes.R;

/**
 * Created by Aleksei Vasilev.
 */

public class AboutActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setSupportActionBar((Toolbar) findViewById(R.id.activity_main_toolbar));
		setTitle(R.string.about);
	}
}
