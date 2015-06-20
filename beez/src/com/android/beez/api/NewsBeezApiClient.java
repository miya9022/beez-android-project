package com.android.beez.api;

import java.util.*;

import com.android.beez.app.AppController;
import com.android.beez.utils.Params;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import android.content.Context;
import android.util.Log;

public class NewsBeezApiClient extends BaseNewsSourceApiClient {

	public NewsBeezApiClient(String baseUrl, String apiBaseUrl, Context ctx) {
		super(baseUrl, apiBaseUrl);
		this.context = ctx;
	}

	@Override
	public String showListNews(Listener<String> listener, ErrorListener errorListener) {
		StringBuilder sb = new StringBuilder(this.apiBaseUrl);
		sb.append(Params.LISTPOST);
		String apiUri = sb.toString();
		
		StringRequest req = new StringRequest(apiUri, listener, errorListener);
		AppController.getInstance().addToRequestQueue(req);
		
		return null;
	}

	@Override
	public String searchByTime(String time, Listener<String> listener,
			ErrorListener errorlistener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String showRecommendPosts(Listener<String> listener, ErrorListener errorListener) {
		StringBuilder sb = new StringBuilder(this.apiBaseUrl);
		sb.append(Params.RECOMMEND);
		String apiUri = sb.toString();

		StringRequest req = new StringRequest(apiUri, listener, errorListener);
		AppController.getInstance().addToRequestQueue(req);
		return apiUri;
	}

	@Override
	public String LoadDataById(Listener<String> listener, ErrorListener errorListener) {
		StringBuilder sb = new StringBuilder(this.apiBaseUrl);
		sb.append(Params.DATA_URl);
		String apiUri = sb.toString();
		
		StringRequest req = new StringRequest(apiUri, listener, errorListener);
		AppController.getInstance().addToRequestQueue(req);
		
		return apiUri;
	}

	@Override
	public String showListNewsByPage(Listener<String> listener, ErrorListener errorListener, final int page) {
		StringBuilder sb = new StringBuilder(this.apiBaseUrl);
		sb.append(Params.LISTPOST);
		String apiUri = sb.toString();
		
		StringRequest req = new StringRequest(Method.POST, apiUri, listener, errorListener){
			protected Map<String, String> getParams() throws com.android.volley.AuthFailureError{
				Map<String, String> params = new HashMap<String, String>();
				params.put(Params.PAGE, page == 0 ? "1" : Integer.toString(page));
				return params;
			};
		};
		AppController.getInstance().addToRequestQueue(req);
		return null;
	}
	
}
