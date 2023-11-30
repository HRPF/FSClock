package com.hrpf.fsclock.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DeviceManagerReceiver extends DeviceAdminReceiver {
    private void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context,
                "激活设备管理器");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context,
                "取消激活设备管理器");
    }
}
