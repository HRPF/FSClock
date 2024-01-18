package com.hrpf.fsclock.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hrpf.fsclock.R;
import com.arcsoft.face.FaceEngine;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArcFaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArcFaceFragment extends Fragment {

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ArcFaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    public static ArcFaceFragment newInstance(String param1, String param2) {
        ArcFaceFragment fragment = new ArcFaceFragment();
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
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    // 在View创建好后的动作,设置文本
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button active = view.findViewById(R.id.active_arc);
        active.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activateArcFace();
                    }
                }
        );
    }

    public void activateArcFace(){
        String APP_ID = "tb6TR1NdDpa3oetpF4jeh3YbisChjD1DPWc9C4YZw4k";
        String SDK_KEY = "DwUCQuCR4zH7AMuXg5b7G1b7CQrX4BRLQDTMvr1WkQkC";
        int errorCode = FaceEngine.activeOnline(getContext(), APP_ID, SDK_KEY);
        Toast.makeText(getContext(), Integer.toString(errorCode), Toast.LENGTH_LONG).show();
    }
}