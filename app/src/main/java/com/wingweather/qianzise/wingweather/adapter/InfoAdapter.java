package com.wingweather.qianzise.wingweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.model.Weather;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qianzise on 2017/3/2 0002.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.Holder> {
    private Context context;
    private Weather weather1;
    private Weather weather2;

    private List<Pair<String,String>> l1;
    private List<Pair<String,String>> l2;

    public InfoAdapter(Context context, Weather weather1,Weather weather2) {
        this.context=context;
        this.weather1=weather1;
        this.weather2=weather2;

        l1=weather1.getBaseInfo();
        l2=weather2.getBaseInfo();
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_list_info,parent,false);
        Holder holder=new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.setLeft(l1.get(position).second);
        if (l1.get(position).first.equals(l2.get(position).first)){
            holder.setTitle(l1.get(position).first);
        }
        holder.setRight(l2.get(position).second);

    }

    @Override
    public int getItemCount() {
        return l1.size();
    }



    public static class Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_info_left)
        TextView left;
        @BindView(R.id.tv_info_right)
        TextView right;
        @BindView(R.id.tv_info_title)
        TextView title;

        public Holder(View itemView) {
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
}
