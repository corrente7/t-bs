package com.example.demo.service;

import com.example.demo.component.UserSpecification;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserParamsDto;
import com.example.demo.model.*;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.EmailRepository;
import com.example.demo.repository.TelephoneRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TelephoneRepository telephoneRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private UserSpecification specBuilder;

    @Autowired
    private PasswordEncoder encoder;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public User createUser(UserDto userDto) {

        User user = new User();
        if (userRepository.existsUserByLogin(userDto.getLogin())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        user.setLogin(userDto.getLogin());
        user.setPassword(encoder.encode(userDto.getPassword()));


        Account account = new Account(userDto.getAccount().getBalance());

        account.setUser(user);
        user.setAccount(account);
        accountRepository.save(account);
        incrementBalance(account);


        Set<Telephone> telephones = new HashSet<>();

        for (Telephone telIn : userDto.getTelephones()) {
            if (telephoneRepository.existsTelephoneByNumber(telIn.getNumber())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            Telephone telephone = new Telephone(telIn.getNumber());
            telephone.setUser(user);
            telephoneRepository.save(telephone);
            telephones.add(telephone);
        }
        user.setTelephones(telephones);

        Set<Email> emails = new HashSet<>();

        for (Email emailIn : userDto.getEmails()) {
            if (emailRepository.existsEmailByEmailAddress(emailIn.getEmailAddress())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            Email email = new Email(emailIn.getEmailAddress());
            email.setUser(user);
            emailRepository.save(email);
            emails.add(email);
        }

        user.setEmails(emails);

        user.setFullName(userDto.getFullName());
        user.setBirthday(userDto.getBirthday());
        user.setRole(UserRole.USER);

        return userRepository.save(user);
    }

    public Page<User> filter(UserParamsDto params, int page) {
        var spec = specBuilder.build(params);

        //List<User> users = userRepository.findAll(spec);

        return userRepository.findAll(spec, PageRequest.of(page - 1, 10));
    }

    //баланс клиента увеличивается без учета трансфера денег
    public void incrementBalance(Account account) {

        BigDecimal balance = account.getBalance();
        final BigDecimal end = balance.multiply(BigDecimal.valueOf(207)).divide(BigDecimal.valueOf(100));

        final int[] c = {0};
        final BigDecimal[] finalBalance = new BigDecimal[1];

        finalBalance[0] = balance;

        Thread run = new Thread(new Runnable() {
            @Override
            public void run() {
                while(c[0] != -1){

                    try {
                        Thread.sleep(1000 * 60); //1000 - 1 сек
                        finalBalance[0] = finalBalance[0].add(finalBalance[0].multiply(BigDecimal.valueOf(5)).divide(BigDecimal.valueOf(100)));
                        c[0] = end.compareTo(finalBalance[0]);
                        account.setBalance(finalBalance[0]);
                        accountRepository.save(account);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        run.start(); // заводим

    }
}
