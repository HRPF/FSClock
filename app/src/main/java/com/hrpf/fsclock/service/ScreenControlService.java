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
            policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            componentName = new ComponentName(this, DeviceManagerReceiver.class);
            if(policyManager.isAdminActive(componentName)){
                policyManager.lockNow();
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}