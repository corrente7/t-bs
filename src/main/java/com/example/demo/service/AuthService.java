package com.example.demo.service;

import com.example.demo.dto.LoginDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtTokenUtil jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String authenticate(LoginDto requestDto) {
        User existedUser = userRepository.findUserByLogin(requestDto.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("Sign in failed. User not found!"));
        var auth = new UsernamePasswordAuthenticationToken(
                requestDto.getLogin(), requestDto.getPassword());
        authenticationManager.authenticate(auth);
        return jwtUtils.generateToken(existedUser);
    }
}
