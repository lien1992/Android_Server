package com.crystal.arc.zxing;
import java.io.IOException;
import java.util.List;

import com.crystal.arc.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraTestActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG="CameraTestActivity";
    private SurfaceHolder surfaceHolder;
    private Camera mCamera;
    //private SurfaceView display;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        //display=surfaceView;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        try {
            Log.d(TAG, "打开相机");
            mCamera = Camera.open();
        }
        catch (Exception e){
            Log.d(TAG, "Error starting camera: " + e.getMessage());
        }
        Log.d(TAG, "已经打开相机");
        Camera.Parameters parameters = mCamera.getParameters();
        Point size=findBestPreviewSizeValue();
        parameters.setPreviewSize(size.x, size.y); // 设置
        
        Camera.Size beforeSize = mCamera.getParameters().getPreviewSize();
        Log.d(TAG, "BeforeSize" + beforeSize.width+"*"+beforeSize.height);
        Log.d(TAG, "willset" + parameters.getPreviewSize().width+"*"+parameters.getPreviewSize().height);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
        Camera.Size afterSize = mCamera.getParameters().getPreviewSize();
        Log.d(TAG, "AfterSize" + afterSize.width+"*"+afterSize.height);
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
          Log.d(TAG, "surfaceChanged");
        // TODO Auto-generated method stub
          if (surfaceHolder == null){
            return;
          }

          try {
              mCamera.stopPreview();
          } catch (Exception e){
            // ignore: tried to stop a non-existent preview
          }

          
          try {
              mCamera.setPreviewDisplay(surfaceHolder);
              mCamera.startPreview();

          } catch (Exception e){
              Log.d(TAG, "Error starting camera preview: " + e.getMessage());
          }
    }
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        Log.d(TAG, "surfaceCreated");
        // TODO Auto-generated method stub
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.d(TAG, "surfaceDestroyed");
        // TODO Auto-generated method stub
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
        mCamera = null;
    }
/*    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }*/
    
    private  Point findBestPreviewSizeValue() {
        List<Size> sizes=mCamera.getParameters().getSupportedPreviewSizes();
        WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        String s="Sizes:";
        int bestX = 0;
        int bestY = 0;
        int diff = Integer.MAX_VALUE;
        int x,y;
        int newX;
        int newY;
        if(display.getWidth()>display.getHeight()){
            x=display.getWidth();
            y=display.getHeight();
        }else{
            x=display.getHeight();
            y=display.getWidth();
        }
        for (Size previewSize : sizes) {
            newX=previewSize.width;
            newY=previewSize.height;
            s+=newX+"*"+newY+"/";
            if ((newX <= x) && (newY <= y)) {
                int newDiff = Math.abs(newX - x) + Math.abs(newY - y);
                if (newDiff == 0) {
                    bestX = newX;
                    bestY = newY;
                    break;
                } else if (newDiff < diff) {
                    bestX = newX;
                    bestY = newY;
                    diff = newDiff;
                }
            }
        }
        if (bestX > 0 && bestY > 0) {
            Log.d(TAG, "Display:"+x+"*"+y+s+"Best:"+bestX+"*"+bestY);
            return new Point(bestX, bestY);
        }
        return null;
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        // TODO Auto-generated method stub
        super.onBackPressed();
    }
    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        // TODO Auto-generated method stub
        super.onRestart();
    }
    
    
    
    
}