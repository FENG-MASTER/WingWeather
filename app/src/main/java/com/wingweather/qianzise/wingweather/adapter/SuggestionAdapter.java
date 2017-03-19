package com.wingweather.qianzise.wingweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.hedgehog.ratingbar.RatingBar;
import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.model.Suggestion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qianzise on 2017/3/19 0019.
 */

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.Holder>{

    private List<Suggestion> suggestionList1;
    private List<Suggestion> suggestionList2;
    private Context mContext;

    public SuggestionAdapter(Context context,List<Suggestion> suggestionList) {
        this.suggestionList1 = suggestionList;
        mContext=context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        View view=layoutInflater.inflate(R.layout.item_list_suggestion,parent,false);
        Holder holder=new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.setDetail(suggestionList1.get(position).getDetail());
        holder.setIndexTex(suggestionList1.get(position).getIndex());
        holder.setName(suggestionList1.get(position).getName());
        holder.setIndex(2);
    }

    @Override
    public int getItemCount() {
        return suggestionList1.size();
    }

    static class Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_suggestion_name)
        TextView name;
        @BindView(R.id.tv_suggestion_index)
        TextView index;
        @BindView(R.id.rb_index)
        RatingBar ratingBar;
        @BindView(R.id.tv_suggestion_detail)
        TextView detail;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        public void setIndexTex(String indexT){
            index.setText(indexT);
        }

        public void setIndex(int i){
            ratingBar.setStar(i);
        }

        public void setName(String name){
            this.name.setText(name);
        }

        public void setDetail(String detail){
            this.detail.setText(detail);
        }




    }
}
