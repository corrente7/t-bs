package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import com.example.demo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findUserByLogin(String login);
    Optional<User> findById(long id);
    List<User> findAll();

    boolean existsUserByLogin(String login);

}
