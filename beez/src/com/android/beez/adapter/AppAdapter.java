package com.android.beez.adapter;

import java.util.ArrayList;
import java.util.Random;

import com.android.beez.ListNewsByDataActivity;
import com.android.beez.R;
import com.android.beez.app.AppController;
import com.android.beez.loadimage.ImageFetcher;
import com.android.beez.model.NewsBeez;
import com.android.beez.ui.Slidemenu;
import com.android.beez.utils.Params;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private ArrayList<NewsBeez> entries;
	private int layoutResourceId;
	private Context context;
	
	private static final String TAG = "AppAdapter";
	
	@Override
	public int getCount() {
		return entries!=null ? entries.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		if (entries != null && entries.size()>0){
			return entries.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (entries != null && entries.size()>0){
			return (entries.get(position).getId()==null)?
					0:Long.valueOf(entries.get(position).getId());
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		AppHolder holder = null;
		Log.v(TAG,position+":"+entries.size());
		final NewsBeez entry = entries.get(position);
		if(view == null){
			view = inflater.inflate(this.layoutResourceId, parent, false);
			holder = new AppHolder();
			holder.textview = (TextView) view.findViewById(R.id.app_item_text);
			
			view.setTag(holder);
		} else {
			holder = (AppHolder) view.getTag();
		}
		holder.textview.setText(entry.getName());
		holder.textview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getContext(), ListNewsByDataActivity.class);
				if (entry.isCate() == false) {
					i.putExtra(Params.DATA_TYPE, "domain");
					i.putExtra(Params.ID, entry.getId());
					i.putExtra(Params.NAME, entry.getName());
					i.putExtra(Params.APP_ID, entry.getApp_id());
					i.putExtra(Params.APP_DOMAIN, entry.getApp_domain());
				} else if (entry.isCate() == true) {
					i.putExtra(Params.DATA_TYPE, "category");
					i.putExtra(Params.ID, entry.getId());
					i.putExtra(Params.TITLE, entry.getTitle());
					i.putExtra(Params.NAME, entry.getName());
				}
				getContext().startActivity(i);
			}
		});
		return view;
	}
	
	static class AppHolder {
		TextView textview;
	}
	
	public AppAdapter(Context context, ArrayList<NewsBeez> entries) {
		super();
		this.entries = entries;
		this.layoutResourceId = R.layout.item_sliding_gv_app;
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	public ArrayList<NewsBeez> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<NewsBeez> entries) {
		this.entries = entries;
	}

	public int getLayoutResourceId() {
		return layoutResourceId;
	}

	public void setLayoutResourceId(int layoutResourceId) {
		this.layoutResourceId = layoutResourceId;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
