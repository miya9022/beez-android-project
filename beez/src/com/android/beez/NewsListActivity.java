package com.android.beez;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.beez.adapter.NewsAdapter;
import com.android.beez.api.NewsSourceApiClient;
import com.android.beez.app.AppController;
import com.android.beez.model.NewsBeez;
import com.android.beez.ui.Actionbar;
import com.android.beez.ui.InterstitialAds;
import com.android.beez.ui.Slidemenu;
import com.android.beez.utils.JSON2Object;
import com.android.beez.utils.Params;
import com.android.beez.utils.ShowMessage;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gcm.GCMManager;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class NewsListActivity extends MenuActivity implements InterstitialAds.OnInterstitialAdsEventListener {
	
	private PullToRefreshListView listView;
	private Button loadMore;
	private NewsAdapter adapter = null;
	private ArrayList<NewsBeez> newsList = null;
//	private int quota_display = AppController.getInstance().getDisplayQuota();
//	private Queue<NewsBeez> QueueDisplay = null;
//	private int concurrent = 0;
	private boolean nomoreData = false;
	private final String default_img_url = "http://beez.club/img/38x38xfavicon.png.pagespeed.ic.lvWi7wDCqW.png";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
        Slidemenu.getInstance().clearMenuActivity(this);
//      GCMManager.getInstace().init(this);
//      Actionbar.getInstance().appendTo(this);
//		Slidemenu.getInstance().appendTo(this);
//		Actionbar.getInstance().showSlidingMenu(View.VISIBLE);
//		Actionbar.getInstance().showBack(View.INVISIBLE);
        
        loadMore = new Button(this);
        loadMore.setText(R.string.btn_more);
		loadMore.setBackgroundColor(getResources().getColor(
				R.color.btn_background));
		loadMore.setTextColor(getResources().getColor(R.color.white));
		loadMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onButtonLoadMoreClick(v);
			}
		});
        
        listView = (PullToRefreshListView)findViewById(R.id.listview);
//        listView.getRefreshableView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        AppController.getInstance().showProgressDialog(this);
		NewsSourceApiClient apiClient = AppController.getInstance().getNewsApiClient();
		apiClient.showListNews(new Response.Listener<String>() {

			@Override
			public void onResponse(String data) {
				onShowListResponse(data);
				listView.onRefreshComplete();
				AppController.getInstance().hideProgressDialog();
				
			}
			
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				onShowListErrorResponse(arg0);
				listView.onRefreshComplete();
				AppController.getInstance().hideProgressDialog();
				
			}
			
		});
        listView.getRefreshableView().addFooterView(loadMore);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
			}
        	
        });
        
        listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				onButtonLoadMoreClick(refreshView);
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
				loadMore.setVisibility(View.GONE);
				return;
			}
			newsList = new ArrayList<NewsBeez>();
			for (int i = 0; i < jsonItems.length(); i++) {
				JSONObject item = jsonItems.getJSONObject(i);
				JSON2Object j2o = new JSON2Object(NewsBeez.class, item);
				NewsBeez news = (NewsBeez) j2o.parse();
				String headline_img = item.optString(Params.HEADLINE_IMG, "NULL");
				if (headline_img != null){
					news.setHeadline_img(headline_img);
				} else {
					news.setHeadline_img(default_img_url);
				}
				newsList.add(news);
			}
			//concurrent += quota_display;
			if(adapter == null){
				adapter = new NewsAdapter(this, newsList);
				listView.setAdapter(adapter); 
			} else {
				if (newsList.size() > 0) {
					adapter.getEntries().addAll(newsList);
				}
			}
		} catch(Exception ex){
			ex.printStackTrace();
			nomoreData = true;
			loadMore.setVisibility(View.GONE);
		}
	}
	
	protected void onShowListErrorResponse(VolleyError error) {
		nomoreData = true;
		loadMore.setVisibility(View.GONE);
	}
	
	protected void onButtonLoadMoreClick(View v){
		AppController.getInstance().showProgressDialog(this);
		NewsSourceApiClient apiClient = AppController.getInstance().getNewsApiClient();
		apiClient.showListNews(new Response.Listener<String>() {

			@Override
			public void onResponse(String data) {
				onShowListResponse(data);
				listView.onRefreshComplete();
				AppController.getInstance().hideProgressDialog();
				
			}
			
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				onShowListErrorResponse(arg0);
				listView.onRefreshComplete();
				AppController.getInstance().hideProgressDialog();
				
			}
			
		});
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
//		if (AppController.getInstance().getMusicPlayer().isNowPlaying()) {
//			Actionbar.getInstance().showNowPlaying();
//		} else {
//			Actionbar.getInstance().hideNowPlaying();
//		}
	}
}
