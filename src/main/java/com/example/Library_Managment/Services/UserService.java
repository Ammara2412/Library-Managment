package com.example.Library_Managment.Services;

import com.example.Library_Managment.dto.UserRegisterRequest;
import com.example.Library_Managment.dto.ChangePasswordRequest;
import com.example.Library_Managment.dto.ResetPasswordRequwst;
import com.example.Library_Managment.dto.UserRequest;
import com.example.Library_Managment.exception.InvalidPasswordException;
import com.example.Library_Managment.exception.UserAlreadyExistsException;
import com.example.Library_Managment.exception.UserNotFoundException;
import com.example.Library_Managment.model.userregister;
import com.example.Library_Managment.repository.UserRepository;
import com.example.Library_Managment.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service

public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
   private JwtUtil jwtUtil;

    public userregister register(UserRegisterRequest registerRequest) {
        Optional<userregister> userOptional = userRepository.findByEmail(registerRequest.getEmail());
        if(userOptional.isPresent()){
            throw new UserAlreadyExistsException("User Already Exists with this Email");
        }

        userregister user = new userregister();
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPassword(registerRequest.getPassword());
        return userRepository.save(user);


    }

    public String login(@Valid UserRequest request) {
        Optional<userregister> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            userregister user = userOptional.get();
            if (user.getPassword().equals(request.getPassword())) {
                return jwtUtil.generateToken(user.getEmail(), user.getId());
            } else {
                throw new InvalidPasswordException("Incorrect password");
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public userregister getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Change user password
    public userregister changePassword(ChangePasswordRequest request) {
        Optional<userregister> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()){
            userregister userPass = userOpt.get();
            // Check if the old password matches
            if (!request.getOldPassword().equals(userPass.getPassword())) {
                throw new IllegalArgumentException("Old password is incorrect");
            }
            userPass.setPassword(request.getNewPassword());
            return userRepository.save(userPass);
        }else {
            throw new UserNotFoundException("User not found with the provided email.");
        }
    }

    //Reset to random password
    public userregister resetPassword(ResetPasswordRequwst request) {
        userregister user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("This Email is not registered"));

        // Generate a random password
        String newPassword = generateRandomPassword(10);

        // Encode and update the new password in the database
        user.setPassword(newPassword);
        return userRepository.save(user);

    }

    private String generateRandomPassword(int length) {
        final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final String SPECIAL_CHARS = "!@#$%^&*()";

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        // Ensure at least one special character is included
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        // Generate the rest of the password
        for (int i = 1; i < length; i++) {
            password.append(CHAR_SET.charAt(random.nextInt(CHAR_SET.length())));
        }

        return password.toString();
    }

}






