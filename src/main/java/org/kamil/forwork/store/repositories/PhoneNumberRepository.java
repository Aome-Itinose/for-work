package org.kamil.forwork.store.repositories;

import org.kamil.forwork.store.entities.PhoneNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumberEntity, Long> {
    Optional<PhoneNumberEntity> findByNumber(String number);
    boolean existsByNumber(String number);
}
