package com.wingweather.qianzise.wingweather.adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hedgehog.ratingbar.RatingBar;
import com.wingweather.qianzise.wingweather.R;
import com.wingweather.qianzise.wingweather.util.Config;
import com.wingweather.qianzise.wingweather.model.Suggestion;
import com.wingweather.qianzise.wingweather.observer.Bus.SuggestionChangeAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 生活指数显示用的适配器
 */

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.Holder> {

    private List<Suggestion> suggestionList1;
    private List<Suggestion> suggestionList2;
    private Context mContext;

    public SuggestionAdapter(Context context, List<Suggestion> l1, List<Suggestion> l2) {
        this.suggestionList1 = l1;
        this.suggestionList2 = l2;
        mContext = context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_list_suggestion, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.addSuggestion(suggestionList1.get(position));
        holder.addSuggestion(suggestionList2.get(position));
        holder.show(Config.LEFT);
    }

    @Override
    public int getItemCount() {
        return suggestionList1.size();
    }


    static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ll_item_bg)
        LinearLayout bg;
        @BindView(R.id.tv_suggestion_name)
        TextView name;
        @BindView(R.id.tv_suggestion_index)
        TextView index;
        @BindView(R.id.rb_index)
        RatingBar ratingBar;
        @BindView(R.id.tv_suggestion_detail)
        TextView detail;


        private List<Suggestion> list=new ArrayList<>();
        private int num=Config.LEFT;//当前显示的是哪个天气的生活指数

        public Holder(View itemView) {
            super(itemView);
            list.clear();
            ButterKnife.bind(this, itemView);
            bg.setOnClickListener(this);
            EventBus.getDefault().register(this);

        }

        public void addSuggestion(Suggestion suggestion){
            list.add(suggestion);
        }



        private void show(int n){
            n=n%2;
            Suggestion s=list.get(n);
            index.setText(s.getIndex());
            ratingBar.setStar(2);
            name.setText(s.getName());
            detail.setText(s.getDetail());
        }


        public void showNextWithAnime(final int num){

            bg.animate().setDuration(500).setInterpolator(new DecelerateInterpolator()).alpha(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    show(num);
                    bg.animate().setListener(null);
                    bg.animate().setDuration(500).alpha(1).start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();

        }

        @Subscribe
        public void play(SuggestionChangeAction action){
            showNextWithAnime(action.getIndex());
        }

        private int getNext(){
            num++;
            num=num%2;
            return num;
        }

        @Override
        public void onClick(View v) {
            if (bg.getAnimation()!=null&&bg.getAnimation().hasStarted()){
                return;
            }
            EventBus.getDefault().post(new SuggestionChangeAction(getNext()));
        }
    }
}
