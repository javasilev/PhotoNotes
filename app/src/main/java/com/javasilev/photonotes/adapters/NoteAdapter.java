package com.javasilev.photonotes.adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.javasilev.photonotes.R;
import com.javasilev.photonotes.adapters.base.CollectionAdapter;
import com.javasilev.photonotes.adapters.base.SimpleViewHolder;
import com.javasilev.photonotes.models.Note;

/**
 * Created by Aleksei Vasilev.
 */

public class NoteAdapter extends CollectionAdapter<Note> {

	public NoteAdapter(Context context, OnItemClickListener<Note> listener) {
		super(context, listener);
	}

	@Override
	public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new NoteViewHolder(mInflater.inflate(R.layout.item_note_card, parent, false));
	}

	@Override
	public void onBindViewHolder(SimpleViewHolder<Note> holder, int position) {
		holder.bind(getItem(position), mListener);
	}
}
