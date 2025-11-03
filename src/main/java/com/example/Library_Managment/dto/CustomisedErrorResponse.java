package com.example.Library_Managment.dto;

public class CustomisedErrorResponse {
    private String message;

    // ✅ No-arg constructor
    public CustomisedErrorResponse() {

    }

    // ✅ Constructor with message
    public CustomisedErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
