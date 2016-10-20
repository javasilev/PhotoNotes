package com.javasilev.photonotes.data;

import java.util.List;

import com.javasilev.photonotes.models.Note;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by Aleksei Vasilev.
 */

public class RealmDataSource implements DataSource<Note> {
	private Realm mRealm = Realm.getDefaultInstance();

	private static RealmDataSource sInstance;

	public static RealmDataSource getInstance() {
		if (sInstance == null) {
			synchronized (RealmDataSource.class) {
				if (sInstance == null) {
					sInstance = new RealmDataSource();
				}
			}
		}
		return sInstance;
	}

	private RealmDataSource() {
	}

	@Override
	public Note createOrUpdate(Note note) {
		if (note.getId() == 0) {
			note.setId(getNextKey());
		}
		mRealm.beginTransaction();
		mRealm.copyToRealmOrUpdate(note);
		mRealm.commitTransaction();
		return note;
	}

	@Override
	public Note getItem(long itemId) {
		return mRealm.where(Note.class).equalTo("id", itemId).findFirst();
	}

	@Override
	public void deleteItem(long itemId) {
		mRealm.beginTransaction();
		mRealm.where(Note.class).equalTo("id", itemId).findFirst().deleteFromRealm();
		mRealm.commitTransaction();
	}

	@Override
	public List<Note> getAll() {
		return mRealm.where(Note.class).findAllSorted("creationDate", Sort.DESCENDING);
	}

	@Override
	public List<Note> find(String searchQuery) {
		return mRealm.where(Note.class)
				.contains("nameLowercased", searchQuery.toLowerCase())
				.or()
				.contains("textLowercased", searchQuery.toLowerCase())
				.findAllSorted("creationDate", Sort.DESCENDING);
	}

	private long getNextKey() {
		Number maxId = mRealm.where(Note.class).max("id");

		return maxId == null ? 1 : maxId.longValue() + 1;
	}
}
