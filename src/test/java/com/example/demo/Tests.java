package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.demo.dto.AccountDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.Account;
import com.example.demo.model.Email;
import com.example.demo.model.Telephone;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.EmailRepository;
import com.example.demo.repository.TelephoneRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtTokenUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@SpringBootTest
@AutoConfigureMockMvc
public class Tests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper om;

    private String token;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final String baseUrl = "http://localhost:8080";

    @AfterEach
    void clean() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void transferTest() throws Exception {
        UserDto testUserDto1 = new UserDto("mark", "password1", new Account(BigDecimal.valueOf(500)), Set.of(new Email("mark@google.com")), Set.of(new Telephone("777777")));
        UserDto testUserDto2 = new UserDto("gwin", "password2", new Account(BigDecimal.valueOf(100)), Set.of(new Email("gwin@google.com")), Set.of(new Telephone("222222")));

        MockHttpServletResponse response1 = mockMvc
                .perform(post(baseUrl + "/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(testUserDto1)))
                .andReturn()
                .getResponse();

        assertThat(response1.getStatus()).isEqualTo(201);

        MockHttpServletResponse response2 = mockMvc
                .perform(post(baseUrl + "/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(testUserDto2)))
                .andReturn()
                .getResponse();

        assertThat(response2.getStatus()).isEqualTo(201);

        AccountDto accountDto = new AccountDto(1, 2, BigDecimal.valueOf(100));

        token = jwtTokenUtil.generateToken(userRepository.findById(1).get());

        MockHttpServletResponse response = mockMvc
                .perform(put(baseUrl + "/api/accounts/1/transfer")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(accountDto)))
                .andReturn()
                .getResponse();


        assertThat(response.getStatus()).isEqualTo(200);

        BigDecimal balAcc1 = accountRepository.findById(1).get().getBalance();
        BigDecimal balAcc2 = accountRepository.findById(2).get().getBalance();

        assertThat(balAcc1.toString()).contains("400");
        assertThat(balAcc2.toString()).contains("200");
    }

}
