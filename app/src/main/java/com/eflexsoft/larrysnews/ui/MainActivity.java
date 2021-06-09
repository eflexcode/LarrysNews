package com.eflexsoft.larrysnews.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.eflexsoft.larrysnews.R;
import com.eflexsoft.larrysnews.adapter.NewsAdapter;
import com.eflexsoft.larrysnews.databinding.ActivityMainBinding;
import com.eflexsoft.larrysnews.model.Article;
import com.eflexsoft.larrysnews.model.News;
import com.eflexsoft.larrysnews.ui.fragment.CountryFragmentBottomSheet;
import com.eflexsoft.larrysnews.utils.Constants;
import com.eflexsoft.larrysnews.utils.NetworkResult;
import com.eflexsoft.larrysnews.viewmodel.NewsViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pub.devrel.easypermissions.EasyPermissions;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    NewsAdapter newsAdapter;

    List<Article> articleList;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;

    String country = "    ";

    String category = "";

    boolean isSearch;
    boolean isCategory;

    String keyword = "";

    ActivityMainBinding binding;

    NewsViewModel viewModel;

    RxDataStore<Preferences> dataStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        dataStore = new RxPreferenceDataStoreBuilder(this, Constants.DATA_STORE_NAME).build();

        binding.swipe.setColorSchemeResources(R.color.colorAccent);
        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipe.setRefreshing(false);
                binding.recyclerView.showShimmer();
                getNewsIfKeywordIsEmptyOrNot(keyword);
            }
        });

        setSupportActionBar(binding.toolbar);

        binding.recyclerView.showShimmer();

        articleList = new ArrayList<>();

        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        viewModel.getNew(country);

        binding.recyclerView.setAdapter(newsAdapter);

        binding.general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = binding.general.getText().toString().toLowerCase();
                viewModel.searchNewsWithCategory(category, country);
                binding.recyclerView.showShimmer();
            }
        });


        binding.business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = binding.business.getText().toString().toLowerCase();
                viewModel.searchNewsWithCategory(category, country);
                binding.recyclerView.showShimmer();
            }
        });

        binding.science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = binding.science.getText().toString().toLowerCase();
                viewModel.searchNewsWithCategory(category, country);
                binding.recyclerView.showShimmer();
            }
        });

        binding.entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = binding.entertainment.getText().toString().toLowerCase();
                viewModel.searchNewsWithCategory(category, country);
                binding.recyclerView.showShimmer();
            }
        });

        binding.sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = binding.sports.getText().toString().toLowerCase();
                viewModel.searchNewsWithCategory(category, country);
                binding.recyclerView.showShimmer();
            }
        });
        binding.technology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = binding.technology.getText().toString().toLowerCase();
                viewModel.searchNewsWithCategory(category, country);
                binding.recyclerView.showShimmer();
            }
        });


        viewModel.resultMutableLiveData.observe(this, new Observer<NetworkResult>() {
            @Override
            public void onChanged(NetworkResult result) {

                if (result.getData() instanceof NetworkResult.Success) {

                    binding.recyclerView.hideShimmer();
                    binding.fire.setVisibility(View.GONE);
                    binding.fireMessage.setVisibility(View.GONE);
                    binding.fireTry.setVisibility(View.GONE);

                    NetworkResult.Success success = (NetworkResult.Success) result.getData();

                    News news = (News) success.getData();

                    newsAdapter.setArticleList(news.getArticles(), MainActivity.this);

                    try {
                        binding.recyclerView.scrollToPosition(0);
                    } catch (Exception e) {

                    }

                    Log.d(TAG, "onChanged: " + NetworkResult.Success.class.getName().toString());

                } else {

                    binding.recyclerView.hideShimmer();

                    binding.fire.setVisibility(View.VISIBLE);
                    binding.fireTry.setVisibility(View.VISIBLE);

                    NetworkResult.Error error = (NetworkResult.Error) result.getData();
//                    articleList.clear();
//                    binding.recyclerView.setAdapter(newsAdapter);
                    binding.fireMessage.setText(error.getData().toString());
                    binding.fireMessage.setVisibility(View.VISIBLE);

                    Log.d(TAG, "onChanged: " + error.getData());

                    Log.d(TAG, "onChanged: " + NetworkResult.Error.class.getName().toString());

                }
            }
        });

////        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
////        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
////


//        retrofit = ApiClient.getRetrofit();
//        apiInterface = retrofit.create(ApiInterface.class);


////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED ||
////                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
////                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
////
////            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 89);
////
////        }
////        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
////            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////            startActivity(intent);
////            Toast.makeText(this, "Please turn on your device location", Toast.LENGTH_SHORT).show();
////
////        }
////
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
////                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
////
////
////            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
////
////                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
////                    @Override
////                    public void onSuccess(Location location) {
////
////                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
////
////
////                        try {
////
////                            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
////
////                            if (addressList != null) {
////
////                                Address theAddress = addressList.get(0);
////
////                                country = theAddress.getCountryCode();

