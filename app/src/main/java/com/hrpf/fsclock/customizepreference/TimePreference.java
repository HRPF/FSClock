package com.hrpf.fsclock.customizepreference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

public class TimePreference extends DialogPreference {
    public TimePreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TimePreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimePreference(@NonNull Context context) {
        super(context);
    }

    protected View onCreateDialogView() {
        TimePicker timePicker = new TimePicker(getContext());
        // Configure your time picker here
        return timePicker;
    }

}
