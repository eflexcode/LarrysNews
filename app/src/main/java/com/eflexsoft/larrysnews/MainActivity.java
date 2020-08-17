package com.eflexsoft.larrysnews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eflexsoft.larrysnews.adapter.NewsAdapter;
import com.eflexsoft.larrysnews.api.ApiClient;
import com.eflexsoft.larrysnews.api.ApiInterface;
import com.eflexsoft.larrysnews.model.Article;
import com.eflexsoft.larrysnews.model.News;
import com.eflexsoft.larrysnews.utils.Utils;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit;
    ApiInterface apiInterface;
    public static final String ApiKey = "your api key";

    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    List<Article> articleList ;
    SwipeRefreshLayout swipeRefreshLayout;

    Toolbar toolbar;
    ImageView fire;
    TextView fireText;
    Button tryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout container = findViewById(R.id.fragment_main);

        recyclerView = findViewById(R.id.newsRecycler);
        swipeRefreshLayout = findViewById(R.id.swipe);
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fire = findViewById(R.id.fire);
        fireText = findViewById(R.id.fireMessage);
        tryAgain = findViewById(R.id.fireTry);

        setSupportActionBar(toolbar);

        retrofit = ApiClient.getRetrofit();
        apiInterface = retrofit.create(ApiInterface.class);

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewsWithKeyword("");
            }
        });

        if (EasyPermissions.hasPermissions(this, Manifest.permission.INTERNET)) {
            getNew("");
        } else {
            EasyPermissions.requestPermissions(this, "First give Larry' New app internet permission", 15, Manifest.permission.INTERNET);
        }

        StartAppSDK.init(this, "your api key", true);
        StartAppAd.disableSplash();
        if (container != null && container.getChildCount() < 1) {
            container.addView(new Banner(this), new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));
            StartAppAd.showAd(this);
        }

        StartAppAd.showAd(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(15)
    public void permGranted() {
        getNewsWithKeyword("");
    }

    private void getNew(String keyword) {

        Call<News> newsCall = null;

        if (keyword.isEmpty()) {

            newsCall = apiInterface.getNews(Utils.getCountry(), ApiKey);

        } else {

            Map<String, String> map = new HashMap<>();
            map.put("q", keyword);
            map.put("language", Utils.getLanguage());
            map.put("sortBy", "publishedAt");
            map.put("apiKey", ApiKey);

            newsCall = apiInterface.searchNews(map);

        }

        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {

                if (response.isSuccessful() && response.body().getArticles() != null) {

                   articleList = new ArrayList<>(response.body().getArticles());
                    newsAdapter = new NewsAdapter(articleList, MainActivity.this);
                    recyclerView.setAdapter(newsAdapter);
                    Toast.makeText(MainActivity.this, "news gotten", Toast.LENGTH_SHORT).show();

                }
                swipeRefreshLayout.setRefreshing(false);
                fire.setVisibility(View.GONE);
                fireText.setVisibility(View.GONE);
                tryAgain.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(MainActivity.this, "unable to get news", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                fire.setVisibility(View.VISIBLE);
                fireText.setVisibility(View.VISIBLE);
                tryAgain.setVisibility(View.VISIBLE);
                articleList.clear();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_acivity_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Search your hot\uD83D\uDD25news");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                swipeRefreshLayout.setRefreshing(true);
                getNewsWithKeyword(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                swipeRefreshLayout.setRefreshing(true);
                getNewsWithKeyword(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;

        }
        return true;
    }

    public void getNewsWithKeyword(String keyword) {

        getNew(keyword);

    }

    public void TryAgain(View view) {
        swipeRefreshLayout.setRefreshing(true);
        getNewsWithKeyword("");
    }
}
