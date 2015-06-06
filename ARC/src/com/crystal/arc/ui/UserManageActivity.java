package com.crystal.arc.ui;

import java.util.Date;
import java.util.LinkedList;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.crystal.arc.R;
import com.crystal.arc.adapter.UserListAdapter;
import com.crystal.arc.contentprovider.UserContract;
import com.crystal.arc.entity.User;

public class UserManageActivity extends Activity {
	private ListView userList;
	private TextView addButton;
	private LinkedList<User> users;
	private ContentResolver cr;
	private UserListAdapter ula;
	private Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.user_manage);
		cr=this.getContentResolver();
		mHandler=new Handler(){
			@Override
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
				switch (msg.what) {
				case R.id.user_delete:
					String userId=(String)msg.obj;
					deleteUser(userId);
					break;
				}
			}
		};
		userList=(ListView) this.findViewById(R.id.userlist);
		addButton=(TextView) this.findViewById(R.id.add_button);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				showDialog(null);
			}
		});
		users=getUserlist();
		ula=new UserListAdapter(this, mHandler, users);
		userList.setAdapter(ula);
		userList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {
				showDialog((User)ula.getItem(paramInt));
			}
		});
	}
	
	public LinkedList<User> getUserlist(){
		LinkedList<User> list=new LinkedList<User>();
		String[] projection=new String[]{UserContract.TID,UserContract.NAME,UserContract.PASSWORD,UserContract.REGISTER_DATE};
		Cursor cursor=cr.query(UserContract.CONTENT_URI, projection, UserContract.DELETE_FLAG+"=?", new String[]{"0"}, UserContract.REGISTER_DATE);
		if(cursor!=null){
			while (cursor.moveToNext()) {
				long id=cursor.getLong(cursor.getColumnIndex(UserContract.TID));
				String name=cursor.getString(cursor.getColumnIndex(UserContract.NAME));
				String password=cursor.getString(cursor.getColumnIndex(UserContract.PASSWORD));
				long registerDate=cursor.getLong(cursor.getColumnIndex(UserContract.REGISTER_DATE));
				User user=new User(id,name,password,registerDate);
				list.add(user);
			}
			cursor.close();
		}
		return list;
	}
	//修改添加用户后都记得更新列表和数据库
	public long addUser(String name, String password){
		ContentValues values = new ContentValues();
        values.put(UserContract.NAME, name);
        values.put(UserContract.PASSWORD, password);
        values.put(UserContract.REGISTER_DATE, new Date().getDate());
		Uri inserted = cr.insert(UserContract.CONTENT_URI, values);
		return ContentUris.parseId(inserted);
	}
	
	public int modifyUser(User user){
		ContentValues values = new ContentValues();
		values.put(UserContract.NAME, user.getName());
        values.put(UserContract.PASSWORD, user.getPassword());
        return cr.update(UserContract.CONTENT_URI, values, UserContract.TID+"="+user.getId(),null);
	}
	
	public int deleteUser(String id){
		return cr.delete(UserContract.CONTENT_URI, UserContract.TID+"="+id, null);
	}
	
	public void showDialog(final User user){
		final Dialog dialog = new Dialog(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
		dialog.setContentView(view );
		final EditText name = (EditText) view.findViewById(R.id.name);
		final EditText password = (EditText) view.findViewById(R.id.password);
		final ImageView warning1= (ImageView) view.findViewById(R.id.warning1);
		final ImageView warning2= (ImageView) view.findViewById(R.id.warning2);
		Button confirmBtn = (Button) dialog.findViewById(R.id.positive);
		Button cancelBtn = (Button) dialog.findViewById(R.id.negative);
		
		if(user!=null){//user为空则代表添加用户，user不为空在代表修改用户；
			name.setText(user.getName());
			password.setText(user.getPassword());
			dialog.setTitle("用户信息修改");
		}else{
			dialog.setTitle("添加用户");
		}
		
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				boolean flag1=false;
				boolean flag2=false;
				String nameValue=name.getText().toString().trim();
				String passwordValue=password.getText().toString().trim();
				if(TextUtils.isEmpty(nameValue)){
					warning1.setVisibility(View.VISIBLE);
				}else{
					warning1.setVisibility(View.INVISIBLE);
					flag1=true;
				}
				if(TextUtils.isEmpty(passwordValue)){
					warning2.setVisibility(View.VISIBLE);
				}else{
					warning2.setVisibility(View.INVISIBLE);
					flag2=true;
				}
				if(flag1&&flag2){
					if(user==null){
						long id = addUser(nameValue,passwordValue);
						Log.d("tag", "id为"+id);
						User inserted = new User(id, nameValue, passwordValue, 0);
						users.add(inserted);
						ula.notifyDataSetChanged();
					}else{
						user.setName(nameValue);
						user.setPassword(passwordValue);
						modifyUser(user);
						ula.notifyDataSetChanged();
					}
					dialog.dismiss();
				}
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				dialog.dismiss();
			}
		});
		
		dialog.setCancelable(false);
		dialog.show();
	}
}
