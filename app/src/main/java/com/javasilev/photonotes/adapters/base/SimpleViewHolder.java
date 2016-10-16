package com.javasilev.photonotes.adapters.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Aleksei Vasilev.
 */

public abstract class SimpleViewHolder<ModelType> extends RecyclerView.ViewHolder {
	public SimpleViewHolder(View itemView) {
		super(itemView);
	}

	abstract public void bind(ModelType model, CollectionAdapter.OnItemClickListener<ModelType> listener);
}
