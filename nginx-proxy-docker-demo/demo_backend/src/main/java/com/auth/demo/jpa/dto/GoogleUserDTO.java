package com.auth.demo.jpa.dto;

public class GoogleUserDTO {
    private String googleEmail;
    private String googleId;

    // Constructors, getters, and setters

    public String getGoogleEmail() {
        return this.googleEmail;
    }
    public void setGoogleEmail(String googleEmail) {
        this.googleEmail = googleEmail;
    }

    public String getGoogleId() {
        return this.googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}