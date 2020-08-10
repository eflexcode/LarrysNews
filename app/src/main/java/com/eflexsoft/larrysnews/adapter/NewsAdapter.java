package com.eflexsoft.larrysnews.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.larrysnews.NewsDetailsActivity;
import com.eflexsoft.larrysnews.R;
import com.eflexsoft.larrysnews.model.Article;
import com.eflexsoft.larrysnews.utils.Utils;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    static List<Article> articleList;
    static Context context;

    public NewsAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(
                R.layout.news_item,parent,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Article article = articleList.get(position);

        holder.author.setText(article.getAuthor());
        holder.source.setText(article.getSource().getName());
        holder.title.setText(article.getTitle());
        holder.text.setText(article.getDescription());
        holder.time.setText(Utils.getTime(article.getPublishedAt()));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.error(Utils.getRandomColor());
        requestOptions.placeholder(Utils.getRandomColor());

        Glide.with(context)
                .load(article.getUrlToImage())
                .apply(requestOptions)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                       holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.newsImage);

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView source;
        TextView time;
        TextView author;
        TextView title;
        TextView text;

        ImageView newsImage;
        ImageView shareImage;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            source = itemView.findViewById(R.id.source);
            text = itemView.findViewById(R.id.content_text);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title_text);
            author = itemView.findViewById(R.id.author);
            shareImage = itemView.findViewById(R.id.share);

            newsImage = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.progress_bar);

            shareImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Article article = articleList.get(getAdapterPosition());

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plan");
                    intent.putExtra(Intent.EXTRA_SUBJECT,article.getTitle());

                    String body = article.getDescription()+"\n"+article.getUrl()+" \n shared from Larry's News";
                    intent.putExtra(Intent.EXTRA_TEXT,body);
                    context.startActivity(Intent.createChooser(intent,"Share On"));


                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Article article = articleList.get(getAdapterPosition());

                    Intent intent = new Intent(context, NewsDetailsActivity.class);
                    intent.putExtra("source",article.getSource().getName());
                    intent.putExtra("url",article.getUrl());
                    intent.putExtra("time",Utils.getTime(article.getPublishedAt()));
                    intent.putExtra("author",article.getAuthor());
                    intent.putExtra("imageUrl",article.getUrlToImage());

                    Pair<View,String > viewStringPair = Pair.create((View)newsImage, ViewCompat.getTransitionName(newsImage));

                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            (Activity) context,viewStringPair
                    );

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        context.startActivity(intent, activityOptionsCompat.toBundle());
                    } else {
                        context.startActivity(intent);
                    }

                }
            });
        }
    }

}
