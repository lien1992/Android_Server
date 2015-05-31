
package com.crystal.arc.zxing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.util.Log;

public class CameraTest {
    private static int index=0;
    private static final String TAG="CameraTest";
    public static void saveBitmap(final Bitmap bm) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Log.e(TAG, "保存图片");
                File f = new File("/sdcard/test/test"+index+".png");
                index++;
                if (f.exists()) {
                    f.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    Log.i(TAG, "已经保存");
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        }.start();
        
    }

}
