package com.javasilev.photonotes.adapters;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.javasilev.photonotes.R;
import com.javasilev.photonotes.adapters.base.SimpleViewHolder;
import com.javasilev.photonotes.models.Note;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aleksei Vasilev.
 */

@SuppressWarnings("WeakerAccess")
public class NoteViewHolder extends SimpleViewHolder<Note> {

	@BindView(R.id.item_note_card_text_view_name)
	TextView mNameTextView;

	@BindView(R.id.item_note_card_text_view_date)
	TextView mDateTextView;

	@BindView(R.id.item_note_card_text_view_content)
	TextView mContentTextView;

	@BindView(R.id.item_note_card_swipe)
	SwipeLayout mSwipeLayout;

	@BindView(R.id.item_note_card_button_delete)
	ImageButton mDeleteButton;

	public NoteViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void bind(final Note model, final NoteAdapter.OnItemClickListener<Note> listener) {
		mNameTextView.setText(model.getName());
		mDateTextView.setText(new SimpleDateFormat("dd.MM.yyyy").format(model.getCreationDate()));
		mContentTextView.setText(model.getText());

		mSwipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
		mSwipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onItemClick(model);
			}
		});

		mDeleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onDelete(getAdapterPosition(), model.getId());
			}
		});
	}
}
