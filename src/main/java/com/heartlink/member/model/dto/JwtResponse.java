package com.heartlink.member.model.dto;

public class JwtResponse {
    private String token;

    // 기본 생성자 (JSON 직렬화를 위해 필요)
    public JwtResponse() {}

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
