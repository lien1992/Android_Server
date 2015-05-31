package com.crystal.arc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.arc.ui.SlidingLayout;
import com.crystal.arc.zxing.activity.CaptureActivity;

import de.fun2code.android.pawserver.PawServerActivity;
import de.fun2code.android.pawserver.PawServerService;
import de.fun2code.android.pawserver.listener.ServiceListener;
import de.fun2code.android.pawserver.util.Utils;

/**
 * Sample "Build your own PAW server" Activity.
 * 
 *
 */
public class MainActivity extends PawServerActivity implements ServiceListener {
	private static String tip="请点击按钮启动服务器";
	private static final int SCAN_RESULT_CODE = 2015;
	private String[] menuItems = { "用户管理", "设置", "关于" };
	
	private static Activity ct;
	private SlidingLayout slidingLayout;
	private LinearLayout mainLayout;
	private ListView menuList;
	private TextView viewUrl;
	private Button serverSwitch;
	private Button qrScan;
	
	private ArrayAdapter<String> menuListAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		TAG = "MainActivity";
		
		INSTALL_DIR = getFilesDir().getAbsolutePath() + "/www";
		
		// Use sdcard
		//INSTALL_DIR = Environment.getExternalStorageDirectory().getPath() + "/www";

		/*
		 * Turn the PawServerActivity into runtime mode.
		 * Otherwise an error may occur if some things special to the
		 * original PAW server are not available.
		 */
		calledFromRuntime = true;

		super.onCreate(savedInstanceState);
		ct=this;
		setContentView(R.layout.main);
		slidingLayout = (SlidingLayout) findViewById(R.id.slidingLayout);
		mainLayout=(LinearLayout) findViewById(R.id.main_layout);
		menuList=(ListView) findViewById(R.id.menulist);
		viewUrl = (TextView)this.findViewById(R.id.url);
		serverSwitch=(Button) this.findViewById(R.id.server_switch);
		qrScan=(Button) this.findViewById(R.id.scan);
		
		mainLayout.setClickable(true);
		slidingLayout.setScrollEvent(mainLayout);
		menuListAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item_1,
				menuItems);
		menuList.setAdapter(menuListAdapter);
		menuList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				switch(position){
					case 0:break;
					case 1:break;
					case 2:break;
				}
			}
		});
		
		viewUrl.setText(tip);
		serverSwitch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				Log.d(TAG, ""+ServerService.isRunning());
				if(ServerService.isRunning()){
					stopService();
				}else{
					startService();
				}
			}
		});
		qrScan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(ct,
                        CaptureActivity.class);
				ct.startActivityForResult(intent, SCAN_RESULT_CODE);
			}
		});
		checkInstallation();
		messageHandler = new MessageHandler(this);
		ServerService.setActivityHandler(messageHandler);
		ServerService.setActivity(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		ServerService.registerServiceListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(ServerService.isRunning()){
			stopService();
		}
		ServerService.unregisterServiceListener(this);
	}

	@Override
	public void stopService() {
		Intent serviceIntent = new Intent(this.getApplicationContext(),
				ServerService.class);
		stopService(serviceIntent);
	}

	@Override
	public void startService() {
		if (ServerService.isRunning()) {
			return;
		}

		Intent serviceIntent = new Intent(MainActivity.this,
				ServerService.class);

		startService(serviceIntent);
	}

	@Override
	public void onServiceStart(boolean success) {
		if (success) {
			PawServerService service = ServerService.getService();
			String ip=Utils.getLocalIpAddress();
			if(ip==null){
				ip="127.0.0.1";
			}
			final String url= service.getPawServer().server.protocol
					+ "://" + ip + ":"
					+ service.getPawServer().serverPort;
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					serverSwitch.setBackgroundResource(R.drawable.buttonon);
					viewUrl.setText("请在浏览器中访问下列地址：\n"+url);
					qrScan.setVisibility(View.VISIBLE);
				}
			});
			
		}
		else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					viewUrl.setText("服务器启动失败，请重启应用后重试！\n");
				}
			});
		}

	}
	
	@Override
	public void onServiceStop(boolean success) {
		serverSwitch.setBackgroundResource(R.drawable.buttonooff);
		viewUrl.setText(tip);
		qrScan.setVisibility(View.INVISIBLE);
	}
	
	private void checkInstallation() {
		if(!new File(INSTALL_DIR).exists()) {
			// Create directories
			new File(INSTALL_DIR).mkdirs();
			
			// Files not to overwrite
			HashMap<String, Integer> keepFiles = new HashMap<String, Integer>();
			
			// Extract ZIP file form assets
			try {
				extractZip(getAssets().open("content.zip"),
						INSTALL_DIR, keepFiles);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
			
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCAN_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String encodeResult = data.getStringExtra("RESULT");   
                    Toast.makeText(ct, encodeResult, Toast.LENGTH_SHORT);
                }
                break; 
        }
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(ServerService.isRunning()){
			moveTaskToBack(false);
		}else{
			this.finish();
		}
	}
}