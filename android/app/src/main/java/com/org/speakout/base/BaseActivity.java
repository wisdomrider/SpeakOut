package com.org.speakout.base;

import android.os.Bundle;

import com.org.speakout.RetrofitInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseActivity extends com.wisdomrider.Activities.BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public RetrofitInterface getRequest() {
        RetrofitInterface service = getRetrofitInstance().create(RetrofitInterface.class);
        return service;
    }

}
