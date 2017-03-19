package com.wingweather.qianzise.wingweather.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.model.Weather;
import com.wingweather.qianzise.wingweather.observer.WeatherObservable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by qianzise on 2017/3/2 0002.
 */

public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_base=1;
    public static final int TYPE_with_image=2;

    private Context context;
    private Weather weather1;
    private Weather weather2;

    private List<Pair<String,String>> l1;
    private List<Pair<String,String>> l2;

    public InfoAdapter(Context context, Weather weather1,Weather weather2) {
        this.context=context;
        this.weather1=weather1;
        this.weather2=weather2;

        l1=weather1.getTodayBaseInfo();
        l2=weather2.getTodayBaseInfo();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_base:
                return createBaseHolder(parent);

            case TYPE_with_image:
                return createImageHolder(parent);

            default:
                return createBaseHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseHolder){
            BaseHolder baseHolder=(BaseHolder)holder;

            baseHolder.setLeft(l1.get(position).second);
            if (l1.get(position).first.equals(l2.get(position).first)){
                baseHolder.setTitle(l1.get(position).first);
            }
            baseHolder.setRight(l2.get(position).second);

        }else if (holder instanceof ImageHolder){
            final ImageHolder imageHolder=(ImageHolder)holder;
            if (l1.get(position).first.equals(l2.get(position).first)){
                imageHolder.setTitle(l1.get(position).first);
            }
            WeatherObservable.getWeatherConDrawable(l1.get(position).second).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Consumer<Drawable>() {
                        @Override
                        public void accept(Drawable drawable) throws Exception {
                            imageHolder.setLeft(drawable);
                        }
                    });

            WeatherObservable.getWeatherConDrawable(l2.get(position).second).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Consumer<Drawable>() {
                        @Override
                        public void accept(Drawable drawable) throws Exception {
                            imageHolder.setRight(drawable);
                        }
                    });

        }


    }

    private BaseHolder createBaseHolder(ViewGroup parent){
        View view= LayoutInflater.from(context).inflate(R.layout.item_list_base_info,parent,false);
        BaseHolder baseHolder =new BaseHolder(view);
        return baseHolder;
    }

    private ImageHolder createImageHolder(ViewGroup parent){
        View view= LayoutInflater.from(context).inflate(R.layout.item_list_image_info,parent,false);
        ImageHolder holder =new ImageHolder(view);
        return holder;
    }





    @Override
    public int getItemCount() {
        return l1.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position==1){
            return TYPE_with_image;
        }else {
            return TYPE_base;
        }
    }



    public static class BaseHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_info_left)
        TextView left;
        @BindView(R.id.tv_info_right)
        TextView right;
        @BindView(R.id.tv_info_title)
        TextView title;

        public BaseHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setLeft(String s){
            left.setText(s);
        }

        public void setRight(String s){
            right.setText(s);
        }

        public void setTitle(String s){
            title.setText(s);
        }

    }


    public static class ImageHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_weather_con_left)
        ImageView left;
        @BindView(R.id.iv_weather_con_right)
        ImageView right;
        @BindView(R.id.tv_info_title)
        TextView title;

        public ImageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setLeft(Drawable d){
            left.setImageDrawable(d);
        }

        public void setRight(Drawable d){
            right.setImageDrawable(d);
        }

        public void setTitle(String s){
            title.setText(s);
        }

    }
}
