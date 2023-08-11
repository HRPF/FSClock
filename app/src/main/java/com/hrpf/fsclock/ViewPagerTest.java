package com.hrpf.fsclock;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerTest extends FragmentActivity{

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager2);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);

        // 生成Fragment列表数据源
        fragmentList = new ArrayList<>();
        DigitalClockFragment fgm1 = DigitalClockFragment.newInstance("fgm1", "");
        AnalogClockFragment fgm2 = AnalogClockFragment.newInstance("fgm2", "");
        BlankFragment fgm3 = BlankFragment.newInstance("fgm3", "");
        fragmentList.add(fgm1);
        fragmentList.add(fgm2);
        fragmentList.add(fgm3);

        // Viewpager2将根据适配器提供的fragment数量创建fragment
        pagerAdapter = new ScreenSlidePagerAdapter(this, fragmentList);
        viewPager.setAdapter(pagerAdapter);
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
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        // 数据，即多个Fragment
        private List<Fragment> mFragmentList;

        // 构造方法
        // 从参数接收并初始化Fragment列表
        public ScreenSlidePagerAdapter(FragmentActivity fa, List fgmList) {
            super(fa);
            this.mFragmentList = fgmList;
        }

        // 根据索引找到对应的fragment
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
