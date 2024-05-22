package org.kamil.forwork.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.kamil.forwork.store.entities.EmailEntity;
import org.kamil.forwork.store.entities.PersonEntity;
import org.kamil.forwork.store.entities.PhoneNumberEntity;
import org.kamil.forwork.store.repositories.PersonRepository;
import org.kamil.forwork.store.PersonSearchSpecifications;
import org.kamil.forwork.util.exceptions.TransferMoneyException;
import org.kamil.forwork.util.exceptions.user.UserNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Data
@Slf4j
@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonSearchSpecifications specifications;

    public PersonEntity findByUsername(String username){
        return personRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }
    public boolean isExist(String username){
        return personRepository.existsByUsername(username);
    }
    public List<PersonEntity> search(Optional<String> fullName, Optional<Date> bornAt, Optional<String> phoneNumber,
                                     Optional<String> email, int page, int pageSize) {

        StringBuilder logMessage = new StringBuilder("search: {params: [");

        Specification<PersonEntity> spec = Specification.where(null);
        if (fullName.isPresent()) {
            logMessage.append("fullName=").append(fullName.get());
            spec = spec.and(specifications.hasFullNameLike(fullName.get()));
        }
        if (email.isPresent()) {
            logMessage.append("email=").append(email.get());
            spec = spec.and(specifications.hasEmail(email.get()));
        }
        if (phoneNumber.isPresent()) {
            logMessage.append("phoneNumber=").append(phoneNumber.get());
            spec = spec.and(specifications.hasPhoneNumber(phoneNumber.get()));
        }
        if (bornAt.isPresent()) {
            logMessage.append("bornAt=").append(bornAt.get());
            spec = spec.and(specifications.birthDateAfter(bornAt.get()));
        }
        logMessage.append("]}");
        log.info(logMessage.toString());

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("fullName"));
        return personRepository.findAll(spec, pageable).getContent();
    }
    @Transactional
    public void save(PersonEntity person){
        for(PhoneNumberEntity entity: person.getPhoneNumbers()){
            entity.setOwner(person);
        }
        for(EmailEntity entity: person.getEmails()){
            entity.setOwner(person);
        }
        personRepository.save(enrich(person));
    }
    @Async
    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    protected void addPercentsEveryMinute(){
        List<PersonEntity> persons = personRepository.findAll();
        persons.forEach(e -> {
            long newAmount;
            if((newAmount = (long) (e.getAmountOfMoney() * 1.05)) <= e.getStartAmountOfMoney() * 2.07){
                e.setAmountOfMoney(newAmount);
            }
        });
        personRepository.saveAll(persons);
        log.info("Percents added");
    }
    @Transactional
    public void transfer(String senderUsername, String targetUsername, Double amount){
        PersonEntity sender = findByUsername(senderUsername);
        PersonEntity target = findByUsername(targetUsername);

        long newAmount;
        if((newAmount = sender.getAmountOfMoney() - (long)(amount * 100))>=0) {
            sender.setAmountOfMoney(newAmount);
            target.setAmountOfMoney(target.getAmountOfMoney() + (long) (amount * 100));

            personRepository.save(sender);
            personRepository.save(target);
        }else {
            throw new TransferMoneyException("There are not enough funds");
        }
    }

    private PersonEntity enrich(PersonEntity entity){
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setStartAmountOfMoney(entity.getAmountOfMoney());
        return entity;
    }

}