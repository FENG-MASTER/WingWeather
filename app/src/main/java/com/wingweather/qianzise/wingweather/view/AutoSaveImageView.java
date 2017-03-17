package com.wingweather.qianzise.wingweather.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.wingweather.qianzise.wingweather.App;
import com.wingweather.qianzise.wingweather.base.MyPreferences;

/**
 * Created by qianzise on 2017/3/16 0016.
 */

public class AutoSaveImageView extends android.support.v7.widget.AppCompatImageView{

    private Drawable mDrawable=null;
    private boolean noFirstTimeSetBitMap;//不能赋初值,别问我为什么,这个默认false就行,不能赋false

    public AutoSaveImageView(Context context) {
        super(context);
        init();
    }

    public AutoSaveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoSaveImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    private void init(){
        check();

    }


    /**
     * 检查下是否符合要求
     * 1.必须有ID
     */
    private void  check(){
        if (getId()==NO_ID){
            try {
                throw new Exception("必须给AutoSaveImageView赋ID");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {

        if (!noFirstTimeSetBitMap){//第一次加载图片
            if (!load()){//先尝试加载存储的图片
                //失败
                mDrawable=getDrawable(bm) ;

            }
        }else {
            mDrawable=getDrawable(bm);
            save();
        }
        noFirstTimeSetBitMap=true;
        super.setImageDrawable(mDrawable);

    }


    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {

        super.setImageDrawable(drawable);

        if (!noFirstTimeSetBitMap){//第一次加载图片
            if (!load()){//先尝试加载存储的图片
                //失败
                mDrawable=drawable;

            }
        }else {
            mDrawable=drawable;
            save();
        }
        noFirstTimeSetBitMap=true;

        super.setImageDrawable(mDrawable);

    }


    @Override
    public void setImageResource(@DrawableRes int resId) {

        if (!noFirstTimeSetBitMap){//第一次加载图片
            if (!load()){//先尝试加载存储的图片
                //失败
                mDrawable=getDrawable();

            }
        }else {
            mDrawable=getDrawable();
            save();
        }
        noFirstTimeSetBitMap=true;

        super.setImageDrawable(mDrawable);
    }

    private Drawable getDrawable(Bitmap bitmap){
        return new BitmapDrawable(App.getContext().getResources(),bitmap);
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

    private void save(){
        if (mDrawable!=null){
            MyPreferences.saveImage(getId(),getBitmap(mDrawable));
        }
    }

    private boolean load(){
        Bitmap bitmap=MyPreferences.loadImage(getId());
        if (bitmap!=null){
            mDrawable=getDrawable(bitmap);
            return true;
        }else {
            return false;
        }
    }

    public Bitmap getBitmap(){
        return getBitmap(mDrawable);
    }

}
