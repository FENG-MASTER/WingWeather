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
 * @see com.wingweather.qianzise.wingweather.fragment.MainInfoFragment
 *
 * 基础天气信息适配器
 * 有两种布局item
 */

public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_base=1;             //第一种布局
    public static final int TYPE_with_image=2;       //第二种布局,带图片显示

    private Context context;
    private Weather weather1;
    private Weather weather2;

    private List<Pair<String,String>> nowList1;//今天天气情况
    private List<Pair<String,String>> nowList2;

    private List<Pair<String,String>> nextList1;//明天天气情况
    private List<Pair<String,String>> nextList2;


    public InfoAdapter(Context context, Weather weather1,Weather weather2) {
        this.context=context;
        this.weather1=weather1;
        this.weather2=weather2;

        nowList1 =weather1.getTodayBaseInfo();//获取天气情况
        nowList2 =weather2.getTodayBaseInfo();

        nextList1=weather1.getNextDayBaseInfo();//获取天气情况
        nextList2=weather2.getNextDayBaseInfo();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){//判断下是哪个类型的
            case TYPE_base:
                //基础类型
                return createBaseHolder(parent);

            case TYPE_with_image:
                //带图的类型
                return createImageHolder(parent);

            default:
                //默认用基础类型
                return createBaseHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseHolder){
            BaseHolder baseHolder=(BaseHolder)holder;

            baseHolder.setLeft(nowList1.get(position).second);
            baseHolder.setLeft_next(nextList1.get(position).second);
            if (nowList1.get(position).first.equals(nowList2.get(position).first)){
                baseHolder.setTitle(nowList1.get(position).first);
            }
            baseHolder.setRight_next(nextList2.get(position).second);
            baseHolder.setRight(nowList2.get(position).second);




        }else if (holder instanceof ImageHolder){
            final ImageHolder imageHolder=(ImageHolder)holder;
            if (nowList1.get(position).first.equals(nowList2.get(position).first)){
                imageHolder.setTitle(nowList1.get(position).first);
            }
            WeatherObservable.getWeatherConDrawable(nowList1.get(position).second).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Consumer<Drawable>() {
                        @Override
                        public void accept(Drawable drawable) throws Exception {
                            imageHolder.setLeft(drawable);
                        }
                    });

            WeatherObservable.getWeatherConDrawable(nowList2.get(position).second).
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

    /**
     * 基础holder生成
     * @param parent 生成新布局的时候用的父view
     * @return holder
     */
    private BaseHolder createBaseHolder(ViewGroup parent){
        View view= LayoutInflater.from(context).inflate(R.layout.item_list_base_info,parent,false);
        BaseHolder baseHolder =new BaseHolder(view);
        return baseHolder;
    }

    /**
     * 如上,带图版本而已
     * @param parent 生成新布局的时候用的父view
     * @return holder
     */
    private ImageHolder createImageHolder(ViewGroup parent){
        View view= LayoutInflater.from(context).inflate(R.layout.item_list_image_info,parent,false);
        ImageHolder holder =new ImageHolder(view);
        return holder;
    }





    @Override
    public int getItemCount() {
        return nowList1.size();
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

        @BindView(R.id.tv_info_left_next_day)
        TextView left_next;
        @BindView(R.id.tv_info_right_next_day)
        TextView right_next;

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

        public void setLeft_next(String s){
            left_next.setText(s);
        }

        public void setRight_next(String s){
            right_next.setText(s);
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
