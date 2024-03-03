package com.example.demo.dto;

import com.example.demo.model.Account;
import com.example.demo.model.Email;
import com.example.demo.model.Telephone;
import com.example.demo.model.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    private Account account;

    private Set<Email> emails;
    private Set<Telephone> telephones;

    private String fullName;

    private LocalDate birthday;

    public UserDto(String login, String password, Account account, Set<Email> emails, Set<Telephone> telephones) {
        this.login = login;
        this.password = password;
        this.account = account;
        this.emails = emails;
        this.telephones = telephones;
    }
}
