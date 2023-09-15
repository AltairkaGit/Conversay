package com.leopold.modules.auth.dto;

public class AuthRequestDto {
    private String username;
    private String email;
    private String gender;
    private String password;
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getGender() {
        return gender;
    }
    public String getPassword() {
        return password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
