package com.example.KhataWeb.Service;

import com.example.KhataWeb.Dtos.AuthRequest;
import com.example.KhataWeb.Dtos.SignupDto;
import com.example.KhataWeb.Models.User;
import com.example.KhataWeb.Repos.UserRepository;
import jakarta.persistence.AttributeOverride;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {


    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;

    public String register(SignupDto request, String role) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setNumber(request.getNumber());
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);
        return "User registered successfully";
    }

}