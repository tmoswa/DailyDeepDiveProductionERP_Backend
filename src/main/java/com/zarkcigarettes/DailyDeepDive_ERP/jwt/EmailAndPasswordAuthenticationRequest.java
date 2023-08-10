package com.zarkcigarettes.DailyDeepDive_ERP.jwt;

public class EmailAndPasswordAuthenticationRequest {
    private String email;
    private String password;

    public EmailAndPasswordAuthenticationRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
