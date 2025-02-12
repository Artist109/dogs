package com.example.dogs;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "MainViewModel";
    private MutableLiveData<DogImage> dogImage = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isError = new MutableLiveData<>();

    public LiveData<DogImage> getDogImage() {
        return dogImage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsError() {
        return isError;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void loadDogImage() {
        Disposable disposable = ApiFactory.getApiService().loadDogImage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isError.setValue(false);
                        isLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .subscribe(new Consumer<DogImage>() {
                    @Override
                    public void accept(DogImage image) throws Throwable {
                        Log.d(TAG, "image: " + image);
                        dogImage.setValue(image);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Throwable {
                        isError.setValue(true);
                    }
                });
        compositeDisposable.add(disposable);
    }

//    private Single<DogImage> loadDogImageRx() {
//        return ApiFactory.getApiService().loadDogImage();
//    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
