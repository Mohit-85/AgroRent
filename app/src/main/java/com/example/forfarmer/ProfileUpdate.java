package com.example.forfarmer;

import java.security.PrivateKey;

public class ProfileUpdate {

    private String profileId;
    private String name;
    private String phone;
    private String location;
    private String profileImageUrl;

    // Default constructor
    public ProfileUpdate() {
    }

    // Constructor
    public ProfileUpdate(String profileId, String name, String phone, String location, String profileImageUrl) {
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.profileImageUrl = profileImageUrl;
        this.profileId = profileId;
    }

    // Getters and Setters


    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
