package com.huzefa.combinatieherepoort.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rashida on 01/06/17.
 */

public class LoginModel {

    private String status;
    @SerializedName("api_token")
    private String token;
    @SerializedName("sessid")
    private String sessionId;
    @SerializedName("session_name")
    private String sessionName;
    @SerializedName("email")
    private String email;
    private UserModel user;
    private String uid;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
