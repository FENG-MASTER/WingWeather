package com.wingweather.qianzise.wingweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.model.Suggestion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qianzise on 2017/3/19 0019.
 */

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.Holder>{

    private List<Suggestion> suggestionList;
    private Context mContext;

    public SuggestionAdapter(Context context,List<Suggestion> suggestionList) {
        this.suggestionList = suggestionList;
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

    }

    @Override
    public int getItemCount() {
        return suggestionList.size();
    }

    static class Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_suggestion_name)
        TextView name;
        @BindView(R.id.tv_suggestion_index)
        TextView index;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);


        }
    }
}
