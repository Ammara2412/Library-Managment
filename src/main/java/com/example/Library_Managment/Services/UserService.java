package com.example.Library_Managment.Services;

import com.example.Library_Managment.dto.UserRegisterRequest;

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

}






