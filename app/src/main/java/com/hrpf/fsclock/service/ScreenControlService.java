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
        // 返回START_STICKY以确保服务在被杀死后会重新启动
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}