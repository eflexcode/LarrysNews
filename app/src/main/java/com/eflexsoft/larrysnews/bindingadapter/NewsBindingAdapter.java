package com.eflexsoft.larrysnews.bindingadapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.eflexsoft.larrysnews.R;
import com.eflexsoft.larrysnews.utils.Utils;

public class NewsBindingAdapter {

    @BindingAdapter("setAuthor")
    public static void setAuthor(TextView textView, String author) {

        textView.setText("By " + author);

    }

    @BindingAdapter("setTime")
    public static void setTime(TextView textView, String time) {

        textView.setText(Utils.getTime(time));

    }

    @BindingAdapter("setImage")
    public static void setImage(ImageView image, String url) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.error(R.drawable.ic_error);
        requestOptions.placeholder(R.color.colorPrimary);

        Glide.with(image).load(url).apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade()).into(image);

    }

}
