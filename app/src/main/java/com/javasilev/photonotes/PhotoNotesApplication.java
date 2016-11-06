package com.javasilev.photonotes;

import com.arellomobile.mvp.MvpApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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

		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
						.setDefaultFontPath("fonts/Roboto-Light.ttf")
						.setFontAttrId(R.attr.fontPath)
						.build()
		                             );
	}
}
