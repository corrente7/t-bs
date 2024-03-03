package com.example.demo.component;


import com.example.demo.dto.UserParamsDto;
import com.example.demo.model.Email;
import com.example.demo.model.Telephone;
import com.example.demo.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserSpecification {

    public Specification<User> build(UserParamsDto params) {
        return withBirthday(params.getBirthdayGt())
                .and(withTelephone(params.getTelephoneEq()))
                .and(withFullName(params.getFullNameCont()))
                 .and(withEmail(params.getEmailEq()));
    }

    private Specification<User> withBirthday(LocalDate birthdayGt) {
        return (root, query, cb) -> birthdayGt == null ? cb.conjunction() : cb.greaterThan(root.get("birthday"), birthdayGt);
    }

    private Specification<User> withTelephone(String telephoneEq) {
        return (root, query, cb) ->
        {
            if (telephoneEq == null)
                return cb.conjunction();
            {
                Join<Telephone, User> usersTelephone = root.join("telephones");
                return cb.equal(usersTelephone.get("number"), telephoneEq);
            }
        };
    }

    private Specification<User> withFullName(String name) {
        return (root, query, cb) -> name == null ? cb.conjunction() : cb.like(root.get("fullName"), name + "%");
    }

    private Specification<User> withEmail(String emailEq) {
        return (root, query, cb) -> {
            if (emailEq == null)
                return cb.conjunction();
            {
            Join<Email, User> usersEmail = root.join("emails");
            return cb.equal(usersEmail.get("emailAddress"), emailEq);
            }
        };
    }
}
