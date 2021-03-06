package com.android.beez.api;

import com.android.volley.Response;
import com.android.volley.Response.Listener;

public interface NewsSourceApiClient {
	//show all news
	public String showListNews(Listener<String> listener, Response.ErrorListener errorlistener);
	//search posts by time
	public String searchByTime(String time, Listener<String> listener, Response.ErrorListener errorlistener);
	//list recommend posts
	public String showRecommendPosts(Listener<String> listener, Response.ErrorListener errorlistener);
	
	//load data(app_id, cate_id) by Id
	public String LoadDataById(Listener<String> listener, Response.ErrorListener errorlistener);
}
