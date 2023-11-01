package com.hrpf.fsclock;

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

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
public class TestView extends View  {
    private Paint numberPaint;
    private Canvas numberCanvas;
    public TestView(Context context) {
        super(context);
        numberPaint = new Paint();
        numberPaint.setAntiAlias(true);
        numberPaint.setColor(Color.GREEN);
        numberPaint.setStyle(Paint.Style.FILL);
        numberPaint.setTextSize(20);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        numberCanvas = new Canvas();
    }
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        // 绘制数字
        String num = "8";
        numberCanvas.drawText(num, 50, 50, numberPaint);
    }
}
