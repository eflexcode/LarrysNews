package com.eflexsoft.larrysnews.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

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
import com.eflexsoft.larrysnews.R;
import com.eflexsoft.larrysnews.databinding.ActivityNewsDetailsAcivityBinding;
import com.eflexsoft.larrysnews.utils.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class NewsDetailsActivity extends AppCompatActivity {

    ActivityNewsDetailsAcivityBinding binding;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_news_details_acivity);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_details_acivity);

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        intent = getIntent();

//        FrameLayout container = findViewById(R.id.fragment_main);

        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i != 0) {
                    binding.source2.setVisibility(View.VISIBLE);
                    binding.url.setVisibility(View.VISIBLE);
                    binding.w.setImageResource(R.drawable.ic_back2);
                } else {
                    binding.source2.setVisibility(View.GONE);
                    binding.url.setVisibility(View.GONE);
                    binding.w.setImageResource(R.drawable.ic_back);
                }
            }
        });

        String getSource = intent.getStringExtra("source");
        String getUrl = intent.getStringExtra("url");
        String getTime = intent.getStringExtra("time");
        String getAuthor = intent.getStringExtra("author");
        String getImageUrl = intent.getStringExtra("imageUrl");

        binding.time.setText(getTime);
        binding.source2.setText(getSource);
        binding.source.setText(getSource);
        binding.author.setText(getAuthor);
        binding.url.setText(getUrl);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.ic_error);
        requestOptions.placeholder(Utils.getRandomColor());

        Glide.with(this)
                .load(getImageUrl)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.toolbarImg);

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDisplayZoomControls(false);
        binding.webView.getSettings().setBuiltInZoomControls(true);
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl(getUrl);

        StartAppSDK.init(this, "207451295", true);
        StartAppAd.disableSplash();
        StartAppSDK.enableReturnAds(false);

        if (binding.fragmentMain != null && binding.fragmentMain.getChildCount() < 1) {
            binding.fragmentMain.addView(new Banner(this), new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));
            StartAppAd.disableSplash();
            StartAppAd.showAd(this);
        }

        StartAppAd.disableSplash();
    }

    public void finish(View view) {
        finish();
    }
}