package com.wingweather.qianzise.wingweather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.util.MyPreferences;

/**
 * 自定义圆形图片框view,继承的子类都会具有切换图片后自动保存的功能
 */

public class CircleImageView extends AutoSaveImageView{
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private Paint mCirlePaint=new Paint();//画圈画笔
    private Paint mImagePaint=new Paint();//画图画笔

    private Bitmap mBitmap;//当前绘制bitmap
    private BitmapShader bitmapShader;//当前绘制shader
    private Matrix mMatrix=new Matrix();

    private float mBitmapW;//bitmap宽度
    private float mBitmapH;//bitmap高度
    private float mViewW;//控件宽度
    private float mViewH;//控件高度

    private float circleRadius;//圆圈半径
    private int circleColor =Color.BLACK;//圆圈的边框颜色
    private float circleBorder;//圆圈边框大小


    private boolean mReady=false;
    private boolean mSetupPending=false;

    private boolean notFirstSetBitMap=false;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        super.setScaleType(SCALE_TYPE);
        

        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.CircleImageView,defStyleAttr,0);
        circleColor =a.getColor(R.styleable.CircleImageView_circleColor,Color.BLACK);
        circleBorder =a.getDimension(R.styleable.CircleImageView_circleBorder,1);


        a.recycle();

        mReady=true;

//        if (load()){
//            initPaint();
//            mSetupPending=false;
//        }else {
            if (mSetupPending) {
                initPaint();
                mSetupPending = false;
            }
//        }





    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

    /**
     * 获取几个高度和宽度
     */
    private void getHeightAndWidth(){
        mBitmapH=mBitmap.getHeight();
        mBitmapW=mBitmap.getWidth();
        mViewH=getHeight();
        mViewW=getWidth();
    }


    private void initPaint(){
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (mBitmap == null) {
            return;
        }


        getHeightAndWidth();

        mCirlePaint=new Paint();
        mCirlePaint.setColor(circleColor);
        mCirlePaint.setStrokeWidth(circleBorder);
        mCirlePaint.setAntiAlias(true);//反锯齿
        mCirlePaint.setStyle(Paint.Style.STROKE);


        bitmapShader=new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mImagePaint=new Paint();
        mImagePaint.setShader(bitmapShader);
        mImagePaint.setAntiAlias(true);//反锯齿

        circleRadius=Math.min(mViewW,mViewH)/2;//设置绘制圈的半径为图片的一半

        upDateMatrix();
        invalidate();//强制刷新下,重绘
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPaint();
    }



    /**
     * 更新变形矩阵
     */
    private void upDateMatrix(){


        float scale;
        float dx = 0;
        float dy = 0;

        mMatrix.set(null);

        if (mBitmapW * mViewH > mViewW * mBitmapH) {
            scale = mViewH /  mBitmapH;
            dx = (mViewW - mBitmapW * scale) * 0.5f;
        } else {
            scale = mViewW /  mBitmapW;
            dy = (mViewH - mBitmapH * scale) * 0.5f;
        }

        mMatrix.setScale(scale, scale);
        mMatrix.postTranslate(dx, dy );

        bitmapShader.setLocalMatrix(mMatrix);

    }

    @Override
    protected void onDraw(Canvas canvas) {


        if (mBitmap==null){
            return;
        }
       // canvas.drawRect(-1,-1,getWidth()+1,getHeight()+1,mCirlePaint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, circleRadius, mImagePaint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, circleRadius, mCirlePaint);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap=getBitmap();
        initPaint();

    }


    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {

        super.setImageDrawable(drawable);
        mBitmap=getBitmap();

        initPaint();

    }


    @Override
    public void setImageResource(@DrawableRes int resId) {

        super.setImageResource(resId);
mBitmap=getBitmap();

        initPaint();

    }

    public void save(){
        if (mBitmap!=null){
            MyPreferences.saveImage(getId(),mBitmap);
        }
    }

    private boolean load(){
        notFirstSetBitMap=true;
        Bitmap bitmap=MyPreferences.loadImage(getId());
        if (bitmap!=null){
            mBitmap=bitmap;
            return true;
        }else {
            return false;
        }
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