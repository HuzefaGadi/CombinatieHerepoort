package com.huzefa.combinatieherepoort.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rashida on 01/06/17.
 */

public class UserModel {
    
    private String id;
    private String name;
    private String email;
    @SerializedName("created_at")
    private String createdOn;
    @SerializedName("updated_at")
    private String updatedOn;
    private String duration;
    @SerializedName("api_token")
    private String apiToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
