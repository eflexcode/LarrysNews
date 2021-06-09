package com.eflexsoft.larrysnews.datastore;

import android.content.Context;
import android.util.Log;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.larrysnews.utils.Constants;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;


import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.eflexsoft.larrysnews.utils.Constants.DATA_STORE_CATEGORY_KEY;

@ActivityRetainedScoped
public class CategoryDataStore {

    private Context context;
    RxDataStore<Preferences> dataStore;
    private static final String TAG = "CategoryDataStore";
    String category;
    MutablePreferences mutablePreferences;

    public MutableLiveData<String> categoryString = new MutableLiveData<>();

    @Inject
    public CategoryDataStore(@ApplicationContext Context context) {
        this.context = context;
        dataStore = new RxPreferenceDataStoreBuilder(context, Constants.DATA_STORE_NAME).build();
    }

    public void saveNewCategory(String newCategory) {

        Single<Preferences> preferencesSingle = dataStore.updateDataAsync(data -> {
            MutablePreferences mutablePreferences = data.toMutablePreferences();
            Preferences.Key<String> mKey = PreferencesKeys.stringKey(DATA_STORE_CATEGORY_KEY);

            mutablePreferences.set(mKey, newCategory);

            return Single.just(mutablePreferences);
        });

//preferencesSingle.

    }

    public Flowable<String> readCategory() {

        Preferences.Key<String> mKey = PreferencesKeys.stringKey(DATA_STORE_CATEGORY_KEY);

         dataStore.data().map(prefs -> prefs.get(mKey)).subscribeOn(Schedulers.io()).subscribe(new Subscriber<String>() {
             @Override
             public void onSubscribe(Subscription s) {

             }

             @Override
             public void onNext(String s) {
                 Log.d(TAG, "onNext: "+s);
             }

             @Override
             public void onError(Throwable t) {

             }

             @Override
             public void onComplete() {

             }
         });

        return  dataStore.data().map(prefs -> prefs.get(mKey));

    }

}