package com.hrpf.fsclock.fragment;

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
import android.widget.Toast;

import com.hrpf.fsclock.customizeview.AnalogClockView;
import com.hrpf.fsclock.R;

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
    private int BACK_COLOR = Color.BLACK;
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
        rootview.setBackgroundColor(BACK_COLOR);//设置背景颜色
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
                Toast.makeText(getContext(), "部分设置重启APP后生效", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(), "部分设置重启APP后生效", Toast.LENGTH_LONG).show();
            }
        });
        dialogInstance = settingsDialogWindow.create();
        dialogInstance.show();
    }
    /*
    从view获取数据并显示于对话框中
     */
    private void setDialogValue(){
        EditText back_color = settingsView.findViewById(R.id.back_color);
        back_color.setText(String.format("#%06X", (0xFFFFFF & BACK_COLOR)));

        EditText dial_color = settingsView.findViewById(R.id.dial_color);
        dial_color.setText(String.format("#%06X", (0xFFFFFF & clockView.getmCircleColor())));

        Switch Is_DIAL_SOLID = settingsView.findViewById(R.id.is_dial_solid);
        Is_DIAL_SOLID.setChecked(clockView.getIs_DIAL_SOLID());

        Switch DISP_NUM = settingsView.findViewById(R.id.disp_num);
        DISP_NUM.setChecked(clockView.getDISP_NUM());

        EditText mark_width = settingsView.findViewById(R.id.mark_width);
        mark_width.setText(Integer.toString(clockView.getMARK_WIDTH()));

        EditText dial_num = settingsView.findViewById(R.id.dial_num_radius);
        dial_num.setText(Integer.toString(clockView.getNUM_RADIUS()));

        EditText num_size = settingsView.findViewById(R.id.num_size);
        num_size.setText(Integer.toString(clockView.getNUM_SIZE()));

        EditText num_color = settingsView.findViewById(R.id.num_color);
        num_color.setText(String.format("#%06X", (0xFFFFFF & clockView.getNumberColor())));

        EditText mark_length = settingsView.findViewById(R.id.mark_length);
        mark_length.setText(Integer.toString(clockView.getMARK_LENGTH()));

        EditText mark_gap = settingsView.findViewById(R.id.mark_gap);
        mark_gap.setText(Integer.toString(clockView.getMARK_GAP()));

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

        EditText pointer_hour_width = settingsView.findViewById(R.id.pointer_hour_width);
        pointer_hour_width.setText(Integer.toString(clockView.getHOUR_LINE_WIDTH()));

        EditText pointer_min_width = settingsView.findViewById(R.id.pointer_min_width);
        pointer_min_width.setText(Integer.toString(clockView.getMINUTE_LINE_WIDTH()));

        EditText pointer_sec_width = settingsView.findViewById(R.id.pointer_sec_width);
        pointer_sec_width.setText(Integer.toString(clockView.getSECOND_LINE_WIDTH()));

        Switch draw_center_circle = settingsView.findViewById(R.id.draw_center_circle);
        draw_center_circle.setChecked(clockView.getisDrawCenterCircle());
    }
    /*
    从对话框中获取数据保存于sharedpreference中
     */
    private void saveDialogValue(){
        EditText back_color = settingsView.findViewById(R.id.back_color);
        editor.putString("backColor", back_color.getText().toString());

        EditText dial_color = settingsView.findViewById(R.id.dial_color);
        editor.putString("mCircleColor", dial_color.getText().toString());

        Switch Is_DIAL_SOLID = settingsView.findViewById(R.id.is_dial_solid);
        editor.putBoolean("Is_DIAL_SOLID", Is_DIAL_SOLID.isChecked());

        Switch DISP_NUM = settingsView.findViewById(R.id.disp_num);
        editor.putBoolean("DISP_NUM", DISP_NUM.isChecked());

        EditText mark_width = settingsView.findViewById(R.id.mark_width);
        editor.putInt("MARK_WIDTH", Integer.parseInt(mark_width.getText().toString()));

        EditText num_radius = settingsView.findViewById(R.id.dial_num_radius);
        editor.putInt("NUM_RADIUS", Integer.parseInt(num_radius.getText().toString()));

        EditText num_size = settingsView.findViewById(R.id.num_size);
        editor.putInt("NUM_SIZE", Integer.parseInt(num_size.getText().toString()));

        EditText num_color = settingsView.findViewById(R.id.num_color);
        editor.putString("numberColor", num_color.getText().toString());

        EditText mark_length = settingsView.findViewById(R.id.mark_length);
        editor.putInt("MARK_LENGTH", Integer.parseInt(mark_length.getText().toString()));

        EditText mark_gap = settingsView.findViewById(R.id.mark_gap);
        editor.putInt("MARK_GAP", Integer.parseInt(mark_gap.getText().toString()));

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

        EditText pointer_hour_width = settingsView.findViewById(R.id.pointer_hour_width);
        editor.putInt("HOUR_LINE_WIDTH", Integer.parseInt(pointer_hour_width.getText().toString()));

        EditText pointer_min_width = settingsView.findViewById(R.id.pointer_min_width);
        editor.putInt("MINUTE_LINE_WIDTH", Integer.parseInt(pointer_min_width.getText().toString()));

        EditText pointer_sec_width = settingsView.findViewById(R.id.pointer_sec_width);
        editor.putInt("SECOND_LINE_WIDTH", Integer.parseInt(pointer_sec_width.getText().toString()));

        Switch draw_center_circle = settingsView.findViewById(R.id.draw_center_circle);
        editor.putBoolean("isDrawCenterCircle", draw_center_circle.isChecked());

        editor.apply();
        setAllSettings();
    }
    /*
    每次绘制前设置值
     */
    private void setAllSettings(){
        BACK_COLOR = Color.parseColor(preferences.getString("backColor", "#000000"));
        clockView.setmCircleColor(Color.parseColor(preferences.getString("mCircleColor", "#FFFFFF")));
        clockView.setIs_DIAL_SOLID(preferences.getBoolean("Is_DIAL_SOLID", false));
        clockView.setDISP_NUM(preferences.getBoolean("DISP_NUM", true));
        clockView.setMARK_WIDTH(preferences.getInt("MARK_WIDTH", 16));
        clockView.setMARK_LENGTH(preferences.getInt("MARK_LENGTH", 20));
        clockView.setNUM_RADIUS(preferences.getInt("NUM_RADIUS", 300));
        clockView.setNumberColor(Color.parseColor(preferences.getString("numberColor", "#DDDDDD")));
        clockView.setNUM_SIZE(preferences.getInt("NUM_SIZE", 120));
        clockView.setNumberColor(Color.parseColor(preferences.getString("numberColor", "#DDDDDD")));
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
    sharedpreference不存在时才执行
     */
    private void createDefaultSharedPreferences(){
        editor.putBoolean("emptySP", false);
        editor.putString("backColor", "#000000");
        editor.putString("mCircleColor", "#000000");
        editor.putBoolean("Is_DIAL_SOLID", false);
        editor.putBoolean("DISP_NUM", true);
        editor.putInt("MARK_WIDTH", 24);
        editor.putInt("MARK_LENGTH", 24);
        editor.putInt("NUM_RADIUS", 300);
        editor.putInt("NUM_SIZE", 120);
        editor.putString("numberColor", "#DDDDDD");
        editor.putString("mQuarterMarkColor","#EBEBEB");
        editor.putString("mMinuteMarkColor", "#A0A0A0");
        editor.putInt("MARK_GAP", 16);
        editor.putString("mHourColor", "#FFFFFF");
        editor.putString("mMinuteColor", "#DDDDDD");
        editor.putString("mSecondColor", "#FF0000");
        editor.putInt("HOUR_LINE_WIDTH", 24);
        editor.putInt("MINUTE_LINE_WIDTH", 12);
        editor.putInt("SECOND_LINE_WIDTH", 4);
        editor.putBoolean("isDrawCenterCircle", false);
        editor.apply();
    }
}