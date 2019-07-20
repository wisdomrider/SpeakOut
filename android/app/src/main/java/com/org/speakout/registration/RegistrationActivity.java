package com.org.speakout.registration;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.org.speakout.R;
import com.org.speakout.base.BaseActivity;
import com.org.speakout.model.RegistrationModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends BaseActivity {
    Button registerButton;

    EditText nameTextView, emailTextView, passwordTextView, phoneNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registerButton = findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationModel registrationModel = new RegistrationModel();
                registrationModel.setName(wisdom.editText(R.id.fullNameText).getText().toString());
                registrationModel.setEmail(wisdom.editText(R.id.emailText).getText().toString());
                registrationModel.setPhoneNumber(wisdom.editText(R.id.phoneNumberText).getText().toString());
                registrationModel.setPassword(wisdom.editText(R.id.password).getText().toString());
                getRequest().registerUser(registrationModel).enqueue(new Callback<RegistrationModel>() {
                    @Override
                    public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                        Toast.makeText(RegistrationActivity.this, "Registration sucessfully", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onFailure(Call<RegistrationModel> call, Throwable t) {
                        Log.e("Error", t.getMessage());
                        Toast.makeText(RegistrationActivity.this, "Error on Registration", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
}