package com.android.beez;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.android.beez.app.AppController;
import com.android.beez.ui.InterstitialAds;

@SuppressWarnings("deprecation")
public class MenuActivity extends ActionBarActivity implements InterstitialAds.OnInterstitialAdsEventListener {
	public   String TAG = MenuActivity.class.getSimpleName();
	protected InterstitialAds ads = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.getInstance().trackScreen(getClass().getSimpleName());
		AppController.getInstance().checkInternet();
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
	}


	@Override
	public void OnAdsOKPressedListener() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void OnAdsCancelPressedListener() {
		// TODO Auto-generated method stub
		
	}
}
