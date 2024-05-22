package org.kamil.forwork.services;

import lombok.Data;
import org.kamil.forwork.store.entities.EmailEntity;
import org.kamil.forwork.store.entities.PersonEntity;
import org.kamil.forwork.store.repositories.EmailRepository;
import org.kamil.forwork.util.exceptions.email.EmailNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Data
@Service
@Transactional(readOnly = true)
public class EmailService {

    private final EmailRepository emailRepository;
    private final PersonService personService;

    public EmailEntity findByEmail(String email){
        return emailRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);
    }
    public boolean isExist(String email){
        return emailRepository.existsByEmail(email);
    }
    @Transactional
    public void save(String ownerUsername, EmailEntity email){
        PersonEntity owner = personService.findByUsername(ownerUsername);
        owner.getEmails().add(email);
        email.setOwner(owner);
        emailRepository.save(email);
    }
    @Transactional
    public void change(String changeableEmail, String newEmail){
        findByEmail(changeableEmail).setEmail(newEmail);
    }
    @Transactional
    public void delete(String deleteEmail){
        EmailEntity emailEntity = findByEmail(deleteEmail);
        emailRepository.delete(emailEntity);
        emailEntity.getOwner().getEmails().remove(emailEntity);
    }

}