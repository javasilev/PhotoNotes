package com.javasilev.photonotes.utils;

import java.security.SecureRandom;

/**
 * Created by Aleksei Vasilev.
 */

public class KeyManager {
	private static String[] keys = {
			"AIzaSyBoNQKxDuEwp5R-8iZR9MrOW7jBRvJqis4",
			"AIzaSyBLJlabnXjUMhUo41DuDi0O2ZmHKX09BIE",
			"AIzaSyCKVkZmbQldKrRy5fGAVAwyfGdWJuxZI0A"
	};
	public static String getKey() {
		return keys[new SecureRandom().nextInt(keys.length)];
	}
}
