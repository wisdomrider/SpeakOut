package com.org.speakout.base;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;

import com.org.speakout.RetrofitInterface;
import com.org.speakout.Validator;
import com.org.speakout.constance.AppConstance;
import com.org.speakout.model.Tags;
import com.wisdomrider.Utils.Preferences;
import com.wisdomrider.sqliteclosedhelper.SqliteClosedHelper;

import java.io.IOException;
import java.util.ArrayList;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseActivity extends com.wisdomrider.Activities.BaseActivity {
    protected  Preferences preferences ;
    public SqliteClosedHelper sqliteClosedHelper;
    public static final int GALLERY_REQUEST = 256;
    public static final int CAMARA_REQUEST = 109;
    protected  Validator validator = new Validator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences=new Preferences(this, AppConstance.APP_NAME, 0);
        sqliteClosedHelper = new SqliteClosedHelper(this, AppConstance.APP_NAME);
        disableKeyboardAtFirst();
    }


    private static Retrofit retrofit;
    private static final String BASE_URL = "http://82c32494.ngrok.io/api/";

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


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    protected boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("location permision")
                        .setMessage("We need your location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(BaseActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public void createTables() {
        sqliteClosedHelper.createTable(new Tags());

    }


    public void openIssueActivity() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        openIssueActivity();
                        //Request location updates:

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    protected void insertintoTables(ArrayList<Tags> tags) {


    }




}
