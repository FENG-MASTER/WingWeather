package com.wingweather.qianzise.wingweather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.wingweather.qianzise.wingweather.R;

/**
 * Created by qianzise on 2017/3/8 0008.
 */

public class CircleImageView extends android.support.v7.widget.AppCompatImageView{
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private Paint mCirlePaint=new Paint();//画圈画笔
    private Paint mImagePaint=new Paint();//画图画笔

    private Bitmap mBitmap;

    private float circleRadius;//圆圈半径
    private int circleColor =Color.BLACK;


    private boolean mReady=false;
    private boolean mSetupPending=false;


    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("my","构造");
        super.setScaleType(SCALE_TYPE);


        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CircleImageView,defStyleAttr,0);
        int indexs=a.getIndexCount();
        for (int i=0;i<indexs;i++){
            int attr=a.getIndex(i);

            switch (attr){
                case R.styleable.CircleImageView_circle_color:
                    circleColor =a.getColor(i,Color.BLACK);
                    break;
                case R.styleable.CircleImageView_circle_r:
                    circleRadius =a.getDimension(R.styleable.CircleImageView_circle_r,1);
                    break;
                default:
                    break;
            }
        }

        a.recycle();

        mReady=true;

        if (mSetupPending) {
            initPaint();
            mSetupPending = false;
        }



    }



    private void initPaint(){
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (mBitmap == null) {
            return;
        }
        Log.e("my","initPaint");
        mCirlePaint=new Paint();
        mCirlePaint.setColor(circleColor);
        mCirlePaint.setStrokeWidth(2);
        mCirlePaint.setAntiAlias(true);//反锯齿
        mCirlePaint.setStyle(Paint.Style.STROKE);


        BitmapShader bitmapShader=new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mImagePaint=new Paint();
        mImagePaint.setShader(bitmapShader);
        mImagePaint.setAntiAlias(true);//反锯齿

        circleRadius=Math.min(getWidth(),getHeight())/2;//设置绘制圈的半径为图片的一半

        invalidate();//强制刷新下,重绘
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPaint();
    }


    private void upDateMatrix(){

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("my","onDraw");

        if (mBitmap==null){
            return;
        }

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, circleRadius, mImagePaint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, circleRadius, mCirlePaint);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        Log.e("my","setImageBitmap");
        super.setImageBitmap(bm);
        mBitmap=bm;
        initPaint();
    }


    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        Log.e("my","setImageDrawable");
        super.setImageDrawable(drawable);
        mBitmap=getBitmap(drawable);
        initPaint();
    }


    @Override
    public void setImageResource(@DrawableRes int resId) {
        Log.e("my","setImageResource");
        super.setImageResource(resId);
        mBitmap=getBitmap(getDrawable());
        initPaint();
    }




    private Bitmap getBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(1,
                        1,
                        Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
}