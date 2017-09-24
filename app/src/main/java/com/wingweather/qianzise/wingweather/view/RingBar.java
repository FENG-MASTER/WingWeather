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
 * <p>
 * 环状进度条
 * <p>
 * 目前不支持拖拽,只支持显示,支持添加中心view组件(只能一个)
 * <p>
 * 组件形状目前只能为正方形
 */

public class RingBar extends RelativeLayout {

    /**
     * 圆润线条
     */
    public static final int TYPE_ROUND = 2;
    /**
     * 棱角线条
     */
    public static final int TYPE_FLAT = 1;
    /**
     * 最大进度
     */
    protected int max;
    /**
     * 当前进度
     */
    protected int mProgress;
    private List<ProgressListener> listenerList = new ArrayList<>();
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

    /**
     * 线条种类{@link RingBar#TYPE_ROUND} {@link RingBar#TYPE_FLAT}
     */
    private int borderType;

    private RectF mRect;
    private int degree;


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
        borderType = array.getInteger(R.styleable.RingBar_borderType, TYPE_FLAT);

        array.recycle();

        if (displayProgress) {
            initDisplayText();
        }


        init();


    }

    /**
     * 初始化显示文字,当{@link RingBar#displayProgress }为真,则调用这函数
     */
    private void initDisplayText() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        addProgressListener(progress -> textView.setText(String.valueOf(progress)));
        addView(textView);
    }


    /**
     * 初始化
     */
    private void init() {

        mFinishPaint = new Paint(ANTI_ALIAS_FLAG);
        mUnFinishPaint = new Paint(ANTI_ALIAS_FLAG);

        if (borderType == TYPE_ROUND) {
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

    /**
     * 增加进度变化监听器
     *
     * @param listener 监听器
     */
    public void addProgressListener(ProgressListener listener) {
        listenerList.add(listener);
    }

    /**
     * 初始化子view,每个新加的子view都要经过这个函数
     * <p>
     * 主要功能:
     * <p>
     * 检测是否超过了一个子view,如果是则报错
     * 添加的子view强制设置成居中位置
     *
     * @param view 子view
     */
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置绘制区域
        mRect = new RectF(borderWidth, borderWidth, w - 2 * borderWidth, h - 2 * borderWidth);
    }

    /**
     * 注意,如果继承的是viewGroup的子类,要自己绘制的话,代码写在这里,已经不会调用{@link ViewGroup#onDraw(Canvas)}方法
     *
     * @param canvas 画布
     */
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

    /**
     * 获得进度
     *
     * @return 进度
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * 获得最大进度
     *
     * @return 最大进度
     */
    public int getMax() {
        return max;
    }

    /**
     * 设置进度
     *
     * @param mProgress 进度
     */
    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
        invalidate();
        notifyProgressChange(mProgress);
    }

    private void notifyProgressChange(int progress) {
        for (ProgressListener progressListener : listenerList) {
            progressListener.onProgressChange(progress);
        }
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        initView(child);
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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        listenerList.clear();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //以最短边设置正方形
        if (width > height) {
            height = width;
        } else {
            width = height;
        }


        super.onMeasure(MeasureSpec.makeMeasureSpec(width, wMode), MeasureSpec.makeMeasureSpec(height, hMode));
    }

    /**
     * 移除进度变化监听器
     *
     * @param listener 监听器
     */
    public void removeProgressListener(ProgressListener listener) {
        listenerList.remove(listener);
    }

    public interface ProgressListener {
        void onProgressChange(int progress);
    }
}
