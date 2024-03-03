package com.example.demo.controller;

import com.example.demo.dto.EmailDto;
import com.example.demo.model.Email;
import com.example.demo.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Operation(summary = "Get all emails")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get list of emails")
    })
    @GetMapping(path = "")
    public List<Email> getEmails() {

        return emailService.getEmails();
    }

    @Operation(summary = "Add new email to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Email successfully added"),
            @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public Email addEmail(@Valid @RequestBody EmailDto emailDto) {

        return emailService.addEmail(emailDto);
    }


    @Operation(summary = "Update user's email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email successfully updated"),
            @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @PutMapping(path = "/{id}")
    public Email updateEmail(@RequestBody EmailDto emailDto, @PathVariable long id) {
        return emailService.updateEmail(emailDto, id);
    }

    @Operation(summary = "Delete user's email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delete email by owner")
    })
    @DeleteMapping(path = "/{id}")
    public void deleteEmail(@PathVariable long id) {

        emailService.deleteEmail(id);
    }
}
