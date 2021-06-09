package com.eflexsoft.larrysnews.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eflexsoft.larrysnews.ui.NewsDetailsActivity;
import com.eflexsoft.larrysnews.R;
import com.eflexsoft.larrysnews.databinding.NewsItem2FullScreenBinding;
import com.eflexsoft.larrysnews.model.Article;
import com.eflexsoft.larrysnews.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    List<Article> articleList = new ArrayList<>();
    Context context;
    Context context2;

    @Inject
    public NewsAdapter(@ApplicationContext Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        NewsItem2FullScreenBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.news_item2_full_screen, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Article article = articleList.get(position);

        holder.binding.setArticle(article);


    }

    @Override
    public int getItemCount() {
        return articleList.size();

    }

    private static final String TAG = "NewsAdapter";
    public void setArticleList(List<Article> articleList, Context context2) {
        this.articleList = articleList;
        this.context2 = context2;
        notifyDataSetChanged();
        Log.d(TAG, "setArticleList: "+articleList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        NewsItem2FullScreenBinding binding;

        public ViewHolder(@NonNull NewsItem2FullScreenBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

//            shareImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Article article = articleList.get(getAdapterPosition());
//
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.setType("text/plan");
//                    intent.putExtra(Intent.EXTRA_SUBJECT,article.getTitle());
//
//                    String body = article.getDescription()+"\n"+article.getUrl()+" \n shared from Larry's News";
//                    intent.putExtra(Intent.EXTRA_TEXT,body);
//                    context.startActivity(Intent.createChooser(intent,"Share On"));
//
//
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Article article = articleList.get(getAdapterPosition());

                    Intent intent = new Intent(context2, NewsDetailsActivity.class);
                    intent.putExtra("source", article.getSource().getName());
                    intent.putExtra("url", article.getUrl());
                    intent.putExtra("time", Utils.getTime(article.getPublishedAt()));
                    intent.putExtra("author", article.getAuthor());
                    intent.putExtra("imageUrl", article.getUrlToImage());

                    Pair<View, String> viewStringPair = Pair.create((View) binding.newImage, ViewCompat.getTransitionName(binding.newImage));

                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            (Activity) context2, viewStringPair
                    );

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        context2.startActivity(intent, activityOptionsCompat.toBundle());
                    } else {
                        context2.startActivity(intent);
                    }
                }
            });
        }
    }
}
