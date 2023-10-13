package com.hrpf.fsclock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class StartReceiver extends BroadcastReceiver {
    public StartReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //此处及是重启的之后，打开我们app的方法
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            SharedPreferences appPreferences;
            appPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            boolean start_at_boot = appPreferences.getBoolean("start_at_boot", false);
            if (start_at_boot){
                Intent bootIntent= new Intent(context, ViewPager.class);
                //新活动会成为历史栈中的新任务（一组活动）的开始,通常用于具有"launcher"行为的活动
                bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //自启动APP（Activity）
                context.startActivity(bootIntent);
            }
        }
    }
}