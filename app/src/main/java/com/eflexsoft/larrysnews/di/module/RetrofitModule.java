package com.eflexsoft.larrysnews.di.module;

import com.eflexsoft.larrysnews.api.ApiInterface;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.scopes.ViewModelScoped;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ViewModelComponent.class)
public class RetrofitModule {

    private static final String Base_url = "https://newsapi.org/v2/";
    private static Retrofit retrofit;

    @Provides
    public static Retrofit provideRetrofit(){

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    @Provides
    public static ApiInterface providesApiInterface(Retrofit retrofit){
        return retrofit.create(ApiInterface.class);
    }

}
