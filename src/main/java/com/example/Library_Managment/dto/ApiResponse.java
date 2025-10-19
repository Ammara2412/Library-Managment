package com.example.Library_Managment.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ApiResponse {

    private String status;
    private String message;
    private String token; // Add token field

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}