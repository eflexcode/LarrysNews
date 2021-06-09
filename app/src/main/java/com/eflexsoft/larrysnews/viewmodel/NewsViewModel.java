package com.eflexsoft.larrysnews.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.larrysnews.api.ApiInterface;
import com.eflexsoft.larrysnews.datastore.CategoryDataStore;
import com.eflexsoft.larrysnews.model.News;
import com.eflexsoft.larrysnews.utils.NetworkResult;
import com.eflexsoft.larrysnews.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.eflexsoft.larrysnews.utils.NetworkResult.Success;
import com.eflexsoft.larrysnews.utils.NetworkResult.Error;

import org.jetbrains.annotations.NotNull;

import static com.eflexsoft.larrysnews.utils.Constants.ApiKey;

@HiltViewModel
public class NewsViewModel extends AndroidViewModel {

    MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<NetworkResult> resultMutableLiveData = new MutableLiveData<NetworkResult>();

    @Inject
    CategoryDataStore categoryDataStore;

    @Inject
    ApiInterface apiInterface;

    @Inject
    Retrofit retrofit;

    private static final String TAG = "NewsViewModel";

    Context context;

    @Inject
    public NewsViewModel(Application application) {
        super(application);

        context = application;

    }

    public void saveNewCategory(String newCategory) {
        categoryDataStore.saveNewCategory(newCategory);
    }

    public Flowable<String> getCurrentCategory() {
        return categoryDataStore.readCategory();
    }

    public LiveData<String> getStringMutableLiveData() {
        categoryDataStore.readCategory();
        stringMutableLiveData = categoryDataStore.categoryString;
        return stringMutableLiveData;
    }

    public void getNew(String country) {

        Log.d(TAG, "getNew: ");
        apiInterface = retrofit.create(ApiInterface.class);

        Call<News> newsCall;

//        if (country.trim().isEmpty()) {
        newsCall = apiInterface.getNews(Utils.getCountry(), ApiKey);
//        } else {
//            newsCall = apiInterface.getNews(country, ApiKey);
//        }

        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NotNull Call<News> call, @NotNull Response<News> response) {

                Log.d(TAG, "onResponse: " + response.body().getArticles().size());

                if (response.code() == 402) {

                    Error error = new Error("API key limited");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 401) {

                    Error error = new Error("Unauthorized");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 429) {

                    Error error = new Error("Too Many Requests");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 403) {

                    Error error = new Error("Request Forbidden");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 404) {

                    Error error = new Error("Request Not Found");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 400) {

                    Error error = new Error("Bad Request");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 500) {

                    Error error = new Error("Server Error");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.isSuccessful() && response.body().getArticles() != null) {

                    Log.d(TAG, "onResponse: " + response.body().getArticles().size());

                    News news = response.body();

                    Success success = new Success(news);

                    NetworkResult successNetworkResult = new NetworkResult(success);

                    resultMutableLiveData.setValue(successNetworkResult);

//                    articleList = new ArrayList<>(response.body().getArticles());
//                    newsAdapter.setArticleList(articleList, MainActivity.this);
//
//                    Log.d(TAG, String.valueOf(response.body().getArticles().size()));
//                    binding.recyclerView.setAdapter(newsAdapter);
//                    newsAdapter.notifyDataSetChanged();
                }
//                binding.fire.setVisibility(View.GONE);
//                binding.fireMessage.setVisibility(View.GONE);
//                binding.fireTry.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<News> call, @NotNull Throwable t) {

                Toast.makeText(context, "unable to get news", Toast.LENGTH_SHORT).show();

                Error stringError = new Error(t.getMessage());
                NetworkResult networkResult = new NetworkResult(stringError);

                resultMutableLiveData.setValue(networkResult);

                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });

    }

    public void searchNews(String keyword, String country) {

        Map<String, String> map = new HashMap<>();

        if (country.trim().isEmpty()) {
            map.put("language", Utils.getLanguage());
        } else {
            map.put("language", country);
        }

        map.put("q", keyword);
        map.put("sortBy", "publishedAt");
        map.put("apiKey", ApiKey);

        apiInterface = retrofit.create(ApiInterface.class);

        Call<News> newsCall = apiInterface.searchNews(map);

        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NotNull Call<News> call, @NotNull Response<News> response) {

                if (response.code() == 402) {

                    Error error = new Error("API key limited");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 403) {

                    Error error = new Error("Request Forbidden");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 404) {

                    Error error = new Error("Request Not Found");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 400) {

                    Error error = new Error("Bad Request");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 500) {

                    Error error = new Error("Server Error");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.isSuccessful() && response.body().getArticles() != null) {

                    Log.d(TAG, "onResponse: " + response.message());

                    News news = response.body();

                    Success success = new Success(news);

                    NetworkResult successNetworkResult = new NetworkResult(success);

                    resultMutableLiveData.setValue(successNetworkResult);

                }
            }

            @Override
            public void onFailure(@NotNull Call<News> call, @NotNull Throwable t) {
                Toast.makeText(context, "unable to get news", Toast.LENGTH_SHORT).show();

                Error stringError = new Error(t.getMessage());
                NetworkResult networkResult = new NetworkResult(stringError);

                resultMutableLiveData.setValue(networkResult);

                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void searchNewsWithCategory(String category, String country) {

        if (country.isEmpty()) {
            country = Utils.getLanguage();
        }

        apiInterface = retrofit.create(ApiInterface.class);

        Call<News> newsCall = apiInterface.getNewsWithCategory(country, category, ApiKey);

        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NotNull Call<News> call, @NotNull Response<News> response) {
                Log.d(TAG, "searchNewsWithCategory: " + response.body().getArticles().size());
                if (response.code() == 402) {

                    Error error = new Error("API key limited");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 403) {

                    Error error = new Error("Request Forbidden");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 404) {

                    Error error = new Error("Request Not Found");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 400) {

                    Error error = new Error("Bad Request");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.code() == 500) {

                    Error error = new Error("Server Error");

                    NetworkResult successNetworkResult = new NetworkResult(error);

                    resultMutableLiveData.setValue(successNetworkResult);

                }

                if (response.isSuccessful() && response.body().getArticles() != null) {

                    Log.d(TAG, "onResponse: " + response.message());

                    News news = response.body();

                    Success success = new Success(news);

                    NetworkResult successNetworkResult = new NetworkResult(success);

                    resultMutableLiveData.setValue(successNetworkResult);

                }
            }

            @Override
            public void onFailure(@NotNull Call<News> call, @NotNull Throwable t) {
                Toast.makeText(context, "unable to get news", Toast.LENGTH_SHORT).show();

                Error stringError = new Error(t.getMessage());
                NetworkResult networkResult = new NetworkResult(stringError);

                resultMutableLiveData.setValue(networkResult);

                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }
}
