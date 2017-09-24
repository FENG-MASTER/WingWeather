package com.wingweather.qianzise.wingweather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wingweather.qianzise.wingweather.R;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by qianzise on 2017/9/23.
 *
 */

public class RingBar extends RelativeLayout {

    public static final int TYPE_ROUND=2;
    public static final int TYPE_FLAT=1;


    private List<ProgressListener> listenerList =new ArrayList<>();
    /**
     * 最大进度
     */
    protected int max;
    /**
     * 当前进度
     */
    protected int mProgress;

    /**
     * 主画笔
     */
    private Paint mFinishPaint;
    /**
     * 副画笔
     */
    private Paint mUnFinishPaint;

    /**
     * 进度条主颜色
     */
    private int finishColor;
    /**
     * 进度条副颜色
     */
    private int unFinishColor;
    /**
     * 进度条宽度
     */
    private float borderWidth;
    /**
     * 是否显示进度文字
     */
    private boolean displayProgress;
    /**
     * 文字大小
     */
    private float textSize;
    /**
     * 文字颜色
     */
    private int textColor;

    private int borderType;

    private RectF mRect;
    private int degree;



    /**
     * @param context
     */
    public RingBar(Context context) {
        super(context);
    }

    public RingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RingBar, defStyleAttr, 0);
        finishColor = array.getColor(R.styleable.RingBar_finishColor, Color.BLACK);
        unFinishColor = array.getColor(R.styleable.RingBar_unFinishColor, Color.WHITE);
        borderWidth = array.getDimension(R.styleable.RingBar_borderWidth, 5);
        displayProgress = array.getBoolean(R.styleable.RingBar_displayProgress, false);
        textSize = array.getDimension(R.styleable.RingBar_textSize, 5);
        textColor = array.getColor(R.styleable.RingBar_textColor, Color.BLACK);

        mProgress = array.getInteger(R.styleable.RingBar_progress, 50);
        max = array.getInteger(R.styleable.RingBar_max, 100);

        degree = array.getInteger(R.styleable.RingBar_degree, 360);
        borderType=array.getInteger(R.styleable.RingBar_borderType,TYPE_FLAT);

        array.recycle();

        if (displayProgress){
            initDisplayText();
        }


        init();



    }

    private void initDisplayText(){
        TextView textView=new TextView(getContext());
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        addProgressListener(progress -> textView.setText(String.valueOf(progress)));
        addView(textView);
    }



    private void init() {

        mFinishPaint = new Paint(ANTI_ALIAS_FLAG);
        mUnFinishPaint = new Paint(ANTI_ALIAS_FLAG);

        if (borderType==TYPE_ROUND){
            mFinishPaint.setStrokeCap(Paint.Cap.ROUND);
            mUnFinishPaint.setStrokeCap(Paint.Cap.ROUND);
        }

        mFinishPaint.setAntiAlias(true);
        mUnFinishPaint.setAntiAlias(true);

        mFinishPaint.setColor(finishColor);
        mUnFinishPaint.setColor(unFinishColor);




        mFinishPaint.setStyle(Paint.Style.STROKE);
        mUnFinishPaint.setStyle(Paint.Style.STROKE);

        mFinishPaint.setStrokeWidth(borderWidth);
        mUnFinishPaint.setStrokeWidth(borderWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mRect = new RectF(borderWidth, borderWidth, w - 2 * borderWidth, h - 2 * borderWidth);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        float start = 270 - degree / 2;
        float anglef = (1f * getProgress()) / getMax() * degree;

        float angleunf = degree - anglef;
        if (getProgress() != 0) {
            canvas.drawArc(mRect, start, anglef, false, mFinishPaint);
        }
        if (getProgress() != getMax()) {
            canvas.drawArc(mRect, start + anglef, angleunf, false, mUnFinishPaint);
        }

    }

    public int getProgress() {
        return mProgress;
    }

    public int getMax() {
        return max;
    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
        invalidate();
        notifyProgressChange(mProgress);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        initView(child);
    }

    private void initView(View view) {

        if (getChildCount() == 0) {
            return;
        }
        if (getChildCount() > 1) {
            //不能超过一个子view
            try {
                throw new Exception("不能超过一个子view");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.addRule(CENTER_IN_PARENT);
            view.setLayoutParams(layoutParams);
            view.invalidate();
        }
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        initView(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        initView(child);

    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        initView(child);

    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        initView(child);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode=MeasureSpec.getMode(widthMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int hMode=MeasureSpec.getMode(heightMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);

        if (width>height){
            height=width;
        }else {
            width=height;
        }


        super.onMeasure(MeasureSpec.makeMeasureSpec(width,wMode), MeasureSpec.makeMeasureSpec(height,hMode));
    }


    public interface ProgressListener{
        void onProgressChange(int progress);
    }

    public void addProgressListener(ProgressListener listener){
        listenerList.add(listener);
    }

    public void removeProgressListener(ProgressListener listener){
        listenerList.remove(listener);
    }

    public void notifyProgressChange(int progress){
        for (ProgressListener progressListener : listenerList) {
            progressListener.onProgressChange(progress);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        listenerList.clear();
    }
}
