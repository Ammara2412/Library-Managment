package com.example.Library_Managment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResetPasswordRequwst {

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank(message = "Email is mandatory")
    @NotEmpty(message = "Email field can't be empty")
    @Email(message = "Invalid email format")
    private String email;
}
