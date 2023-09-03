package com.hrpf.fsclock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalogClockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalogClockFragment extends Fragment {

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View settingsView;
    private ImageButton settingsButton;
    private AlertDialog dialogInstance;
    private AnalogClockView clockView;
    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public AnalogClockFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return 一个AnalogClockFragment的新实例.
     */
    // Rename and change types and number of parameters
    public static AnalogClockFragment newInstance(String param1, String param2) {
        AnalogClockFragment fragment = new AnalogClockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) throws NullPointerException{
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        preferences = requireActivity().getSharedPreferences("analog_settings_sp", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_analog_clock, container, false);
        clockView = rootview.findViewById(R.id.analog_clock);
        if(preferences.contains("emptySP")){
            // 设置所有
            setAllSettings();
        } else {
            // 使用默认数据创建SP
            createDefaultSharedPreferences();
        }
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingsView = getLayoutInflater().inflate(R.layout.settings_analog, null, false);
        settingsButton = getView().findViewById(R.id.bt_analog_settings);
        settingsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSettings();
                    }
                }
        );
    }
    // 打开设置对话框
    private void onSettings(){
        // https://blog.csdn.net/niohandsome/article/details/53354528
        Log.i("null", "点击设置");
        if (dialogInstance != null) {
            dialogInstance.show();
            return;
        }
        if (settingsView.getParent() != null) {
            ((ViewGroup) settingsView.getParent()).removeView(settingsView);
        }
        AlertDialog.Builder settingsDialogWindow = new AlertDialog.Builder(getActivity());
        settingsDialogWindow.setView(settingsView);
        settingsDialogWindow.setTitle("自定义模拟时钟");
        setDialogValue();
        settingsDialogWindow.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 响应保存按钮
                saveDialogValue();
            }
        });
        settingsDialogWindow.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 响应取消按钮
                dialogInstance.dismiss();
                dialogInstance = null;
            }
        });
        settingsDialogWindow.setNeutralButton("恢复默认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 恢复默认值
                createDefaultSharedPreferences();
                setDialogValue();
            }
        });
        dialogInstance = settingsDialogWindow.create();
        dialogInstance.show();
    }
    private void setDialogValue(){
        EditText dial_color = settingsView.findViewById(R.id.dial_color);
        dial_color.setText(preferences.getString("mCircleColor", "#FFFFFF"));

        EditText mark_width = settingsView.findViewById(R.id.mark_width);
        mark_width.setText(Integer.toString(clockView.getMARK_WIDTH()));

        EditText mark_length = settingsView.findViewById(R.id.mark_length);
        mark_length.setText(Integer.toString(clockView.getMARK_LENGTH()));

        EditText mark_quarter_color = settingsView.findViewById(R.id.mark_quarter_color);
        mark_quarter_color.setText(String.format("#%06X", (0xFFFFFF & clockView.getmQuarterMarkColor())));

        EditText mark_min_color = settingsView.findViewById(R.id.mark_min_color);
        mark_min_color.setText(String.format("#%06X", (0xFFFFFF & clockView.getmMinuteMarkColor())));

        EditText pointer_hour_color = settingsView.findViewById(R.id.pointer_hour_color);
        pointer_hour_color.setText(String.format("#%06X", (0xFFFFFF & clockView.getmHourColor())));

        EditText pointer_min_color = settingsView.findViewById(R.id.pointer_min_color);
        pointer_min_color.setText(String.format("#%06X", (0xFFFFFF & clockView.getmMinuteColor())));

        EditText pointer_sec_color = settingsView.findViewById(R.id.pointer_sec_color);
        pointer_sec_color.setText(String.format("#%06X", (0xFFFFFF & clockView.getmSecondColor())));

        Switch draw_center_circle = settingsView.findViewById(R.id.draw_center_circle);
        draw_center_circle.setChecked(clockView.getisDrawCenterCircle());
    }
    private void saveDialogValue(){
        EditText dial_color = settingsView.findViewById(R.id.dial_color);
        editor.putString("mCircleColor", dial_color.getText().toString());
        Log.i("DEBUG", dial_color.getText().toString());
        Log.i("DEBUG", preferences.getString("mCircleColor", "#FFFFFF"));

        EditText mark_width = settingsView.findViewById(R.id.mark_width);
        editor.putInt("MARK_WIDTH", Integer.parseInt(mark_width.getText().toString()));

        EditText mark_length = settingsView.findViewById(R.id.mark_length);
        editor.putInt("MARK_LENGTH", Integer.parseInt(mark_length.getText().toString()));

        EditText mark_quarter_color = settingsView.findViewById(R.id.mark_quarter_color);
        editor.putString("mQuarterMarkColor", mark_quarter_color.getText().toString());

        EditText mark_min_color = settingsView.findViewById(R.id.mark_min_color);
        editor.putString("mMinuteMarkColor", mark_min_color.getText().toString());

        EditText pointer_hour_color = settingsView.findViewById(R.id.pointer_hour_color);
        editor.putString("mHourColor", pointer_hour_color.getText().toString());

        EditText pointer_min_color = settingsView.findViewById(R.id.pointer_min_color);
        editor.putString("mMinuteColor", pointer_min_color.getText().toString());

        EditText pointer_sec_color = settingsView.findViewById(R.id.pointer_sec_color);
        editor.putString("mSecondColor", pointer_sec_color.getText().toString());

//        Switch draw_center_circle = settingsView.findViewById(R.id.draw_center_circle);
//        editor.putString("isDrawCenterCircle", draw_center_circle.get);
        editor.apply();
    }
    /*
    每次绘制前设置值
     */
    private void setAllSettings(){
        clockView.setmCircleColor(Color.parseColor(preferences.getString("mCircleColor", "#FFFFFF")));
        clockView.setMARK_WIDTH(preferences.getInt("MARK_WIDTH", 16));
        clockView.setMARK_LENGTH(preferences.getInt("MARK_LENGTH", 20));
        clockView.setmQuarterMarkColor(Color.parseColor(preferences.getString("mQuarterMarkColor", "#B5B5B5")));
        clockView.setmMinuteMarkColor(Color.parseColor(preferences.getString("mMinuteMarkColor", "#EBEBEB")));
        clockView.setMARK_GAP(preferences.getInt("MARK_GAP", 16));
        clockView.setmHourColor(Color.parseColor(preferences.getString("mHourColor", "#000000")));
        clockView.setmMinuteColor(Color.parseColor(preferences.getString("mMinuteColor", "#000000")));
        clockView.setmSecondColor(Color.parseColor(preferences.getString("mSecondColor", "#FF0000")));
        clockView.setHOUR_LINE_WIDTH(preferences.getInt("HOUR_LINE_WIDTH", 10));
        clockView.setMINUTE_LINE_WIDTH(preferences.getInt("MINUTE_LINE_WIDTH", 6));
        clockView.setSECOND_LINE_WIDTH(preferences.getInt("SECOND_LINE_WIDTH", 4));
        clockView.setisDrawCenterCircle(preferences.getBoolean("isDrawCenterCircle", false));
    }
    /*
    在sharedpreference不存在时才执行
     */
    private void createDefaultSharedPreferences(){
        editor.putBoolean("emptySP", false);
        editor.putString("mCircleColor", "#FFFFFF");
        editor.putInt("MARK_WIDTH", 16);
        editor.putInt("MARK_LENGTH", 20);
        editor.putString("mQuarterMarkColor","#B5B5B5");
        editor.putString("mMinuteMarkColor", "#EBEBEB");
        editor.putInt("MARK_GAP", 16);
        editor.putString("mHourColor", "#000000");
        editor.putString("mMinuteColor", "#000000");
        editor.putString("mSecondColor", "#FF0000");
        editor.putInt("HOUR_LINE_WIDTH", 10);
        editor.putInt("MINUTE_LINE_WIDTH", 6);
        editor.putInt("SECOND_LINE_WIDTH", 4);
        editor.putBoolean("isDrawCenterCircle", false);
        editor.apply();
    }
}