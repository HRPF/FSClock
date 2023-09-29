package com.hrpf.fsclock.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class ScreenControlService extends Service {
    public ScreenControlService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // FIXME 使用WAKE_LOCK保持屏幕常亮,未验证可用
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        boolean action = intent.getBooleanExtra("screenStatus", false);
        if ( action ) {
            Log.d("DEBUG", "打开屏幕");
            wakeLock.acquire();
            Handler handlerThree=new Handler(Looper.getMainLooper());
            handlerThree.post(new Runnable(){
                public void run(){
                    Toast.makeText(getApplicationContext() ,"打开屏幕",Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Log.d("DEBUG", "关闭屏幕");
            if (wakeLock.isHeld()){
                wakeLock.release();
            } else {
                Log.d("DEBUG", "wakeLock未被请求");
            }
            Handler handlerThree=new Handler(Looper.getMainLooper());
            handlerThree.post(new Runnable(){
                public void run(){
                    Toast.makeText(getApplicationContext() ,"关闭屏幕",Toast.LENGTH_LONG).show();
                }
            });
        }
        // 返回START_STICKY以确保服务在被杀死后会重新启动
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}