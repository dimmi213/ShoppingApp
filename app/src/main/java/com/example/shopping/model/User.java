package com.example.shopping.model;

public class User {
    String userId, userEmail, userName, userPhoneNumber;

    public User(String userId, String userEmail, String userName, String userPhoneNumber) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImageUrl() {
        return userPhoneNumber;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.userPhoneNumber = profileImageUrl;
    }
}
