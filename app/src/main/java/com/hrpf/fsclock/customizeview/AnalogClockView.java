package com.hrpf.fsclock.customizeview;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

import com.hrpf.fsclock.R;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AnalogClockView extends View {
    private final int DEFAULT_SIZE = 400;//使用wrap_content时默认的尺寸
    private int CIRCLE_WIDTH = 8;//表盘空心状态下外圈宽度
    private boolean is_DIAL_SOLID = false;//设置表盘是否填充
    private boolean DISP_NUM = true;//是否显示数字
    private int MARK_WIDTH = 8;//刻度线宽度
    private int MARK_LENGTH = 20;//刻度线长度
    private int MARK_GAP = 16;//刻度线与圆的间隙
    private int HOUR_LINE_WIDTH = 10;//时针宽度
    private int MINUTE_LINE_WIDTH = 6;//分针宽度
    private int SECOND_LINE_WIDTH = 4;//秒针宽度
    private int centerX;//圆心坐标
    private int centerY;
    private int NUM_RADIUS = 300;//表盘数字中心离圆心的距离
    private int NUM_SIZE = 120;//表盘数字字体大小
    private int radius;//圆半径
    private Paint circlePaint;//圆的画笔
    private Paint markPaint;//刻度线画笔
    private Paint hourPaint;//时针画笔
    private Paint minutePaint;//分针画笔
    private Paint secondPaint;//秒针画笔
    private Paint numberPaint;//数字画笔
    private int hourLineLength;//时针长度
    private int minuteLineLength;//分针长度
    private int secondLineLength;//秒针长度

    private Bitmap hourBitmap;
    private Bitmap minuteBitmap;
    private Bitmap secondBitmap;

    private Canvas hourCanvas;
    private Canvas minuteCanvas;
    private Canvas secondCanvas;

    private int mCircleColor = Color.WHITE;//表盘的颜色
    private int numberColor = Color.GRAY;//表盘数字颜色
    private int mHourColor = Color.GRAY;//时针的颜色
    private int mMinuteColor = Color.GRAY;//分针的颜色
    private int mSecondColor = Color.RED;//秒针的颜色
    private int mQuarterMarkColor = Color.parseColor("#B5B5B5");//一刻钟刻度线的颜色
    private int mMinuteMarkColor = Color.parseColor("#EBEBEB");//分钟刻度线的颜色
    private boolean isDrawCenterCircle = true;//是否绘制3个指针的圆心

    private static HashMap<String, Integer> map;
    private boolean hasDrawnStaticContent = false;
    private Rect textBounds = new Rect();

    //获取时间监听
    private OnCurrentTimeListener onCurrentTimeListener;

    public void setOnCurrentTimeListener(OnCurrentTimeListener onCurrentTimeListener) {
        this.onCurrentTimeListener = onCurrentTimeListener;
    }

    public AnalogClockView(Context context) {
        super(context);
        init();
    }

    public AnalogClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnalogClockView);
        mCircleColor = a.getColor(R.styleable.AnalogClockView_circle_color, Color.WHITE);
        mHourColor = a.getColor(R.styleable.AnalogClockView_hour_color, Color.BLACK);
        mMinuteColor = a.getColor(R.styleable.AnalogClockView_minute_color, Color.BLACK);
        mSecondColor = a.getColor(R.styleable.AnalogClockView_second_color, Color.RED);
        mQuarterMarkColor = a.getColor(R.styleable.AnalogClockView_quarter_mark_color, Color.parseColor("#B5B5B5"));
        mMinuteMarkColor = a.getColor(R.styleable.AnalogClockView_minute_mark_color, Color.parseColor("#EBEBEB"));
        isDrawCenterCircle = a.getBoolean(R.styleable.AnalogClockView_draw_center_circle, false);
        a.recycle();
        init();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        Log.i("INFO", "WindowVisibilityChanged: " + visibility);
        if(visibility == 0) {
            if(is_DIAL_SOLID)
                circlePaint.setStyle(Paint.Style.FILL);
            else
                circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeWidth(CIRCLE_WIDTH);
            circlePaint.setColor(mCircleColor);

            numberPaint.setColor(numberColor);
            numberPaint.setTextSize(NUM_SIZE);

            markPaint.setStrokeWidth(MARK_WIDTH);

            hourPaint.setColor(mHourColor);
            hourPaint.setStrokeWidth(HOUR_LINE_WIDTH);

            minutePaint.setColor(mMinuteColor);
            minutePaint.setStrokeWidth(MINUTE_LINE_WIDTH);

            secondPaint.setColor(mSecondColor);
            secondPaint.setStrokeWidth(SECOND_LINE_WIDTH);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        reMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        // 中心位置
        centerX = width / 2 ;
        centerY = height / 2;
        // 中心距最近边的距离
        radius = Math.min(width, height) / 2;
        // 指针对于上述距离的相对长度
        hourLineLength = radius / 2;
        minuteLineLength = radius * 3 / 4;
        secondLineLength = radius * 3 / 4;

        // 时针
        hourBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        hourCanvas = new Canvas(hourBitmap);

        // 分针
        minuteBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        minuteCanvas = new Canvas(minuteBitmap);

        // 秒针
        secondBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        secondCanvas = new Canvas(secondBitmap);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制圆。半径减去一半的线宽以避免表盘超出边界
        canvas.drawCircle(centerX, centerY, radius - CIRCLE_WIDTH / 2, circlePaint);

        // 绘制数字
        if(DISP_NUM) {
            final String num12 = "12";
            final String num3 = "3";
            final String num6 = "6";
            final String num9 = "9";
            numberPaint.getTextBounds(num12, 0, num12.length(), textBounds);
            float textWidth_2 = textBounds.width();
            float textHeight_2 = textBounds.height();
            canvas.drawText(num12,
                    centerX - textWidth_2 / 2,
                    centerY + textHeight_2 / 2 - NUM_RADIUS,
                    numberPaint);
            numberPaint.getTextBounds(num3, 0, num3.length(), textBounds);
            float textWidth = textBounds.width();
            float textHeight = textBounds.height();
            canvas.drawText(num3,
                    centerX - textWidth / 2 + NUM_RADIUS,
                    centerY + textHeight / 2,
                    numberPaint);
            canvas.drawText(num6,
                    centerX - textWidth / 2,
                    centerY + textHeight / 2 + NUM_RADIUS,
                    numberPaint);
            canvas.drawText(num9,
                    centerX - textWidth / 2 - NUM_RADIUS,
                    centerY + textHeight / 2,
                    numberPaint);
        }

        //绘制刻度线
        //TODO 分别绘制分钟刻度和时刻刻度
        for (int i = 0; i < 12; i++) {
            if (i % 3 == 0) {//一刻钟
                markPaint.setColor(mQuarterMarkColor);
            } else {
                markPaint.setColor(mMinuteMarkColor);
            }
            canvas.drawLine(
                    centerX,
                    centerY - radius + MARK_GAP + MARK_WIDTH/2,
                    centerX,
                    centerY - radius + MARK_GAP + MARK_WIDTH/2 + MARK_LENGTH,
                    markPaint);
            canvas.rotate(30, centerX, centerY);
        }
        canvas.save();

        Calendar calendar = Calendar.getInstance();
        int hour12 = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        //(方案一)每过一小时(3600秒)时针添加30度，所以每秒时针添加（1/120）度
        //(方案二)每过一小时(60分钟)时针添加30度，所以每分钟时针添加（1/2）度
        hourCanvas.save();
        //清空画布
        hourCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        hourCanvas.rotate(hour12 * 30 + minute * 0.5f, centerX, centerY);
        hourCanvas.drawLine(centerX, centerY,
                centerX, centerY - hourLineLength, hourPaint);
        if (isDrawCenterCircle)//根据指针的颜色绘制圆心
            hourCanvas.drawCircle(centerX, centerY, 2 * HOUR_LINE_WIDTH, hourPaint);
        hourCanvas.restore();

        //每过一分钟（60秒）分针添加6度，所以每秒分针添加（1/10）度；当minute加1时，正好second是0
        minuteCanvas.save();
        //清空画布
        minuteCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        minuteCanvas.rotate(minute * 6 + second * 0.1f, centerX, centerY);
        minuteCanvas.drawLine(centerX, centerY,
                centerX, centerY - minuteLineLength, minutePaint);
        if (isDrawCenterCircle)//根据指针的颜色绘制圆心
            minuteCanvas.drawCircle(centerX, centerY, 2 * MINUTE_LINE_WIDTH, minutePaint);
        minuteCanvas.restore();

        //每过一秒旋转6度
        secondCanvas.save();
        //清空画布
        secondCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        secondCanvas.rotate(second * 6, centerX, centerY);
        secondCanvas.drawLine(centerX, centerY,
                centerX, centerY - secondLineLength, secondPaint);
        if (isDrawCenterCircle)//根据指针的颜色绘制圆心
            secondCanvas.drawCircle(centerX, centerY, 2 * SECOND_LINE_WIDTH, secondPaint);
        secondCanvas.restore();

        canvas.drawBitmap(hourBitmap, 0, 0, null);
        canvas.drawBitmap(minuteBitmap, 0, 0, null);
        canvas.drawBitmap(secondBitmap, 0, 0, null);

        //每隔1s重新绘制
        postInvalidateDelayed(1000);

        if (onCurrentTimeListener != null) {
            //小时采用24小时制返回
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            String currentTime = intAdd0(h) + ":" + intAdd0(minute) + ":" + intAdd0(second);
            onCurrentTimeListener.currentTime(currentTime);
        }
    }

    /**
     * 初始化表盘、指针、刻度的画笔，设置颜色、抗锯齿等属性
     */
    private void init() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);

        markPaint = new Paint();
        markPaint.setAntiAlias(true);
        markPaint.setStyle(Paint.Style.FILL);
        markPaint.setStrokeCap(Paint.Cap.ROUND);

        numberPaint = new Paint();
        numberPaint.setStyle(Paint.Style.FILL);
        numberPaint.setAntiAlias(true);

        hourPaint = new Paint();
        hourPaint.setAntiAlias(true);
        hourPaint.setStyle(Paint.Style.FILL);
        hourPaint.setStrokeCap(Paint.Cap.ROUND);

        minutePaint = new Paint();
        minutePaint.setAntiAlias(true);
        minutePaint.setStyle(Paint.Style.FILL);
        minutePaint.setStrokeCap(Paint.Cap.ROUND);

        secondPaint = new Paint();
        secondPaint.setAntiAlias(true);
        secondPaint.setStyle(Paint.Style.FILL);
        secondPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * 重新设置view尺寸
     */
    private void reMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (measureWidthMode == MeasureSpec.AT_MOST
                && measureHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, DEFAULT_SIZE);
        } else if (measureWidthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, measureHeight);
        } else if (measureHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(measureWidth, DEFAULT_SIZE);
        }
    }

    public interface OnCurrentTimeListener {
        void currentTime(String time);
    }

    /**
     * int小于10的添加0
     */
    private String intAdd0(int i) {
        DecimalFormat df = new DecimalFormat("00");
        if (i < 10) {
            return df.format(i);
        } else {
            return i + "";
        }
    }

    // 重绘
    public void applyModify(){
        invalidate();
    }

    public void setmCircleColor(int mCircleColor){
        this.mCircleColor = mCircleColor;
        circlePaint.setColor(mCircleColor);
    }
    public void setIs_DIAL_SOLID(boolean is_DIAL_SOLID){
        this.is_DIAL_SOLID = is_DIAL_SOLID;
        if(is_DIAL_SOLID)
            circlePaint.setStyle(Paint.Style.FILL);
        else
            circlePaint.setStyle(Paint.Style.STROKE);
    }
    public void setDISP_NUM(boolean DISP_NUM){
        this.DISP_NUM = DISP_NUM;
    }
    public void setMARK_WIDTH(int MARK_WIDTH){
        this.MARK_WIDTH = MARK_WIDTH;
        markPaint.setStrokeWidth(MARK_WIDTH);
    }
    public void setMARK_LENGTH(int MARK_LENGTH){ //绘制时才使用
        this.MARK_LENGTH = MARK_LENGTH;
    }
    public void setNUM_RADIUS(int NUM_RADIUS){
        this.NUM_RADIUS = NUM_RADIUS;
    }
    public void setNUM_SIZE(int NUM_SIZE){
        this.NUM_SIZE = NUM_SIZE;
        numberPaint.setTextSize(NUM_SIZE);
    }
    public void setNumberColor(int numberColor){
        this.numberColor = numberColor;
        numberPaint.setColor(numberColor);
    }
    public void setmQuarterMarkColor(int mQuarterMarkColor){ //绘制时才使用
        this.mQuarterMarkColor = mQuarterMarkColor;
    }
    public void setmMinuteMarkColor(int mMinuteMarkColor){ //绘制时才使用
        this.mMinuteMarkColor = mMinuteMarkColor;
    }
    public void setMARK_GAP(int MARK_GAP){ //绘制时才使用
        this.MARK_GAP = MARK_GAP;
    }
    public void setmHourColor(int mHourColor){
        this.mHourColor = mHourColor;
        hourPaint.setColor(mHourColor);
    }
    public void setmMinuteColor(int mMinuteColor){
        this.mMinuteColor = mMinuteColor;
        minutePaint.setColor(mMinuteColor);
    }
    public void setmSecondColor(int mSecondColor){
        this.mSecondColor = mSecondColor;
        secondPaint.setColor(mSecondColor);
    }
    public void setHOUR_LINE_WIDTH(int HOUR_LINE_WIDTH){
        this.HOUR_LINE_WIDTH = HOUR_LINE_WIDTH;
        hourPaint.setStrokeWidth(HOUR_LINE_WIDTH);
    }
    public void setMINUTE_LINE_WIDTH(int MINUTE_LINE_WIDTH){
        this.MINUTE_LINE_WIDTH = MINUTE_LINE_WIDTH;
        minutePaint.setStrokeWidth(MINUTE_LINE_WIDTH);
    }
    public void setSECOND_LINE_WIDTH(int SECOND_LINE_WIDTH){
        this.SECOND_LINE_WIDTH = SECOND_LINE_WIDTH;
        secondPaint.setStrokeWidth(SECOND_LINE_WIDTH);
    }
    public void setisDrawCenterCircle(boolean isDrawCenterCircle){ //绘制时才使用
        this.isDrawCenterCircle = isDrawCenterCircle;
    }

    // 提供数值获取接口，用于显示在设置对话框
    public int getmCircleColor(){
        return mCircleColor;
    }
    public boolean getIs_DIAL_SOLID(){
        return is_DIAL_SOLID;
    }
    public boolean getDISP_NUM(){
        return DISP_NUM;
    }
    public int getMARK_WIDTH(){
        return MARK_WIDTH;
    }
    public int getMARK_LENGTH(){
        return MARK_LENGTH;
    }
    public int getNUM_RADIUS(){
        return NUM_RADIUS;
    }
    public int getNUM_SIZE(){
        return NUM_SIZE;
    }
    public int getNumberColor(){
        return numberColor;
    }
    public int getmQuarterMarkColor(){
       return mQuarterMarkColor;
    }
    public int getmMinuteMarkColor(){
        return mMinuteMarkColor;
    }
    public int getMARK_GAP(){
        return MARK_GAP;
    }
    public int getmHourColor(){
        return mHourColor;
    }
    public int getmMinuteColor(){
        return mMinuteColor;
    }
    public int getmSecondColor(){
        return mSecondColor;
    }
    public int getHOUR_LINE_WIDTH(){
        return HOUR_LINE_WIDTH;
    }
    public int getMINUTE_LINE_WIDTH(){
        return MINUTE_LINE_WIDTH;
    }
    public int getSECOND_LINE_WIDTH(){
        return SECOND_LINE_WIDTH;
    }
    public boolean getisDrawCenterCircle(){
        return isDrawCenterCircle;
    }

}
