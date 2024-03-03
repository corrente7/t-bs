package com.example.demo.repository;


import com.example.demo.model.Email;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmailRepository extends CrudRepository<Email, Long> {

    Optional<Email> findById(long id);

    boolean existsEmailByEmailAddress(String email);

    List<Email> findAll();
}
