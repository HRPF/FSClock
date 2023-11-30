package com.hrpf.fsclock;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import com.hrpf.fsclock.fragment.AnalogClockFragment;
import com.hrpf.fsclock.fragment.AppSettingsFragment;
import com.hrpf.fsclock.fragment.DigitalClockFragment;
import com.hrpf.fsclock.fragment.WebviewFragment;
import com.hrpf.fsclock.service.ScreenControlService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewPager extends FragmentActivity{

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    // 生成Fragment列表数据源
    private List<Fragment> fragmentList;
    private Context appContext;
    private SharedPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.viewpager2);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);

        // 获取设置DefaultSharedPreferences
        appPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // 生成Fragment列表数据源
        fragmentList = new ArrayList<>();
        AppSettingsFragment fgm0 = AppSettingsFragment.newInstance();
        DigitalClockFragment fgm1 = DigitalClockFragment.newInstance("fgm1", "");
        AnalogClockFragment fgm2 = AnalogClockFragment.newInstance("fgm2", "");
        // BlankFragment test_fgm = BlankFragment.newInstance("Test Fragment", "");

        fragmentList.add(fgm0);
        fragmentList.add(fgm1);
        fragmentList.add(fgm2);
        // fragmentList.add(test_fgm);

        boolean enable_remote_sensor = appPreferences.getBoolean("enable_remote_sensor", false);
        if(enable_remote_sensor) {
            WebviewFragment fgm4 = WebviewFragment.newInstance("web", "https://cn.bing.com/");
            fragmentList.add(fgm4);
        }

        // Viewpager2将根据适配器提供的fragment数量创建fragment
        pagerAdapter = new ScreenSlidePagerAdapter(this, fragmentList);
        viewPager.setAdapter(pagerAdapter);
        // 切换到默认页
        switch (appPreferences.getString("home_screen", "digital_clk")){
            case "digital_clk":
                viewPager.setCurrentItem(1, false);
                break;
            case "analog_clk":
                viewPager.setCurrentItem(2, false);
                break;
            case "remote_sensor":
                if(enable_remote_sensor)
                    viewPager.setCurrentItem(3, false);
                else
                    Toast.makeText(getApplicationContext(), "未启用硬件监控屏幕，当前主屏幕设置不生效", Toast.LENGTH_LONG).show();
                break;
        }
        boolean screen_time_ctrl = appPreferences.getBoolean("on_off_timer", false);
        if(screen_time_ctrl) {
            setScreenControlAlarm();
        }
    }

    private void setScreenControlAlarm(){
        // 使用AlarmManager定期触发ScreenControlService服务
        // FIXME 第一次启动APP时会同时打开/关闭屏幕
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent offintent = new Intent(this, ScreenControlService.class);
        // false表示灭屏，true表示亮屏
        offintent.putExtra("screenStatus", false);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, offintent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 设置定时任务，这里设置为每天早上7点执行一次
        Calendar calendarNow = Calendar.getInstance();
        Calendar offcalendar = Calendar.getInstance();
        String time_str = appPreferences.getString("screen_off_time", "00:00");
        int hour = Integer.parseInt(time_str.substring(0, 2));
        int min = Integer.parseInt(time_str.substring(3, 5));
        offcalendar.set(Calendar.HOUR_OF_DAY, hour);
        offcalendar.set(Calendar.MINUTE, min);
        if (offcalendar.before(calendarNow)) {
            offcalendar.add(Calendar.DATE, 1);
        }
        long offTime = offcalendar.getTimeInMillis();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, offTime, AlarmManager.INTERVAL_DAY, pendingIntent);

        Intent onintent = new Intent(this, ScreenControlService.class);
        onintent.putExtra("screenStatus", true);
        PendingIntent pendingIntent2 = PendingIntent.getService(this, 1, onintent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar oncalendar = Calendar.getInstance();
        String time_str_on = appPreferences.getString("screen_on_time", "08:00");
        int hour_on = Integer.parseInt(time_str_on.substring(0, 2));
        int min_on = Integer.parseInt(time_str_on.substring(3, 5));
        oncalendar.set(Calendar.HOUR_OF_DAY, hour_on);
        oncalendar.set(Calendar.MINUTE, min_on);
        if (offcalendar.after(calendarNow)) {
            offcalendar.add(Calendar.DATE, 1);
        }
        long onTime = oncalendar.getTimeInMillis();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, onTime, AlarmManager.INTERVAL_DAY, pendingIntent2);

        Toast.makeText(getApplicationContext(),
                "启用屏幕自动开关: "+
                        String.format("%02d", hour) + ":" + String.format("%02d", min) + ", " +
                        String.format("%02d", hour_on) + ":" + String.format("%02d", min_on),
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {//强制横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            hideSystemUI();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (viewPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
//        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        // 数据，即多个Fragment
        private List<Fragment> mFragmentList;

        // 构造方法
        // 从参数接收并初始化Fragment列表
        public ScreenSlidePagerAdapter(FragmentActivity fa, List fgmList) {
            super(fa);
            this.mFragmentList = fgmList;
        }

        // 根据索引找到对应的fragment
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            // 从列表获取fragment数量
            return mFragmentList.size();
        }
    }

    //隐藏系统UI
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
