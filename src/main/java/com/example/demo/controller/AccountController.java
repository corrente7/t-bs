package com.example.demo.controller;

import com.example.demo.dto.AccountDto;
import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list of accounts")
    })
    @GetMapping(path = "")
    public List<Account> getAccounts() {

        return accountService.getAccounts();
    }


    @Operation(summary = "Transfer money from one account to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successfully executed"),
            @ApiResponse(responseCode = "422", description = "Data not valid")
    })

    @PutMapping(path = "/{id}/transfer")
    public List<Account> transfer(@RequestBody AccountDto accountDto, @PathVariable long id) {
        return accountService.transfer(accountDto, id);
    }

}
