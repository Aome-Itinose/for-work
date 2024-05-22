package org.kamil.forwork.util.validators.email;

import lombok.RequiredArgsConstructor;
import org.kamil.forwork.dtos.EmailChangeDTO;
import org.kamil.forwork.security.PersonSecurityDetails;
import org.kamil.forwork.services.EmailService;
import org.kamil.forwork.services.PersonService;
import org.kamil.forwork.store.entities.EmailEntity;
import org.kamil.forwork.store.entities.PersonEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;


@Component
@RequiredArgsConstructor
public class EmailChangeValidator implements Validator {

    private final EmailService emailService;
    private final PersonService personService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(EmailChangeDTO.class);
    }

    public void validate(Object target, Errors errors) {
        EmailChangeDTO email = (EmailChangeDTO) target;
        PersonSecurityDetails personSecurityDetails = email.getPersonDetails();

        String errorMessage;
        if(!emailService.isExist(email.getChangeableEmail())){
            errorMessage = String.format("Email '%s' doesn't exist", email.getChangeableEmail());
            errors.rejectValue("changeableEmail","", errorMessage);
        }else{
            //Проверяем, является ли авторизированный пользователь обладателем изменяемого номера
            PersonEntity person = personService.findByUsername(personSecurityDetails.getUsername());
            if(!checkHavePersonEmail(person, email.getChangeableEmail())){
                errorMessage = String.format("You haven't the email '%s'", email.getChangeableEmail());
                errors.rejectValue("changeableEmail","", errorMessage);
            }
        }
        if(emailService.isExist(email.getNewEmail())){
            errorMessage = String.format("Email '%s' already used", email.getNewEmail());
            errors.rejectValue("newEmail","", errorMessage);
        }

    }

    private boolean checkHavePersonEmail(PersonEntity person, String email){
        List<EmailEntity> filteredList = person.getEmails().stream().filter(e->e.getEmail().equals(email)).toList();
        return !filteredList.isEmpty();
    }
}
