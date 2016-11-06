package com.javasilev.photonotes.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

/**
 * Created by Aleksei Vasilev.
 */

public class FileNameHelper {
	@SuppressLint("SimpleDateFormat")
	public static String getFileNameFromUri(Uri uri, Context context) {
		String noteName;
		Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);

		if (returnCursor != null) {
			int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
			returnCursor.moveToFirst();
			noteName = returnCursor.getString(nameIndex);
			returnCursor.close();
		} else {
			noteName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		}

		return noteName;
	}
}
