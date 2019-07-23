package com.org.speakout.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.org.speakout.loginpage.LoginPage;

import java.util.ArrayList;

public class RegistrationModel {

    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String success;
    private String gender;
    private String photo;

    private String tag;


    public ArrayList<LoginPage.Tag> tags;


    public ArrayList<LoginPage.Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<LoginPage.Tag> tags) {
        this.tags = tags;
    }


    public String getPhoto() {
        return photo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String title;
    private String desc;
    private String timestamp;
    private String location;
    @Expose
    @SerializedName("username")
    private String userName;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private Data data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
