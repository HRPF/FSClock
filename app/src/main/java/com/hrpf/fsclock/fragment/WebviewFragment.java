package com.hrpf.fsclock.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hrpf.fsclock.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebviewFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String URL = "https://cn.bing.com/";
    private View settingsView;
    private ImageButton settingsButton;
    private ImageButton refreshButton;
    private AlertDialog dialogInstance;
    private WebView webView;

    public WebviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WebviewFragment.
     */
    public static WebviewFragment newInstance(String param1, String param2) {
        WebviewFragment fragment = new WebviewFragment();
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
            mParam2 = getArguments().getString(ARG_PARAM1);
        }
        preferences = requireActivity().getSharedPreferences("web_settings_sp", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_webview, container, false);
        webView = rootview.findViewById(R.id.remote_sensor);
        editor.putBoolean("emptySP", false);
        if(preferences.contains("emptySP")){
            this.URL = preferences.getString("URL", "https://cn.bing.com/");
        } else {
            editor.putBoolean("emptySP", false);
            editor.putString("URL", "https://cn.bing.com/");
            editor.apply();
        }
        // 直接通过WebView显示网页
        webView.setWebViewClient(new WebViewClient());
        webView.setInitialScale(100);
        // 加载URL
        webView.loadUrl(URL);
        // 启用javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingsView = getLayoutInflater().inflate(R.layout.settings_web, null, false);
        settingsButton = getView().findViewById(R.id.bt_webview_settings);
        settingsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSettings();
                    }
                }
        );
        refreshButton = getView().findViewById(R.id.bt_webview_refresh);
        refreshButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        webView.reload();
                    }
                }
        );
    }

    private void onSettings() {
        if (dialogInstance != null) {
            dialogInstance.show();
            return;
        }
        if (settingsView.getParent() != null) {
            ((ViewGroup) settingsView.getParent()).removeView(settingsView);
        }
        AlertDialog.Builder settingsDialogWindow = new AlertDialog.Builder(getActivity(), R.style.CustomDialogStyle);
        settingsDialogWindow.setView(settingsView);
        settingsDialogWindow.setTitle("监控设置");
        EditText url_text = settingsView.findViewById(R.id.url_text);
        url_text.setText(URL);
        settingsDialogWindow.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 响应保存按钮,刷新webview
                EditText url_text = settingsView.findViewById(R.id.url_text);
                editor.putString("URL", url_text.getText().toString());
                webView.loadUrl(url_text.getText().toString());
                editor.apply();
            }
        });
        settingsDialogWindow.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 响应取消按钮
                dialogInstance.dismiss();
                dialogInstance = null;
            }
        });
        dialogInstance = settingsDialogWindow.create();
        dialogInstance.show();
    }
}