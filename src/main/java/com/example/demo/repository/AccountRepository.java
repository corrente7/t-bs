package com.example.demo.repository;


import com.example.demo.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findById(long id);

    List<Account> findAll();
}