////
////                            } else {
////                                Toast.makeText(MainActivity.this, "No address found", Toast.LENGTH_SHORT).show();
////                            }
////
////
////                        } catch (Exception e) {
////
////
////                        }
////
////
////                    }
////                }).addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception e) {
//////                        getNew("");
////                    }
////                });
////
////
////            } else {
////                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                startActivity(intent);
////                Toast.makeText(this, "Please turn on your device location", Toast.LENGTH_SHORT).show();
////
////            }
////
////
////        } else {
////            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 89);
////
////        }
////
//
//        swipeRefreshLayout.setRefreshing(true);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getNewsWithKeyword("");
//            }
//        });
//
//
        StartAppSDK.init(this, "207451295", true);
        StartAppAd.disableSplash();
        StartAppSDK.enableReturnAds(false);
        if (binding.fragmentMain != null && binding.fragmentMain.getChildCount() < 1) {
            binding.fragmentMain.addView(new Banner(this), new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));
            StartAppAd.showAd(this);
        }

        StartAppAd.showAd(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        if (requestCode == 89 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            getNew("");
        }

    }

    private static final String TAG = "MainActivity";

//    private void getNew(String keyword, String category) {
//
//        Call<News> newsCall = null;
//
//        if (keyword.isEmpty()) {
//
//            if (country != null) {
//                newsCall = apiInterface.getNews(country, ApiKey);
//            } else {
//
//                newsCall = apiInterface.getNews(Utils.getCountry(), ApiKey);
//            }
//        } else {
//
//            Map<String, String> map = new HashMap<>();
//            map.put("q", keyword);
//            map.put("language", Utils.getLanguage());
//            map.put("sortBy", "publishedAt");
//            map.put("apiKey", ApiKey);
//
//            newsCall = apiInterface.searchNews(map);
//
//        }
//
//        newsCall.enqueue(new Callback<News>() {
//            @Override
//            public void onResponse(Call<News> call, Response<News> response) {
//
//                if (response.isSuccessful() && response.body().getArticles() != null) {
//
//                    articleList = new ArrayList<>(response.body().getArticles());
//                    newsAdapter.setArticleList(articleList, MainActivity.this);
//
//                    Log.d(TAG, String.valueOf(response.body().getArticles().size()));
//                    binding.recyclerView.setAdapter(newsAdapter);
//                    newsAdapter.notifyDataSetChanged();
//
//                }
//                binding.fire.setVisibility(View.GONE);
//                binding.fireMessage.setVisibility(View.GONE);
//                binding.fireTry.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(Call<News> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "unable to get news", Toast.LENGTH_SHORT).show();
//                binding.recyclerView.hideShimmer();
//                binding.fire.setVisibility(View.VISIBLE);
//                binding.fireMessage.setVisibility(View.VISIBLE);
//                binding.fireTry.setVisibility(View.VISIBLE);
//                try {
//
//                } catch (Exception e) {
//                }
//
//            }
//        });
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_acivity_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Search your hot\uD83D\uDD25news");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.trim().isEmpty()) {
                    binding.recyclerView.showShimmer();
                    keyword = query;
                    getNewsIfKeywordIsEmptyOrNot(keyword);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                keyword = "";
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.about:

                AestheticDialog builder = new AestheticDialog.Builder(this, DialogStyle.TOASTER, DialogType.WARNING)
                        .setTitle("News License")
                        .setMessage("For news license visit link below" + "\nhttps://newsapi.org/terms")
                        .setDarkMode(false)
                        .setGravity(Gravity.CENTER)
                        .setCancelable(true)
                        .setAnimation(DialogAnimation.SLIDE_LEFT)
                        .show();

//                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;

        }
        return true;
    }

    public void getNewsIfKeywordIsEmptyOrNot(String keyword) {

        if (!keyword.trim().isEmpty()) {
            viewModel.searchNews(keyword, country);
        } else if (!category.trim().isEmpty()) {
            viewModel.searchNewsWithCategory(category, country);
        } else {
            viewModel.getNew(country);
        }

    }

    public void TryAgain(View view) {
        getNewsIfKeywordIsEmptyOrNot(keyword);
        binding.fire.setVisibility(View.GONE);
        binding.fireMessage.setVisibility(View.GONE);
        binding.fireTry.setVisibility(View.GONE);
        binding.recyclerView.showShimmer();
    }

    public void openCountrySelector(View view) {

        CountryFragmentBottomSheet fragmentBottomSheet = new CountryFragmentBottomSheet();
        fragmentBottomSheet.show(getSupportFragmentManager(), "country");

    }
}
