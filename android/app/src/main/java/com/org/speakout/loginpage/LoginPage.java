package com.org.speakout.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.org.speakout.HomePageActivity;
import com.org.speakout.R;
import com.org.speakout.base.BaseActivity;
import com.org.speakout.model.RegistrationModel;
import com.org.speakout.model.Tags;
import com.org.speakout.registration.RegistrationActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends BaseActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        validator.add(wisdom.editText(R.id.userName));
        validator.add(wisdom.editText(R.id.password));
        wisdom.button(R.id.login_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        wisdom.button(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validator.validate()) return;
                login();
            }
        });
    }


    private void login() {
        showProgressBar();
        RegistrationModel registrationModel = new RegistrationModel();
        registrationModel.setPassword(wisdom.editText(R.id.password).getText().toString());
        registrationModel.setUserName(wisdom.editText(R.id.userName).getText().toString());
        createTables();
        getRequest().login(registrationModel).enqueue(new Callback<RegistrationModel>() {
            @Override
            public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                if (response.code() == 200) {
                    insertintoTables(response.body().getTags());
                    Intent intent = new Intent(LoginPage.this, HomePageActivity.class);
                    hideProgressBar();
                    startActivity(intent);
                } else  {
                    Toast.makeText(LoginPage.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                }

            }

            @Override
            public void onFailure(Call<RegistrationModel> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(LoginPage.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void insertintoTables(ArrayList<Tags> tags) {
        sqliteClosedHelper.insertAll(tags);
    }
}
