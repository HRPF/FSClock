package com.hrpf.fsclock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AnalogClockFragment() {
        // Required empty public constructor
    }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analog_clock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingsView = getLayoutInflater().inflate(R.layout.settings_analog, null, false);
        settingsButton = getView().findViewById(R.id.bt_analog_settings);
        Log.i("null", settingsButton.toString());
        settingsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 在这里打开对话框
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
                        settingsDialogWindow.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 响应保存按钮
                                // TODO 尝试更新表盘颜色
                                // TODO 存储设置
                            }
                        });
                        settingsDialogWindow.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 响应取消按钮
                            }
                        });
                        dialogInstance = settingsDialogWindow.create();
                        dialogInstance.show();
                    }
                }
        );
    }
}