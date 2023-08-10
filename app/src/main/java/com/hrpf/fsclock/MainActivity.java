package com.hrpf.fsclock;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextClock;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "开机自启成功", Toast.LENGTH_LONG).show();
        TextClock hh = (TextClock) findViewById(R.id.hh);
        TextClock aa = (TextClock) findViewById(R.id.aa);
        TextClock mm = (TextClock) findViewById(R.id.mm);
        TextClock ss = (TextClock) findViewById(R.id.ss);

        AssetManager assets = getAssets();
        Typeface typeface = Typeface.createFromAsset(assets, "BebasNeueRegular.ttf");
        hh.setTypeface(typeface);
        mm.setTypeface(typeface);
        ss.setTypeface(typeface);

        // FIXED: 在不同时间格式下显示不对
        boolean is24 = DateFormat.is24HourFormat(this);
        if(is24){
            hh.setFormat24Hour("hh");
            aa.setFormat24Hour("aa");
            mm.setFormat24Hour("mm");
            ss.setFormat24Hour("ss");
        } else {
            hh.setFormat12Hour("hh");
            aa.setFormat12Hour("aa");
            mm.setFormat12Hour("mm");
            ss.setFormat12Hour("ss");
        }
    }

    public void startTestActivity(View view){
        Intent intent = new Intent(this, ViewPagerTest.class);
        startActivity(intent);
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

    private void hideSystemUI() {//隐藏系统UI
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
