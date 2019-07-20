package com.org.speakout.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.org.speakout.R;
import com.org.speakout.base.BaseActivity;
import com.org.speakout.constance.AppConstance;
import com.org.speakout.loginpage.LoginPage;

public class SplashScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        }, AppConstance.SPLASH_DISPLAY_LENGTH);
    }
}
