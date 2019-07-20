package com.org.speakout.base;

import android.app.ProgressDialog;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.view.WindowManager;

import com.org.speakout.RetrofitInterface;
import com.org.speakout.Validator;
import com.org.speakout.constance.AppConstance;
import com.wisdomrider.Utils.Preferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseActivity extends com.wisdomrider.Activities.BaseActivity {
    protected  Preferences preferences ;
    protected  Validator validator = new Validator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences=new Preferences(this, AppConstance.APP_NAME, 0);
        disableKeyboardAtFirst();
    }

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://172.20.0.85:3000/api/";

    public  Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new TokenRenewIntercepter(preferences))
                    .build();
        }
        return retrofit;
    }

    ProgressDialog progressDialog;

    protected void showProgressBar() {
        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    protected  void hideProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public RetrofitInterface getRequest() {
        RetrofitInterface service = getRetrofitInstance().create(RetrofitInterface.class);
        return service;
    }


     class TokenRenewIntercepter extends OkHttpClient implements Interceptor {
        private Preferences preferences;
        public TokenRenewIntercepter(Preferences sharedPreferences) {
            this.preferences = sharedPreferences;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder newRequest = request.newBuilder().header("token", preferences.getString(AppConstance.TOKEN, ""));
            return chain.proceed(newRequest.build());
        }
    }

    protected void disableKeyboardAtFirst() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}
