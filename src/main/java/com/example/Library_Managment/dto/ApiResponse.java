package com.example.Library_Managment.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ApiResponse {

    private String status;
    private String message;
    private String token;
    private Long Id; // // Add token field

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

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