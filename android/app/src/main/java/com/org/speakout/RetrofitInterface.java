package com.org.speakout;

import com.org.speakout.model.RegistrationModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("register")
    Call<RegistrationModel> registerUser(@Body RegistrationModel registrationModel);

    @POST("login")
    Call<RegistrationModel> login(@Body RegistrationModel registrationModel);

}
