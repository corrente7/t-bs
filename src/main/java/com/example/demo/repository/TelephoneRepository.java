package com.example.demo.repository;

import com.example.demo.model.Telephone;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TelephoneRepository extends CrudRepository<Telephone, Long> {

    Optional<Telephone> findById(long id);

    boolean existsTelephoneByNumber(String number);

    List<Telephone> findAll();

}
