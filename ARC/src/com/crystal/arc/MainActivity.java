package com.crystal.arc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.crystal.arc.ui.AboutMeFragment;
import com.crystal.arc.ui.ServerControlFragment;
import com.crystal.arc.ui.SettingFragment;
import com.crystal.arc.ui.SlidingLayout;
import com.crystal.arc.ui.UserManageFragment;
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
	public static final int SCAN_RESULT_CODE = 2015;
	private static String[] menuItems = { "用户管理", "设置", "关于" };
	private static String tip="请点击按钮启动服务器";
	private static Context ct;
	
	private FragmentManager fragmentManager;  
	private ServerControlFragment serverControlFragment;
	private UserManageFragment userManageFragment;
	private SettingFragment settingFragment;
	private AboutMeFragment aboutMeFragment;
	
	private SlidingLayout slidingLayout;
	private LinearLayout mainLayout;
	private ListView menuList;

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
		setContentView(R.layout.main);
		ct=this.getApplicationContext();
		fragmentManager=((FragmentActivity)ct).getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		serverControlFragment=new ServerControlFragment();
		ft.replace(R.id.main_layout,serverControlFragment);
		ft.commit();
		serverControlFragment.setUrl(tip);

		slidingLayout = (SlidingLayout) findViewById(R.id.slidingLayout);
		mainLayout=(LinearLayout) findViewById(R.id.main_layout);
		menuList=(ListView) findViewById(R.id.menulist);
		
		mainLayout.setClickable(true);
		slidingLayout.setScrollEvent(mainLayout);
		menuListAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item_1,
				menuItems);
		menuList.setAdapter(menuListAdapter);
		menuList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					setSelect(0);
					break;
				case 1:
					setSelect(1);
					break;
				case 2:
					setSelect(2);
					break;
				case 3:
					setSelect(3);
					break;
				}
			}
		});
		checkInstallation();
		messageHandler = new MessageHandler(this);
		ServerService.setActivityHandler(messageHandler);
		ServerService.setActivity(this);
	}

	protected void setSelect(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();  
        hideFragments(transaction);  
        switch (index) {  
        case 0:  
            if (serverControlFragment == null) {  
            	serverControlFragment = new ServerControlFragment();  
                transaction.add(R.id.main_layout, serverControlFragment);  
            } else {  
                transaction.show(serverControlFragment);  
            }  
            break;  
        case 1:  
            if (userManageFragment == null) {  
            	userManageFragment = new UserManageFragment();  
                transaction.add(R.id.main_layout, userManageFragment);  
            } else {  
                transaction.show(userManageFragment);  
            }  
            break;  
        case 2:   
            if (settingFragment == null) {  
            	settingFragment = new SettingFragment();  
                transaction.add(R.id.main_layout, settingFragment);  
            } else {  
                transaction.show(settingFragment);  
            }  
            break;  
        case 3:  
        default:  
            if (aboutMeFragment == null) {  
            	aboutMeFragment = new AboutMeFragment();  
                transaction.add(R.id.main_layout, aboutMeFragment);  
            } else {  
                transaction.show(aboutMeFragment);  
            }  
            break;  
        }  
        transaction.commit(); 
        slidingLayout.scrollToRightLayout();
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (serverControlFragment != null) {  
            transaction.hide(serverControlFragment);  
        }  
        if (userManageFragment != null) {  
            transaction.hide(userManageFragment);  
        }  
        if (settingFragment != null) {  
            transaction.hide(settingFragment);  
        }  
        if (aboutMeFragment != null) {  
            transaction.hide(aboutMeFragment);  
        }  
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
					serverControlFragment.switchButton(true);
					serverControlFragment.setUrl("请在浏览器中访问下列地址：\n"+url);
					serverControlFragment.switchQRScan(true);
				}
			});
			
		}
		else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					serverControlFragment.setUrl("服务器启动失败，请重启应用后重试！\n");
				}
			});
		}

	}
	
	@Override
	public void onServiceStop(boolean success) {
		serverControlFragment.switchButton(false);
		serverControlFragment.setUrl(tip);
		serverControlFragment.switchQRScan(false);
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