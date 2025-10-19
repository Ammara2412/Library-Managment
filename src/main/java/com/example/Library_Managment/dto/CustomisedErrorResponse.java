package com.example.Library_Managment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/*

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomisedErrorResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}*/

public class CustomisedErrorResponse {
    private String message;

    // ✅ No-arg constructor (needed for frameworks like Jackson)
    public CustomisedErrorResponse() {}

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
