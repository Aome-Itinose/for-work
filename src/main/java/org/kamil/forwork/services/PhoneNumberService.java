package org.kamil.forwork.services;

import lombok.Data;
import org.kamil.forwork.store.entities.PersonEntity;
import org.kamil.forwork.store.entities.PhoneNumberEntity;
import org.kamil.forwork.store.repositories.PhoneNumberRepository;
import org.kamil.forwork.util.exceptions.phoneNumber.PhoneNumberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Data
@Service
@Transactional(readOnly = true)
public class PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;

    private final PersonService personService;

    public PhoneNumberEntity findByNumber(String number){
        return phoneNumberRepository.findByNumber(number).orElseThrow(PhoneNumberNotFoundException::new);
    }
    public boolean isExist(String number){
        return phoneNumberRepository.existsByNumber(number);
    }

    @Transactional
    public void save(String ownerUsername, PhoneNumberEntity phoneNumber){
        PersonEntity owner = personService.findByUsername(ownerUsername);
        owner.getPhoneNumbers().add(phoneNumber);
        phoneNumber.setOwner(owner);
        phoneNumberRepository.save(phoneNumber);
    }
    @Transactional
    public void change(String changeableNumber, String newNumber){
        PhoneNumberEntity phoneNumber = findByNumber(changeableNumber);
        phoneNumber.setNumber(newNumber);
    }
    @Transactional
    public void delete(String deleteNumber){
        PhoneNumberEntity phoneNumber = findByNumber(deleteNumber);
        phoneNumberRepository.delete(phoneNumber);
        phoneNumber.getOwner().getPhoneNumbers().remove(phoneNumber);

    }
}