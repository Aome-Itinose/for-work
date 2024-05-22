package org.kamil.forwork.util.validators.email;

import lombok.RequiredArgsConstructor;
import org.kamil.forwork.dtos.EmailDTO;
import org.kamil.forwork.dtos.PhoneNumberDTO;
import org.kamil.forwork.security.PersonSecurityDetails;
import org.kamil.forwork.services.EmailService;
import org.kamil.forwork.services.PersonService;
import org.kamil.forwork.services.PhoneNumberService;
import org.kamil.forwork.store.entities.EmailEntity;
import org.kamil.forwork.store.entities.PersonEntity;
import org.kamil.forwork.store.entities.PhoneNumberEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;


@Component
@RequiredArgsConstructor
public class EmailDeleteValidator implements Validator {

    private final EmailService emailService;
    private final PersonService personService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(EmailDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmailDTO email = (EmailDTO) target;
        PersonSecurityDetails personDetails = (PersonSecurityDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        PersonEntity person = personService.findByUsername(personDetails.getUsername());

        String errorMessage;
        if(!emailService.isExist(email.getEmail())){
            errorMessage = String.format("Email '%s' doesn't exist", email.getEmail());
            errors.rejectValue("email","", errorMessage);
        }
        if(!checkHavePersonEmail(person, email.getEmail())){
            errorMessage = String.format("You haven't the email '%s'", email.getEmail());
            errors.rejectValue("email","", errorMessage);
        }else{
            if(person.getEmails().size()==1){
                errorMessage = "You can't delete your last email.";
                errors.rejectValue("email","", errorMessage);
            }
        }

    }

    private boolean checkHavePersonEmail(PersonEntity person, String email){
        List<EmailEntity> filteredList = person.getEmails().stream().filter(e->e.getEmail().equals(email)).toList();
        return !filteredList.isEmpty();
    }
}
