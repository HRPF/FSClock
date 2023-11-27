package com.hrpf.fsclock.customizeview;

import android.content.Context;
import android.view.View;
import android.content.Context;
import android.icu.text.RelativeDateTimeFormatter;
import android.util.Log;
import android.view.View;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.Window;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
public class TestView extends View  {
    private Paint numberPaint;

    public TestView(Context context) {
        super(context);
        numberPaint = new Paint();
        numberPaint.setAntiAlias(true);
        numberPaint.setColor(Color.GREEN);
        numberPaint.setStyle(Paint.Style.FILL);
        numberPaint.setTextSize(20);
        Log.i("DEBUG", "构造函数1");
    }

    // 这个构造函数在XML布局文件中使用.
    // AttributeSet包含了XML中定义的所有属性，这允许您在XML布局文件中为视图设置自定义属性。
    // 在这个构造函数中，您可以读取XML中定义的属性值，以便根据这些属性值进行初始化。
    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        numberPaint = new Paint();
        numberPaint.setAntiAlias(true);
        numberPaint.setColor(Color.GREEN);
        numberPaint.setStyle(Paint.Style.FILL);
        numberPaint.setTextSize(200);
        Log.i("DEBUG", "构造函数1");
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        // 绘制数字
        String num = "8";
        canvas.drawText(num, 500, 500, numberPaint);
    }
}
