package com.javasilev.photonotes;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Aleksei Vasilev.
 */

public class PhotoNotes extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// Initialize Realm. Should only be done once when the application starts.
		Realm.init(this);
		RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
		Realm.setDefaultConfiguration(realmConfiguration);
	}
}
