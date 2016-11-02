package com.javasilev.photonotes.utils;

import java.security.SecureRandom;

/**
 * Created by Aleksei Vasilev.
 */

public class KeyManager {
	private static String[] keys = {
			"AIzaSyBoMQKxDuEwp5R-8iZR9Wr0M7jBRvJqis4",
	        "AIzaSyBLJlabnXgUMhUo41DuDiOO2ZmHKX09BIE",
	        "AIzaSyBgZnRMN8fgpIdiQR-L29Mpmnf9jsAeKjA"
	};
	public static String getKey() {
		return keys[new SecureRandom().nextInt(keys.length)];
	}
}
