package com.example.demo.controller;

import com.example.demo.dto.TelephoneDto;
import com.example.demo.model.Telephone;
import com.example.demo.service.TelephoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/telephones")
public class TelephoneController {

    @Autowired
    private TelephoneService telephoneService;

    @Operation(summary = "Get all telephones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Get list of telephones")
    })
    @GetMapping(path = "")
    public List<Telephone> getTelephones() {

        return telephoneService.getTelephones();
    }

    @Operation(summary = "Add new telephone to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Telephone successfully added"),
            @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public Telephone addTelephone(@RequestBody TelephoneDto telephoneDto) {

        return telephoneService.addTelephone(telephoneDto);
    }

    @Operation(summary = "Update user's telephone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telephone successfully updated"),
            @ApiResponse(responseCode = "422", description = "Data not valid")
    })
    @PutMapping(path = "/{id}")
    public Telephone updateLabel(@RequestBody TelephoneDto telephoneDto, @PathVariable long id) {
        return telephoneService.updateTelephone(telephoneDto, id);
    }

    @Operation(summary = "Delete user's telephone")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Delete telephone by owner")
    })
    @DeleteMapping(path = "/{id}")
    public void deleteTelephone(@PathVariable long id) {
        telephoneService.deleteTelephone(id);
    }
}
