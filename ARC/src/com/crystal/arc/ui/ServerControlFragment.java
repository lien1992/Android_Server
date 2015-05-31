package com.crystal.arc.ui;

import com.crystal.arc.MainActivity;
import com.crystal.arc.R;
import com.crystal.arc.ServerService;
import com.crystal.arc.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager; 
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ServerControlFragment extends Fragment {
	private static final String TAG="ServerControlFragment";
	private static Activity ct;
	private TextView viewUrl;
	private Button serverSwitch;
	private Button qrScan;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		ct=this.getActivity();
		View rootView = inflater.inflate(
				R.layout.server_control, container, false);
		viewUrl = (TextView)rootView.findViewById(R.id.url);
		serverSwitch=(Button) rootView.findViewById(R.id.server_switch);
		qrScan=(Button) rootView.findViewById(R.id.scan);
		
		serverSwitch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				Log.d(TAG, ""+ServerService.isRunning());
				if(ServerService.isRunning()){
					((MainActivity)ct).stopService();
					serverSwitch.setBackgroundResource(R.drawable.buttonon);
				}else{
					((MainActivity)ct).startService();
					serverSwitch.setBackgroundResource(R.drawable.buttonooff);
				}
			}
		});
		qrScan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(ct,
                        CaptureActivity.class);
				ct.startActivityForResult(intent, MainActivity.SCAN_RESULT_CODE);
			}
		});
		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public void setUrl(String url){
		this.viewUrl.setText(url);
	}
	
	public void switchQRScan(boolean flag){
		int visibility=flag?View.VISIBLE:View.INVISIBLE;
		this.qrScan.setVisibility(visibility);
	}
	
	public void switchButton(boolean flag){
		int drawable=flag?R.drawable.buttonon:R.drawable.buttonooff;
		this.serverSwitch.setBackgroundResource(drawable);
	}
}
