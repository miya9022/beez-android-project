package com.android.beez;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class NewsPlayer implements OnItemClickListener, OnScrollChangedListener {
	private Context context;
	
	public NewsPlayer(Context context) {
		this.context = context;
	}
	
	@Override
	public void onScrollChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	
}
