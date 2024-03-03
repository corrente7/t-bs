package com.example.demo.dto;

import com.example.demo.model.Account;
import com.example.demo.model.Email;
import com.example.demo.model.Telephone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private long fromID;
    private long toID;
    @Positive
    private BigDecimal amount;

}
