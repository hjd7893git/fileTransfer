package com.fileTransfer.custom.model;

/**
 * Created by nano on 18-4-12.
 */
public class AuthResponse {

    private String username;
    private String timestamp;
    private String hashpass;
    private String sign;

    public AuthResponse() {
        super();
    }

    public AuthResponse(String username, String timestamp, String hashpass, String sign, String token) {
        super();
        this.username = username;
        this.timestamp = timestamp;
        this.hashpass = hashpass;
        this.sign = sign;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHashpass() {
        return hashpass;
    }

    public void setHashpass(String hashpass) {
        this.hashpass = hashpass;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
