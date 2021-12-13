package com.hrpf.fsclock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class StartReceiver  extends BroadcastReceiver {
    public StartReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //此处及是重启的之后，打开我们app的方法
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent bootIntent= new Intent(context, MainActivity.class);
            bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//非常重要，如果缺少的话，程序将在启动时报错
            //自启动APP（Activity）
            context.startActivity(bootIntent);
        }
    }
}