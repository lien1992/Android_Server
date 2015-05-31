package com.crystal.arc.ui;

import com.crystal.arc.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutMeFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(
				R.layout.about_me, container, false);
		return rootView;
	}
}
