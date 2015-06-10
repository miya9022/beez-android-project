package com.android.beez;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.android.beez.app.AppController;
import com.android.beez.ui.Actionbar;
import com.android.beez.ui.InterstitialAds;
import com.android.beez.ui.Slidemenu;


public class MenuActivity extends ActionBarActivity implements InterstitialAds.OnInterstitialAdsEventListener {
	public   String TAG = MenuActivity.class.getSimpleName();
	protected InterstitialAds ads = null;
	
	private PullToRefreshListView listView;
	private Button loadMore;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Slidemenu.getInstance().clearMenuActivity(this);
        listView = (PullToRefreshListView)findViewById(R.id.listview);
        listView.getRefreshableView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
			}
        	
        });
        
        listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
			}
        });
        loadMore = new Button(this);
        loadMore.setText(R.string.btn_more);
		loadMore.setBackgroundColor(getResources().getColor(
				R.color.btn_background));
		loadMore.setTextColor(getResources().getColor(R.color.white));
		loadMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
        listView.getRefreshableView().addFooterView(loadMore);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
