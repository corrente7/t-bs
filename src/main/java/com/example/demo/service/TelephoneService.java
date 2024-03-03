package com.example.demo.service;

import com.example.demo.dto.TelephoneDto;
import com.example.demo.model.Telephone;
import com.example.demo.model.User;
import com.example.demo.repository.TelephoneRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class TelephoneService {

    @Autowired
    private TelephoneRepository telephoneRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;


    public List<Telephone> getTelephones() {

        return telephoneRepository.findAll();
    }

    public Telephone addTelephone(TelephoneDto telephoneDto) {

        User currentUser = userDetailsServiceImpl.getCurrentUserName();

        if (telephoneRepository.existsTelephoneByNumber(telephoneDto.getNumber())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Telephone telephone = new Telephone(telephoneDto.getNumber());
        telephone.setUser(currentUser);

        currentUser.getTelephones().add(telephone);
        userRepository.save(currentUser);

        return telephone;
    }

    public Telephone updateTelephone(TelephoneDto telephoneDto, long id) {

        Telephone telephone = telephoneRepository.findById(id).orElseThrow();

        User currentUser = userDetailsServiceImpl.getCurrentUserName();
        User ownerUser = telephone.getUser();

        if (!Objects.equals(currentUser, ownerUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        telephone.setNumber(telephoneDto.getNumber());
        return telephoneRepository.save(telephone);
    }

    public void deleteTelephone(long id) {

        Telephone telephone = telephoneRepository.findById(id).orElseThrow();

        User currentUser = userDetailsServiceImpl.getCurrentUserName();
        User ownerUser = telephone.getUser();

        if (!Objects.equals(currentUser, ownerUser)) {
           throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (ownerUser.getTelephones().size() == 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        telephoneRepository.deleteById(id);
        }

}

