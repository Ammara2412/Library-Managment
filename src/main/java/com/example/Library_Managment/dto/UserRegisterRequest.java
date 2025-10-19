package com.example.Library_Managment.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    private String firstName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    private String lastName;
    @Email(message = "Email should be valid")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "Email should be a valid address (e.g., example@mail.com)")
    private String email;
    @NotEmpty(message = "Password can't be empty")
    @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters.")
    @Pattern(regexp = ".*[!@#$%^&*()_+{}|:<>?].*", message = "Password must contain at least 1 special character.")
    @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least 1 number.")
    private String password;
}






