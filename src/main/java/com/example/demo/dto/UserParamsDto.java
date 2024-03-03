package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserParamsDto {


    private String emailEq;
    private String telephoneEq;

    private String fullNameCont;
    private LocalDate birthdayGt;

}
