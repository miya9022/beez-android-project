package com.android.beez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.beez.adapter.FavouriteAdapter;
import com.android.beez.adapter.NewsAdapter;
import com.android.beez.api.NewsSourceApiClient;
import com.android.beez.app.AppController;
import com.android.beez.model.NewsBeez;
import com.android.beez.ui.Actionbar;
import com.android.beez.ui.Slidemenu;
import com.android.beez.utils.JSON2Object;
import com.android.beez.utils.Params;
import com.android.beez.utils.ShowMessage;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.analytics.GoogleAnalytics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.AbsListView.OnScrollListener;

public class FavouriteActivity extends MenuActivity implements AbsListView.OnItemClickListener{
	
	private boolean nomoreData = false;
	private com.etsy.android.grid.StaggeredGridView gridView;
	private ArrayList<NewsBeez> newsList;
	private FavouriteAdapter adapter;
	private FavouriteAdapter display_adapter;
	private int page = 1;
	private boolean isLastPage = false;
	
	private int quota_display = AppController.getInstance().getDisplayQuota();
	private int concurrent = 0;
	
	private Button loadMore;
	private ImageButton imgb_scrolltop;

	private final String default_img_url = "http://beez.club/img/38x38xfavicon.png.pagespeed.ic.lvWi7wDCqW.png";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourite);
		Slidemenu.getInstance().clearMenuActivity(this);
		loadMore = new Button(this);
        loadMore.setText(R.string.btn_more);
		loadMore.setBackgroundColor(R.drawable.mybutton);
		loadMore.setTextColor(getResources().getColor(R.color.white));
		loadMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadFavouritePosts(v);
			}
		});
		
		
		gridView = (com.etsy.android.grid.StaggeredGridView) findViewById(R.id.favourite_Gridview);
		LoadFavouritePosts(null);
		gridView.setOnItemClickListener(this);
		
		imgb_scrolltop = (ImageButton) findViewById(R.id.scroll_top_favour);
        imgb_scrolltop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(imgb_scrolltop.getVisibility() != View.GONE && gridView.getFirstVisiblePosition() > 0){
					if(display_adapter != null) gridView.setAdapter(display_adapter); 
				}
			}
		});
        gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(gridView.getFirstVisiblePosition() == 0){
					imgb_scrolltop.setVisibility(View.GONE);
				} else {
					imgb_scrolltop.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	protected void onShowListResponse(String data) {
		try{
			JSONObject jsonObject = new JSONObject(data);
			String code = jsonObject.getString("code");
			if(Params.ERROR_10010.equals(code)){
				ShowMessage.showDialogUpdateApp(this);
				return;
			}
			if (!"OK".equals(code)) {
				nomoreData = true;
				loadMore.setVisibility(View.GONE);
				return;
			}
			
			String strData = jsonObject.getString(Params.DATA);
			
			if(strData == null){
				nomoreData = true;
				loadMore.setVisibility(View.GONE);
				return;
			}
			
			JSONArray jsonItems = new JSONArray(strData);
			if (jsonItems.length() <= 0) {
				nomoreData = true;
				isLastPage = true;
				loadMore.setVisibility(View.GONE);
				return;
			} else {
				nomoreData = false;
			}
			
			if(isLastPage){
				if(adapter != null){
					if(adapter.getEntries().size() > 1){
						Collections.sort(adapter.getEntries(), new Comparator<NewsBeez>() {

							@Override
							public int compare(NewsBeez lhs, NewsBeez rhs) {
								return rhs.getView() - lhs.getView();
							}
							
						});
					}
					int totalLength = adapter.getEntries().size();
					final ArrayList<NewsBeez> entries = adapter.getEntries();
					int current_length = (totalLength < concurrent + quota_display) ? totalLength : concurrent + quota_display;
					newsList = new ArrayList<NewsBeez>();
					for(int i = concurrent; i < current_length; i++){
						newsList.add(entries.get(i));
						if(i == totalLength-1) loadMore.setVisibility(View.GONE);
					}
					concurrent = current_length;
					if(display_adapter == null){
						display_adapter = new FavouriteAdapter(this, newsList);
						gridView.setAdapter(adapter);
					} else {
						display_adapter.getEntries().addAll(newsList);
						display_adapter.notifyDataSetChanged();
					}
				}
			} else {
				newsList = new ArrayList<NewsBeez>();
				for(int i = 0; i < jsonItems.length(); i++){
					JSONObject item = jsonItems.getJSONObject(i);
					JSON2Object j2o = new JSON2Object(NewsBeez.class, item);
					NewsBeez news = (NewsBeez) j2o.parse();
					String headline_img = item.optString(Params.HEADLINE_IMG, "NULL");
					String time = item.optString(Params.TIME, "NULL");
					String app_domain = item.optString(Params.APP_DOMAIN, "NULL");
					String origin_url = item.optString(Params.ORIGIN_URL,"NULL");
					int view = item.optInt(Params.VIEW, 0);
					if (headline_img != null){
						news.setHeadline_img(headline_img);
						news.setTime(time);
						news.setApp_domain(app_domain);
						news.setView(view);
						news.setOrigin_url(origin_url);
					} else {
						news.setHeadline_img(default_img_url);
					}
					if(time.contains("2015.06") || time.contains("hour") || time.contains("second")){
							newsList.add(news);
					}
					Log.v("Favourite Activity",i+": "+newsList.get(i).getView());
				}
				
				if(adapter == null){
					adapter = new FavouriteAdapter(this, newsList);
				} else {
					if (newsList.size() > 0) {
						adapter.getEntries().addAll(newsList);
					}
				}
			}
		} catch(Exception ex){
			ex.printStackTrace();
			nomoreData = true;
			isLastPage = true;
			loadMore.setVisibility(View.GONE);
		}
	}
	
	protected void onShowListErrorResponse(VolleyError error) {
		nomoreData = true;
	}
	
	protected void LoadFavouritePosts(View v){
		while(isLastPage){
			AppController.getInstance().showProgressDialog(this);
			NewsSourceApiClient apiClient = AppController.getInstance().getNewsApiClient();
			apiClient.showListNewsByPage(new Response.Listener<String>() {
	
				@Override
				public void onResponse(String data) {
					onShowListResponse(data);
					AppController.getInstance().hideProgressDialog();
					
				}
				
			}, new Response.ErrorListener() {
	
				@Override
				public void onErrorResponse(VolleyError arg0) {
					onShowListErrorResponse(arg0);
					AppController.getInstance().hideProgressDialog();
					
				}
				
			}, page);
			page += !isLastPage ? 1 : 0;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Get an Analytics tracker to report app starts & uncaught exceptions
		// etc.
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Actionbar.getInstance().appendTo(this);
		Slidemenu.getInstance().appendTo(this);
		Actionbar.getInstance().showSlidingMenu(View.VISIBLE);
		Actionbar.getInstance().showBack(View.INVISIBLE);
		if(gridView.getFirstVisiblePosition() == 0){
			imgb_scrolltop.setVisibility(View.GONE);
		} else {
			imgb_scrolltop.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		NewsBeez entry = newsList.get(position);
		Intent i = new Intent(FavouriteActivity.this, ViewContentActivity.class);
		i.putExtra(Params.TITLE, entry.getTitle());
		i.putExtra(Params.HEADLINE, entry.getHeadline());
		i.putExtra(Params.HEADLINE_IMG, entry.getHeadline_img());
		i.putExtra(Params.APP_DOMAIN, entry.getApp_domain());
		i.putExtra(Params.TIME, entry.getTime());
		i.putExtra(Params.VIEW, entry.getView());
		startActivity(i);
	}
}
