package com.crystal.arc.adapter;

import java.util.LinkedList;

import com.crystal.arc.R;
import com.crystal.arc.entity.User;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserListAdapter extends BaseAdapter {
	
	private Context mContext;
	private Handler mHandler;//在MainActivity进行数据库操作；
	private LinkedList<User> users;
	
	public UserListAdapter(Context mContext, Handler mHandler,
			LinkedList<User> users) {
		super();
		this.mContext = mContext;
		this.mHandler = mHandler;
		this.users = users;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return users.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.user_list_item, null);
			holder.name=(TextView) convertView.findViewById(R.id.name);
			holder.password=(TextView) convertView.findViewById(R.id.password);
			holder.delete=(TextView) convertView.findViewById(R.id.delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final User user = (User) getItem(position);
		holder.name.setText(user.getName());
		holder.password.setText(user.getPassword());
		final int pos=position;
		holder.delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("tag", "id为"+user.getId());
				mHandler.obtainMessage(R.id.user_delete,String.valueOf(user.getId())).sendToTarget();
				users.remove(pos);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	final class ViewHolder {
		TextView name;
		TextView password;
		TextView delete;
	}
}
