package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserParamsDto;
import com.example.demo.model.User;
import com.example.demo.service.UserDetailsServiceImpl;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Operation(summary = "Search users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filter users")
    })
    @GetMapping(path = "/search")
    public Page<User> filter(UserParamsDto params, @RequestParam(defaultValue = "1") int page) {

        return userService.filter(params, page);
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list of all users")
    })
    @GetMapping(path = "")
    public List<User> getUsers() {

        return userService.getUsers();
    }

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get a specific user by id")
    })
    @GetMapping(path = "/{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User successfully created"),
        @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);

    }

}
