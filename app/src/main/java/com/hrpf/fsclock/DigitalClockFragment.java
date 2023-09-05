package com.hrpf.fsclock;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DigitalClockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DigitalClockFragment extends Fragment {

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DigitalClockFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DigitalClockFragment.
     */
    public static DigitalClockFragment newInstance(String param1, String param2) {
        DigitalClockFragment fragment = new DigitalClockFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_digital_clock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextClock hh = getView().findViewById(R.id.hh);
        TextClock aa = getView().findViewById(R.id.aa);
        TextClock mm = getView().findViewById(R.id.mm);
        TextClock ss = getView().findViewById(R.id.ss);

        AssetManager assets = getResources().getAssets();
        Typeface typeface = Typeface.createFromAsset(assets, "BebasNeueRegular.ttf");
        hh.setTypeface(typeface);
        mm.setTypeface(typeface);
        ss.setTypeface(typeface);

        // FIXED: 在不同时间格式下显示不对
        boolean is24 = DateFormat.is24HourFormat(getContext());
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
}