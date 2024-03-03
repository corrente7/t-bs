package com.example.demo.controller;

import com.example.demo.dto.LoginDto;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthController {

    @Autowired
    private AuthService authServiceImpl;

    @Operation(summary = "Log in user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Log in user")
    })

    @PostMapping(path = "/api/login")

    public String authUser(@Valid @RequestBody LoginDto requestDto) {
        return authServiceImpl.authenticate(requestDto);

    }

}

