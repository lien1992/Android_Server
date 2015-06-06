package com.crystal.arc.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.crystal.arc.R;

public class SettingActivity extends Activity {
	private static String[] settingItems = { "端口设置", "连接方式", "主题设置" };
	private ListView settingList;
	ArrayAdapter<String> settingListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setting);
		settingList=(ListView) this.findViewById(R.id.setting_menu);
		settingListAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item_2,
				settingItems);
		settingList.setAdapter(settingListAdapter);
		settingList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
