package com.org.speakout;

import com.org.speakout.loginpage.LoginPage;
import com.org.speakout.model.RegistrationModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {



    @POST("register")
    Call<LoginPage.Response> registerUser(@Body RegistrationModel registrationModel);

    @POST("login")
    Call<LoginPage.Response> login(@Body RegistrationModel registrationModel);

    @POST("problem")
    Call<LoginPage.Problem> postProblem(@Body RegistrationModel registrationModel);

    @GET("splash")
    Call<LoginPage.Response> getSplash();



}
