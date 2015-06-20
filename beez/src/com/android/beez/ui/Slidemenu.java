package com.android.beez.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.support.v4.content.IntentCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.android.beez.FavouriteActivity;
import com.android.beez.ListNewsByDataActivity;
import com.android.beez.MenuActivity;
import com.android.beez.NewsListActivity;
import com.android.beez.R;
import com.android.beez.SuggestActivity;
import com.android.beez.adapter.AppAdapter;
import com.android.beez.adapter.NewsAdapter;
import com.android.beez.api.NewsSourceApiClient;
import com.android.beez.app.AppController;
import com.android.beez.model.NewsBeez;
import com.android.beez.utils.Params;
import com.android.beez.utils.ShowMessage;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class Slidemenu implements OnItemClickListener {
	private static Slidemenu instance = null;
	private MenuActivity parentActivity;
	private View view;
	private SlidingMenu menu = null;
	private HashMap<String, SlidingMenu> menuList = new HashMap<String, SlidingMenu>();

	public static Slidemenu getInstance() {
		if (instance == null) {
			instance = new Slidemenu();
		}

		return instance;
	}

	private Slidemenu() {
	}

	public void showMenu() {
		if (menuList.get(parentActivity.TAG) != null) {
			menuList.get(parentActivity.TAG).showMenu();
		}
	}

	public void hideMenu() {
		if (menuList.get(parentActivity.TAG) != null) {
			menuList.get(parentActivity.TAG).toggle(true);
		}
	}

	public boolean isShowMneu() {
		if (menuList.get(parentActivity.TAG) != null) {
			return menuList.get(parentActivity.TAG).isMenuShowing();
		}
		return false;
	}

	public void clearMenuActivity(MenuActivity ac) {
		menuList.remove(ac.TAG);
	}

	public void appendTo(MenuActivity parentActivity) {
		this.parentActivity = parentActivity;
		if (menuList.get(parentActivity.TAG) != null) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) this.parentActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.view = inflater.inflate(R.layout.slidingmenu, null);
		menu = new SlidingMenu(this.parentActivity);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setMode(SlidingMenu.LEFT);
		menu.setBehindOffset((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 50, this.parentActivity
						.getResources().getDisplayMetrics()));
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this.parentActivity, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(this.view);
		menuList.put(parentActivity.TAG, menu);
		ExpandableListView menuItemList = (ExpandableListView) this.view
				.findViewById(R.id.slidingmenu);
		menuItemList.setAdapter(new SlidemenuAdapter(this.parentActivity));
		menuItemList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TextView v = (TextView) view.findViewById(R.id.slidingmenu_item_text);
		if ("Home".equals(v.getTag().toString())) {
			Intent intent = new Intent(this.parentActivity,
					NewsListActivity.class);
			parentActivity.startActivity(intent);
		} else if ("Favourite".equals(v.getTag().toString())) {
			Intent intent = new Intent(this.parentActivity,
					FavouriteActivity.class);
			parentActivity.startActivity(intent);
		} else if ("Suggest".equals(v.getTag().toString())) {
			Intent intent = new Intent(this.parentActivity,
					SuggestActivity.class);
			parentActivity.startActivity(intent);
		}
		// if ("PlayInBackground".equals(v.getTag().toString())) {
		// // Do nothing
		// } else if ("InviteFriend".equals(v.getTag().toString())) {
		// Intent intent = new Intent(this.parentActivity,
		// InviteActivity.class);
		// intent.putExtra("back_stack_activity", this.parentActivity
		// .getClass().getSimpleName());
		// // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
		// // IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
		//
		// parentActivity.startActivity(intent);
		// // parentActivity.finish();
		// parentActivity.overridePendingTransition(R.anim.diagslide_enter,
		// R.anim.diagslide_leave);
		//
		// } else if ("ReviewApp".equals(v.getTag().toString())) {
		// final String appPackageName = this.parentActivity.getPackageName();
		// try {
		// this.parentActivity.startActivity(new Intent(
		// Intent.ACTION_VIEW, Uri.parse("market://details?id="
		// + appPackageName)));
		// } catch (android.content.ActivityNotFoundException anfe) {
		// this.parentActivity
		// .startActivity(new Intent(
		// Intent.ACTION_VIEW,
		// Uri.parse("http://play.google.com/store/apps/details?id="
		// + appPackageName)));
		// }
		// parentActivity.overridePendingTransition(R.anim.diagslide_enter,
		// R.anim.diagslide_leave);
		// }
		// else if ("Exit".equals(v.getTag().toString())){
		// AppController.getInstance().getSharedMem().put("app_exit", true);
		//
		// InterstitialAds ads = new InterstitialAds();
		// ads.setAdsTitle(this.parentActivity.getResources().getString(R.string.msg_confirm_close_app));
		// ads.setOkTitle(this.parentActivity.getResources().getString(R.string.btn_accept));
		// ads.show(this.parentActivity, 6);
		// }
	}

	public View getView() {
		return view;
	}

	public Activity getParentActivity() {
		return parentActivity;
	}

	public void setParentActivity(MenuActivity parentActivity) {
		this.parentActivity = parentActivity;
	}

	private class SlidemenuAdapter extends BaseExpandableListAdapter {
		private LayoutInflater inflater;

		private Context context;
		private ArrayList<MenuItem> data = new ArrayList<MenuItem>();
		private HashMap<MenuItem, ArrayList<NewsBeez>> menu_child;
		private AppAdapter adapter;

		private ArrayList<NewsBeez> domainList;
		private ArrayList<NewsBeez> cateList;
		private GridView gridview;

		private class MenuItem {
			public final static int TYPE_TOGGLE = 1;
			public final static int TYPE_CHECK = 2;
			public final static int TYPE_BUTTON = 0;

			private long id;
			private int thumbIcon;
			private String name;
			private String tag;
			private AppAdapter adapter;
			private int type = TYPE_BUTTON;

			public MenuItem(long id, int thumbIcon, String name, String tag,
					int type) {
				super();
				this.id = id;
				this.thumbIcon = thumbIcon;
				this.name = name;
				this.tag = tag;
				this.type = type;
			}

			public AppAdapter getAdapter() {
				return adapter;
			}

			public void setAdapter(AppAdapter adapter) {
				this.adapter = adapter;
			}

			public int getThumbIcon() {
				return thumbIcon;
			}

			public String getName() {
				return name;
			}

			public long getId() {
				return id;
			}

			public String getTag() {
				return tag;
			}

			public int getType() {
				return type;
			}
		}

		public SlidemenuAdapter(Context context) {
			super();
			String menuItems[] = context.getResources().getStringArray(
					R.array.slidingmenu_items);
			String menuItemTags[] = context.getResources().getStringArray(
					R.array.slidingmenu_item_tags);
			String menuItemTypes[] = context.getResources().getStringArray(
					R.array.slidingmenu_item_types);

			this.inflater = LayoutInflater.from(context);
			for (int i = 0; i < menuItems.length; i++) {
				int type = MenuItem.TYPE_BUTTON;
				if ("toggle".equals(menuItemTypes[i])) {
					type = MenuItem.TYPE_TOGGLE;
				} else if ("check".equals(menuItemTypes[i])) {
					type = MenuItem.TYPE_CHECK;
				} else {
					type = MenuItem.TYPE_BUTTON;
				}
				data.add(new MenuItem(i, 0, menuItems[i], menuItemTags[i], type));
			}
			NewsSourceApiClient apiClient = AppController.getInstance()
					.getNewsApiClient();
			apiClient.LoadDataById(new Response.Listener<String>() {

				@Override
				public void onResponse(String data) {
					menu_child = new HashMap<MenuItem, ArrayList<NewsBeez>>();
					try {
						JSONObject jsonObject = new JSONObject(data);
						String code = jsonObject.getString("code");
						if (Params.ERROR_10010.equals(code)) {
							ShowMessage.showDialogUpdateApp(getInstance()
									.getParentActivity());
							return;
						}
						if (!"OK".equals(code)) {
							return;
						}

						String strData = jsonObject.getString(Params.DATA);
						if (strData == null) {
							return;
						}

						JSONObject jsonItemsObject = new JSONObject(strData);
						String strSite = jsonItemsObject.getString(Params.SITE);
						if (strSite == null) {
							return;
						}

						String strCategory = jsonItemsObject
								.getString(Params.CATEGORY);
						if (strCategory == null) {
							return;
						}

						JSONArray jsonItemsCate = new JSONArray(strCategory);
						if (jsonItemsCate.length() <= 0) {
							return;
						}
						cateList = new ArrayList<NewsBeez>();
						for (int i = 0; i < jsonItemsCate.length(); i++) {
							JSONObject jobject = jsonItemsCate.getJSONObject(i);
							NewsBeez cate_beez = new NewsBeez();
							cate_beez.setId(jobject
									.optString(Params.ID, "NULL"));
							cate_beez.setTitle(jobject.optString(Params.TITLE,
									"NULL"));
							cate_beez.setName(jobject.optString(Params.NAME,
									"NULL"));
							cate_beez.setCate(true);
							cateList.add(cate_beez);
						}
						JSONArray jsonItemsDomain = new JSONArray(strSite);
						if (jsonItemsDomain.length() <= 0) {
							return;
						}
						domainList = new ArrayList<NewsBeez>();
						for (int i = 0; i < jsonItemsDomain.length(); i++) {
							JSONObject jobject = jsonItemsDomain
									.getJSONObject(i);
							NewsBeez domain_beez = new NewsBeez();
							domain_beez.setId(jobject.optString(Params.ID,
									"NULL"));
							domain_beez.setName(jobject.optString(Params.NAME,
									"NULL"));
							domain_beez.setApp_id(jobject.optString(
									Params.APP_ID, "NULL"));
							domain_beez.setApp_domain(jobject.optString(
									Params.APP_DOMAIN, "NULL"));
							domain_beez.setCate(false);
							domainList.add(domain_beez);
						}
						menu_child.put(SlidemenuAdapter.this.data.get(3),
								domainList);
						menu_child.put(SlidemenuAdapter.this.data.get(4),
								cateList);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {

				}

			});

		}

		@Override
		public int getGroupCount() {
			return this.data.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return this.menu_child == null ? 0 : 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return this.data.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return this.menu_child.get(this.data.get(groupPosition));
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(final int groupPosition,
				final boolean isExpanded, View convertView,
				final ViewGroup parent) {
			View view = convertView;
			SlidingmenuItemHolder holder = null;
			final MenuItem item = data.get(groupPosition);
			if (view == null) {
				holder = new SlidingmenuItemHolder();
				view = inflater.inflate(R.layout.slidingmenu_item, null);
				holder.text = (TextView) view
						.findViewById(R.id.slidingmenu_item_text);
				holder.image = (ImageView) view
						.findViewById(R.id.slidingmenu_item_thumb);

				view.setTag(holder);
			} else {
				holder = (SlidingmenuItemHolder) view.getTag();
			}

			holder.text.setText(item.getName());
			holder.text.setTag(item.getTag());
			holder.text.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if ("Home".equals(v.getTag().toString())) {
						Intent intent = new Intent(getParentActivity(),
								NewsListActivity.class);
						parentActivity.startActivity(intent);
					} else if ("Favourite".equals(v.getTag().toString())) {
						Intent intent = new Intent(getParentActivity(),
								FavouriteActivity.class);
						parentActivity.startActivity(intent);
					} else if ("Suggest".equals(v.getTag().toString())) {
						Intent intent = new Intent(getParentActivity(),
								SuggestActivity.class);
						parentActivity.startActivity(intent);
					}
				}

			});
			if ("App_domain".equals(item.getTag().toString()) || "Category".equals(item.getTag().toString())) {
				int imageResourceId = isExpanded ? R.drawable.arrow_down
						: R.drawable.arrow_right;
				holder.image.setImageResource(imageResourceId);
				holder.image.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
						else ((ExpandableListView) parent).expandGroup(groupPosition, true);
					}
				});
				// holder.image.setBackgroundResource(item.getThumbIcon());
				holder.image.setTag(item.getTag());
				Toast.makeText(getParentActivity().getApplicationContext(),
						Integer.toString(groupPosition), Toast.LENGTH_LONG)
						.show();
			} else {
				holder.image.setVisibility(View.GONE);
			}

			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// if (groupPosition >= 3) {

			View item = convertView;
			if (item == null) {
				item = inflater.inflate(R.layout.item_gv_app, null);
			}
			gridview = (GridView) item.findViewById(R.id.app_gridview);
			gridview.setNumColumns(2);
			gridview.setHorizontalSpacing(10);
			AppAdapter adapter = null;
			if(groupPosition == 3){
				adapter = new AppAdapter(parent.getContext(), domainList);
			} else if(groupPosition == 4){
				adapter = new AppAdapter(parent.getContext(), cateList);
			} else {
				item = null;
			}
			if(groupPosition >= 3){
				gridview.setAdapter(adapter);
				int totalHeight = 0;
				for (int size = 0; size < adapter.getCount(); size++) {
					RelativeLayout relativeLayout = (RelativeLayout) adapter
							.getView(size, null, gridview);
					TextView textView = (TextView) relativeLayout.getChildAt(0);
					textView.measure(0, 0);
					totalHeight += textView.getMeasuredHeight();
				}
				ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) gridview
						.getLayoutParams();
				if (params != null) {
					params.height = totalHeight-20;
				}
			}
			return item;
		}

		private ViewGroup getViewGroupChild(View convertView, ViewGroup parent,
				int resourceId) {
			// The parent will be our ListView from the ListActivity
			if (convertView instanceof ViewGroup) {
				return (ViewGroup) convertView;
			}
			Context context = parent.getContext();
			LayoutInflater inflater = LayoutInflater.from(context);
			ViewGroup item = (ViewGroup) inflater.inflate(resourceId, null);
			return item;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		//
		// SharedPreferences shared = AppController.getInstance()
		// .getSharedPreferences();
		// if (item.getType() == MenuItem.TYPE_TOGGLE) {
		// holder.toggle
		// .setChecked(shared.getBoolean(item.getTag(), true));
		// holder.toggle.setTag(item.getTag());
		// } else if (item.getType() == MenuItem.TYPE_CHECK) {
		// holder.check
		// .setSelected(shared.getBoolean(item.getTag(), true));
		// holder.check.setTag(item.getTag());
		// }

	}

	private static class SlidingmenuItemHolder {
		TextView text;
		ImageView image;
		GridView gridview;
		ToggleButton toggle;
		CheckBox check;
	}
}