package com.org.speakout.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.org.speakout.HomePageActivity;
import com.org.speakout.R;
import com.org.speakout.base.BaseActivity;
import com.org.speakout.constance.AppConstance;
import com.org.speakout.model.RegistrationModel;
import com.org.speakout.model.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends BaseActivity {
    Button registerButton;
    EditText nameTextView, emailTextView, passwordTextView, phoneNumberTextView;
    List<String> genderList = new ArrayList<>();
    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registerButton = findViewById(R.id.button_register);
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        wisdom.spinner(R.id.gender_spinner).
                setAdapter(new ArrayAdapter<String>(this, R.layout.custom_spinner, genderList));
        wisdom.spinner(R.id.gender_spinner).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    gender = "";
                 } else  if(position == 1){
                    gender = AppConstance.MALE;

                } else {
                    gender = AppConstance.FEMALE;
                }
                Toast.makeText(RegistrationActivity.this, gender, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        validator.add(wisdom.editText(R.id.fullNameText));
        validator.add(wisdom.editText(R.id.emailText));
        validator.add(wisdom.editText(R.id.phoneNumberText));
        validator.add(wisdom.editText(R.id.password));
        validator.add(wisdom.editText(R.id.confrimPasswordText));
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validator.validate())
                    return;
                showProgressBar();
                final RegistrationModel registrationModel = new RegistrationModel();
                registrationModel.setGender(gender);
                registrationModel.setName(wisdom.editText(R.id.fullNameText).getText().toString());
                registrationModel.setEmail(wisdom.editText(R.id.emailText).getText().toString());
                registrationModel.setPhoneNumber(wisdom.editText(R.id.phoneNumberText).getText().toString());
                registrationModel.setPassword(wisdom.editText(R.id.password).getText().toString());
                createTables();
                getRequest().registerUser(registrationModel).enqueue(new Callback<RegistrationModel>() {
                    @Override
                    public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                        if (response.code() == 200) {
                            insertintoTables(response.body().getTags());
                            preferences.edit().putString(AppConstance.TOKEN, response.body().getData().getToken()).apply();
                            preferences.edit().putString(AppConstance.ROLE, "USER").apply();
                            hideProgressBar();
                            Intent intent = new Intent(RegistrationActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            hideProgressBar();
                        }
                    }
                    @Override
                    public void onFailure(Call<RegistrationModel> call, Throwable t) {
                        Toast.makeText(RegistrationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                    }
                });
            }
        });
    }

    @Override
    protected void insertintoTables(ArrayList<Tags> tags) {
        sqliteClosedHelper.insertAll(tags);
    }
}
