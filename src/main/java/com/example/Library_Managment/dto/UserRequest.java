
package com.example.Library_Managment.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
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

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Password is required.")
    @NotEmpty(message = "Password can't be empty")
    @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters.")
    @Pattern(regexp = ".*[!@#$%^&*()_+{}|:<>?].*", message = "Password must contain at least 1 special character.")
    private String password;
}
