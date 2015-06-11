package com.android.beez.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.beez.MenuActivity;
import com.android.beez.R;
import com.android.beez.ViewContentActivity;

public class Actionbar {
	private static Actionbar instance = null;
	private MenuActivity parentActivity;
	private View view;

	private ImageButton nowPlaying;
	private ImageButton slidingMenu;
	private ImageButton back;
	private ImageView activityTitle;

	public static Actionbar getInstance() {
		if (instance == null) {
			instance = new Actionbar();
		}

		return instance;
	}

	private Actionbar() {
	}

	public void showNowPlaying() {
		if (nowPlaying != null) {
			nowPlaying.setVisibility(View.VISIBLE);
		}
	}

	public void showNowPlaying(int visibility) {
		if (nowPlaying != null) {
			nowPlaying.setVisibility(visibility);
		}
	}

	public void hideNowPlaying() {
		if (nowPlaying != null) {
			nowPlaying.setVisibility(View.INVISIBLE);
		}
	}


	public void showSlidingMenu(int visibility) {
		if (slidingMenu != null) {
			slidingMenu.setVisibility(visibility);
		}
	}

	public void showBack(int visibility) {
		if (back != null) {
			back.setVisibility(visibility);
		}
	}

//	public void setActivityTitle(String title) {
//		if (activityTitle != null) {
//			activityTitle.setText(title);
//		}
//	}

	public void appendTo(MenuActivity parentActivity) {
		this.parentActivity = parentActivity;

		LayoutInflater inflater = LayoutInflater.from(parentActivity);
		this.view = inflater.inflate(R.layout.myactionbar, null);
		ActionBarActivity aba = (ActionBarActivity) parentActivity;
		ActionBar actionbar = (aba).getSupportActionBar();

		// Set title for activity
		activityTitle = (ImageView) this.view.findViewById(R.id.activity_title);
//		activityTitle.setText(parentActivity.getTitle());

		// Set click handler for sliding menu button (Open menu on clicking)
		slidingMenu = (ImageButton) this.view.findViewById(R.id.sliding_menu);
		slidingMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSlidingMenuClick();
			}
		});

		// Show player activity when user click on Now playing button
//		nowPlaying = (ImageButton) this.view.findViewById(R.id.now_playing);
//		nowPlaying.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				onNowPlayingClick();
//			}
//		});

		// Process back button on the actionbar
		back = (ImageButton) this.view.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackClick();
			}
		});
		
		actionbar.setCustomView(this.view);
		actionbar.setDisplayShowCustomEnabled(true);
		
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
	    Toolbar parent =(Toolbar) view.getParent();
	    parent.setContentInsetsAbsolute(0,0);
	}

	protected void onBackClick() {
		String backStackActivity = parentActivity.getIntent().getStringExtra(
				"back_stack_activity");
//		 Toast.makeText(parentActivity, backStackActivity,
//		 Toast.LENGTH_LONG).show();
		Intent intent = null;
		if("ViewContentActivity".equals(backStackActivity)) {
			intent = new Intent(parentActivity, ViewContentActivity.class);
		}
//		if ("HistoryActivity".equals(backStackActivity)) {
//			intent = new Intent(parentActivity, HistoryActivity.class);
//		} else if ("PlaylistActivity".equals(backStackActivity)) {
//			intent = new Intent(parentActivity, PlaylistActivity.class);
//		} else if ("SongsListActivity".equals(backStackActivity)) {
//			intent = new Intent(parentActivity, SongsListActivity.class);
//		} else if ("SearchActivity".equals(backStackActivity)) {
//			intent = new Intent(parentActivity, SearchActivity.class);
//		} else if ("RankingActivity".equals(backStackActivity)) {
//			intent = new Intent(parentActivity, RankingActivity.class);
//		} else {
//			if ("SongsListActivity".equals(parentActivity.getClass()
//					.getSimpleName())) {
//				if (Bottombar.getInstance().getActiveTab() == Bottombar.TAB_PLAYLIST) {
//					intent = new Intent(parentActivity, PlaylistActivity.class);
//				} else {
//					intent = new Intent(parentActivity, RankingActivity.class);
//				}
//			}
//		}
		parentActivity.finish();
		parentActivity.overridePendingTransition(R.anim.diagslide_enter_back,
				R.anim.diagslide_leave_back);
	}

	protected void onSlidingMenuClick() {
		Slidemenu.getInstance().showMenu();
	}

//	protected void onNowPlayingClick() {
//		try {
//			Intent intent = new Intent(parentActivity.getApplicationContext(),
//					PlayerActivity.class);
//			intent.putExtra("back_stack_activity", parentActivity.getClass()
//					.getSimpleName());
////			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////					| IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//			intent.putExtra("back_from_now_playing", true);
//			parentActivity.startActivity(intent);
////			parentActivity.finish();
//			parentActivity.overridePendingTransition(R.anim.diagslide_enter,
//					R.anim.diagslide_leave);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public View getView() {
		return view;
	}

	public Activity getParentActivity() {
		return parentActivity;
	}

	public void setParentActivity(MenuActivity parentActivity) {
		this.parentActivity = parentActivity;
	}
}
