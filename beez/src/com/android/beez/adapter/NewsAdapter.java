package com.android.beez.adapter;

import java.util.ArrayList;
import java.util.Queue;

import com.android.beez.R;
import com.android.beez.app.AppController;
import com.android.beez.loadimage.ImageFetcher;
import com.android.beez.model.NewsBeez;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<NewsBeez> entries;
	private int layoutResourceId;
	private Context context;
	private ImageFetcher imageFetcher;
	private AlertDialog alert;
	
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
		NewsEntryHolder holder = null;
		final NewsBeez entry = entries.get(position);
		
		if(view == null){
			view = inflater.inflate(this.layoutResourceId, parent, false);
			holder = new NewsEntryHolder();
			holder.headline_img = (ImageView) view.findViewById(R.id.headline_img);
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.headline = (TextView) view.findViewById(R.id.headline);
			holder.time = (TextView) view.findViewById(R.id.time);
			holder.app_domain = (TextView) view.findViewById(R.id.app_domain);
			view.setTag(holder);
		} else {
			holder = (NewsEntryHolder)view.getTag();
		}
		
		holder.title.setText(entry.getTitle());
		holder.headline.setText(entry.getHeadline());
		holder.time.setText(entry.getTime());
		holder.app_domain.setText(entry.getApp_domain());
		imageFetcher.loadImage(entry.getHeadline_img(), holder.headline_img);
		return view;
	}
	
	static class NewsEntryHolder{
		ImageView headline_img;
		TextView title;
		TextView headline;
		TextView time;
		TextView app_domain;
	}
	
//	public NewsAdapter(Context context, Queue<NewsBeez> entriesDisplay) {
//		super();
//		this.entries = entriesDisplay;
//		
//		this.layoutResourceId = R.layout.item_newslist;
//		this.context = context;
//		this.inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		this.imageFetcher = AppController.getInstance().getImageFetcher();
//	}
	
	public NewsAdapter(Context context, ArrayList<NewsBeez> entries) {
		super();
		this.entries = entries;
		
		this.layoutResourceId = R.layout.item_newslist;
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageFetcher = AppController.getInstance().getImageFetcher();
	}

	public NewsAdapter(Context context, ArrayList<NewsBeez> entries, boolean hideOrder) {
		super();
		this.entries = entries;
		this.layoutResourceId = R.layout.item_newslist;
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageFetcher = AppController.getInstance().getImageFetcher();
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

	public ImageFetcher getImageFetcher() {
		return imageFetcher;
	}

	public void setImageFetcher(ImageFetcher imageFetcher) {
		this.imageFetcher = imageFetcher;
	}

	public AlertDialog getAlert() {
		return alert;
	}

	public void setAlert(AlertDialog alert) {
		this.alert = alert;
	}
}
