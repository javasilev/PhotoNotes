package com.javasilev.photonotes.adapters.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

/**
 * Created by Aleksei Vasilev.
 */

public abstract class CollectionAdapter<T> extends RecyclerView.Adapter<SimpleViewHolder<T>> {
	protected OnItemClickListener<T> mListener;

	protected LayoutInflater mInflater;
	private Context mContext;
	private final List<T> mList = new ArrayList<>();

	public CollectionAdapter(Context context, OnItemClickListener<T> listener) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mListener = listener;
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	public void setCollection(Collection<? extends T> collection) {
		mList.clear();

		if (collection != null) {
			mList.addAll(collection);
		}

		notifyDataSetChanged();
	}

	public T getItem(int position) {
		return mList.get(position);
	}

	public void clearCollection() {
		mList.clear();
		notifyDataSetChanged();
	}

	public void removeItem(T item) {
		mList.remove(item);
		notifyDataSetChanged();
	}

	public void removeItem(int itemId) {
		mList.remove(itemId);
		notifyItemRemoved(itemId);
	}

	public void addItem(T item) {
		mList.add(item);
		notifyItemInserted(getItemCount() - 1);
	}

	public List<T> getCollection() {
		return mList;
	}

	protected Context getContext() {
		return mContext;
	}

	public interface OnItemClickListener<T> {
		void onItemClick(T item);
	}
}
