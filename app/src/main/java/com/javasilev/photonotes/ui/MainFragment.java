package com.javasilev.photonotes.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.javasilev.photonotes.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * Created by Aleksei Vasilev.
 */

public class MainFragment extends Fragment implements Observer<Boolean> {
	@BindView(R.id.fragment_main_button_camera)
	Button mCameraButton;

	@BindView(R.id.fragment_main_button_gallery)
	Button mGalleryButton;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);

		mCameraButton.setOnClickListener((View.OnClickListener) getActivity());
		mGalleryButton.setOnClickListener((View.OnClickListener) getActivity());
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onError(Throwable e) {

	}

	@Override
	public void onNext(Boolean aBoolean) {
		mGalleryButton.setEnabled(aBoolean);
		mCameraButton.setEnabled(aBoolean);
	}
}
