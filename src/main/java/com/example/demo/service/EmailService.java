package com.example.demo.service;


import com.example.demo.dto.EmailDto;
import com.example.demo.model.Email;
import com.example.demo.model.User;
import com.example.demo.repository.EmailRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;


    public List<Email> getEmails() {

        return emailRepository.findAll();
    }

    public Email addEmail(EmailDto emailDto) {

        User currentUser = userDetailsServiceImpl.getCurrentUserName();

        if (emailRepository.existsEmailByEmailAddress(emailDto.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Email email = new Email(emailDto.getEmailAddress());
        email.setUser(currentUser);

        currentUser.getEmails().add(email);
        userRepository.save(currentUser);

        return email;
    }

    public Email updateEmail(EmailDto emailDto, long id) {

        Email email = emailRepository.findById(id).orElseThrow();

        User currentUser = userDetailsServiceImpl.getCurrentUserName();
        User ownerUser = email.getUser();

        if (!Objects.equals(currentUser, ownerUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        email.setEmailAddress(emailDto.getEmailAddress());
        return emailRepository.save(email);
    }

    public void deleteEmail(long id) {

        Email email = emailRepository.findById(id).orElseThrow();

        User currentUser = userDetailsServiceImpl.getCurrentUserName();
        User ownerUser = email.getUser();

        if (!Objects.equals(currentUser, ownerUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (ownerUser.getEmails().size() == 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

            emailRepository.deleteById(id);
        }

}

