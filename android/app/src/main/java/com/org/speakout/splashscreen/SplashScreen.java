package com.org.speakout.splashscreen;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.org.speakout.HomePageActivity;
import com.org.speakout.R;
import com.org.speakout.base.BaseActivity;
import com.org.speakout.constance.AppConstance;
import com.org.speakout.loginpage.LoginPage;
import com.org.speakout.model.RegistrationModel;
import com.org.speakout.model.Tags;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        isUserLogin();
       /* new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        }, AppConstance.SPLASH_DISPLAY_LENGTH);*/
    }

    private void isUserLogin() {
        // user is login
        if (!preferences.getString(AppConstance.TOKEN, "").equals("")) {
            openHomePage();
        } else {
            openLoginPage();
        }
    ;
    }

    private void openLoginPage() {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
        finish();
    }

    private void openHomePage() {
        createTables();
        getRequest().getSplash().enqueue(new Callback<RegistrationModel>() {
            @Override
            public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                if (response.code() == 200) {
                    insertintoTables(response.body().getTags());
                    Intent intent = new Intent(SplashScreen.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SplashScreen.this, "Sorry please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegistrationModel> call, Throwable t) {
                Toast.makeText(SplashScreen.this, "Onfalurar", Toast.LENGTH_SHORT).show();
            }
        });


    }

     @Override
    protected void insertintoTables(ArrayList<Tags> tags) {
        sqliteClosedHelper.insertAll(tags);


    }


}
