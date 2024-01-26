package com.hrpf.fsclock.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;

import com.hrpf.fsclock.receiver.DeviceManagerReceiver;

public class ScreenControlService extends Service {
    private DevicePolicyManager policyManager;
    private ComponentName componentName;

    public ScreenControlService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 返回START_STICKY以确保服务在被杀死后会重新启动
        boolean action = intent.getBooleanExtra("screenStatus", false);
        if(!action){
            Log.i("ScreenControlService", "定时锁屏");
            policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            componentName = new ComponentName(this, DeviceManagerReceiver.class);
            if(policyManager.isAdminActive(componentName)){
                Log.i("ScreenControlService", "设备管理器已激活");
                policyManager.lockNow();
            }
        }
        else {
            Log.i("ScreenControlService", "定时亮屏");
            // TODO: 实现自动亮屏
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::MyWakelockTag");
            wakeLock.acquire(10*60*1000L /*10 minutes*/);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}