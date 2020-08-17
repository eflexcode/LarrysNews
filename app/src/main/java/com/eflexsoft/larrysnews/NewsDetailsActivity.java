package com.eflexsoft.larrysnews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.larrysnews.utils.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class NewsDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView source;
    TextView time;
    TextView author;
    TextView source2;
    TextView url;

    ImageView imageView;
    WebView webView;

    Intent intent;
    AppBarLayout appBarLayout;

    ProgressBar progressBar;
    ImageView w ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details_acivity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        intent = getIntent();

        FrameLayout container = findViewById(R.id.fragment_main);

        appBarLayout = findViewById(R.id.appbar);
        w = findViewById(R.id.w);
        source = findViewById(R.id.source);
        progressBar = findViewById(R.id.progress_bar);
        url = findViewById(R.id.url);
        time = findViewById(R.id.time);
        source2 = findViewById(R.id.source2);
        author = findViewById(R.id.author);

        imageView = findViewById(R.id.toolbar_img);
        webView = findViewById(R.id.webView);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i != 0){
                    source2.setVisibility(View.VISIBLE);
                    url.setVisibility(View.VISIBLE);
                    w.setImageResource(R.drawable.ic_back2);
                }else {
                    source2.setVisibility(View.GONE);
                    url.setVisibility(View.GONE);
                    w.setImageResource(R.drawable.ic_back);
                }
            }
        });

        String getSource = intent.getStringExtra("source");
        String getUrl = intent.getStringExtra("url");
        String getTime = intent.getStringExtra("time");
        String getAuthor = intent.getStringExtra("author");
        String getImageUrl = intent.getStringExtra("imageUrl");


        time.setText(getTime);
        source2.setText(getSource);
        source.setText(getSource);
        author.setText(getAuthor);
        url.setText(getUrl);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomColor());
        requestOptions.placeholder(Utils.getRandomColor());

        Glide.with(this)
                .load(getImageUrl)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setSupportZoom(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(getUrl);

        Toast.makeText(this, "Loading web page", Toast.LENGTH_SHORT).show();

        StartAppSDK.init(this, "your api key", true);
        StartAppAd.disableSplash();
        if (container != null && container.getChildCount() < 1) {
            container.addView(new Banner(this), new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));
            StartAppAd.showAd(this);
        }

        StartAppAd.showAd(this);

    }

    public void finish(View view) {
        finish();
    }
}