package com.org.speakout.loginpage;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.org.speakout.R;
import com.org.speakout.registration.RegistrationActivity;

public class LoginPage extends AppCompatActivity  {
    TextView userRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        userRegistration = findViewById(R.id.user_registration);
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }


}
