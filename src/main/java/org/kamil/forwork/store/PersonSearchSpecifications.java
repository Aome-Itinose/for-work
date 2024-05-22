package org.kamil.forwork.store;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.kamil.forwork.store.entities.PersonEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PersonSearchSpecifications {
    public Specification<PersonEntity> hasFullNameLike(String fullName){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("fullName"), fullName + "%");
    }
    public Specification<PersonEntity> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> join = root.join("emails", JoinType.INNER);
            return criteriaBuilder.equal(join.get("email"), email);
        };
    }

    public Specification<PersonEntity> hasPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> join = root.join("phoneNumbers", JoinType.INNER);
            return criteriaBuilder.equal(join.get("number"), phoneNumber);
        };
    }

    public Specification<PersonEntity> birthDateAfter(Date bornAt) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("bornAt"), bornAt);
    }
}
