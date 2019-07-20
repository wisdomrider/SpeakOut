package com.org.speakout.base;

import android.os.Bundle;

import com.org.speakout.RetrofitInterface;
import com.org.speakout.Validator;
import com.org.speakout.constance.AppConstance;
import com.wisdomrider.Utils.Preferences;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseActivity extends com.wisdomrider.Activities.BaseActivity {
    protected  Preferences preferences ;
    protected  Validator validator = new Validator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences=new Preferences(this, AppConstance.APP_NAME, 0);

    }

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://172.20.0.85:3000/api/";

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
