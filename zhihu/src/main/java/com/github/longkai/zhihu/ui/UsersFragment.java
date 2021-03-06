/*
 * The MIT License (MIT)
 * Copyright (c) 2013 longkai(龙凯)
 * The software shall be used for good, not evil.
 */
package com.github.longkai.zhihu.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.github.longkai.zhihu.R;
import com.github.longkai.zhihu.ZhihuApp;
import com.github.longkai.zhihu.util.Constants;
import com.github.longkai.zhihu.util.Utils;

import static com.github.longkai.zhihu.util.Constants.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @User longkai
 * @Date 13-11-11
 * @Mail im.longkai@gmail.com
 */
public class UsersFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private CursorAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new UsersAdapter(getActivity());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setEmptyText(getString(R.string.empty_list));
		setListAdapter(mAdapter);
		setListShown(false);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Cursor cursor = mAdapter.getCursor();
		String uid = cursor.getString(cursor.getColumnIndex(UID));
		// go to zhihu.com
		Utils.viewUserInfo(getActivity(), uid);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Utils.parseUri(ITEMS);
        return new CursorLoader(getActivity(), uri, USER_PROJECTION, null, null, DESC_ORDER);
    }

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.changeCursor(null);
	}

	/**
	 * 用户列表适配器
	 */
	private static class UsersAdapter extends CursorAdapter {

		public UsersAdapter(Context context) {
			super(context, null, 0);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = LayoutInflater.from(context).inflate(R.layout.user_row, null);
			ViewHolder holder = new ViewHolder();
			holder.avatar = (ImageView) view.findViewById(R.id.avatar);
			holder.avatarIndex = cursor.getColumnIndex(AVATAR);
			holder.nick = (TextView) view.findViewById(R.id.nick);
			holder.nickIndex = cursor.getColumnIndex(NICK);
			holder.status = (TextView) view.findViewById(R.id.status);
			holder.statusIndex = cursor.getColumnIndex(STATUS);

			view.setTag(holder);
			return view;
		}

		@Override
		public void bindView(View view, final Context context, final Cursor cursor) {
			final ViewHolder holder = (ViewHolder) view.getTag();
			ImageLoader imageLoader = ZhihuApp.getImageLoader();
			imageLoader.get(cursor.getString(holder.avatarIndex),
					imageLoader.getImageListener(holder.avatar,
							R.drawable.ic_launcher, R.drawable.ic_launcher));
			holder.nick.setText(cursor.getString(holder.nickIndex));
			holder.status.setText(cursor.getString(holder.statusIndex));
		}

		private static class ViewHolder {

			ImageView avatar;
			int avatarIndex;

			TextView nick;
			int nickIndex;

			TextView status;
			int statusIndex;
		}
	}
}
