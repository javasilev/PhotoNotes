package com.javasilev.photonotes;

import com.arellomobile.mvp.MvpApplication;
import com.javasilev.photonotes.utils.FontsOverride;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Aleksei Vasilev.
 */

public class PhotoNotesApplication extends MvpApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		// Initialize Realm. Should only be done once when the application starts.
		Realm.init(this);
		RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
		Realm.setDefaultConfiguration(realmConfiguration);

		FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Roboto-Light.ttf");
		FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Roboto-Light.ttf");
	}
}
