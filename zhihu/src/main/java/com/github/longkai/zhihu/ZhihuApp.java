/*
 * The MIT License (MIT)
 * Copyright (c) 2013 longkai(龙凯)
 * The software shall be used for good, not evil.
 */
package com.github.longkai.zhihu;

import android.app.Application;
import android.content.AsyncQueryHandler;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.github.longkai.zhihu.util.BitmapLruCache;

import static com.github.longkai.zhihu.util.Constants.ITEM;
import static com.github.longkai.zhihu.util.Constants.ITEMS;

/**
 * 知乎阅读应用程序对象.
 *
 * @User longkai
 * @Date 13-11-10
 * @Mail im.longkai@gmail.com
 */
public class ZhihuApp extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

	public static final String TAG = "ZhihuApp";

	private static ZhihuApp sApp; // 全局对象

	private static RequestQueue sQueue; // 异步进行网络连接请求的队列
	private static ImageLoader sLoader; // 异步网络图片抓取与缓存

	private SharedPreferences mPreferences;

	@Override
	public void onCreate() {
		super.onCreate();
		sApp = this;
		sQueue = Volley.newRequestQueue(this);
		// todo makes the cache' s size available
		sLoader = new ImageLoader(sQueue, new BitmapLruCache(100)); // 100 cache entries
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

	public static ZhihuApp getApp() {
		return sApp;
	}

	public static RequestQueue getRequestQueue() {
		return sQueue;
	}

	public static ImageLoader getImageLoader() {
		return sLoader;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(TAG, "key: " + key + " changed!");
	}

	public SharedPreferences getPreferences() {
		return mPreferences;
	}
}
