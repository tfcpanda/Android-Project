package com.example.newsreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment2 extends Fragment {
	private View layoutView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 
		layoutView = inflater.inflate(R.layout.fragment2, null);
		// ����layoutView��Ϊfragment1����
		return layoutView;
	}

}
