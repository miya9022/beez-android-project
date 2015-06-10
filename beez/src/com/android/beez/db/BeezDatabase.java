package com.android.beez.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.android.beez.model.NewsBeez;
import com.android.beez.utils.Params;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class BeezDatabase extends SqliteDatabase{
	public BeezDatabase(Context context, String dbName) {
		super(context, dbName);
	}

	public ArrayList<Map<String, String>> getNews(){
		/**
		 * CREATE TABLE NEWS_BEEZ
		 */
		final String SELECT_PLAYLIST = "SELECT ID, TITLE FROM NEWS_BEEZ";
		Cursor cursor = query(SELECT_PLAYLIST);
		if (cursor == null) return null;
		
		ArrayList<Map<String, String>> newslist = new ArrayList<Map<String, String>>();
		if (cursor.moveToFirst()) {
			do {
				
				Map<String, String> item = new HashMap<String, String>();
				item.put("ID", String.valueOf(cursor.getInt(0)));
				item.put("TITLE", cursor.getString(1));				
				newslist.add(item);
			} while (cursor.moveToNext());
		}
		close();
		if(cursor != null)
	        cursor.close();
		return newslist;
	}
}
