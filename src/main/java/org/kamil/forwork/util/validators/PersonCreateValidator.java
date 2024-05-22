package org.kamil.forwork.util.validators;

import lombok.Data;
import org.kamil.forwork.dtos.EmailDTO;
import org.kamil.forwork.dtos.PersonDTO;
import org.kamil.forwork.dtos.PhoneNumberDTO;
import org.kamil.forwork.services.EmailService;
import org.kamil.forwork.services.PersonService;
import org.kamil.forwork.services.PhoneNumberService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@Data
public class PersonCreateValidator implements Validator {

    private final PersonService personService;
    private final PhoneNumberService phoneNumberService;
    private final EmailService emailService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(PersonDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonDTO person = (PersonDTO) target;
        List<PhoneNumberDTO> phones = person.getPhoneNumbers();
        List<EmailDTO> emails = person.getEmails();
        String errorMessage;
        if(personService.isExist(person.getUsername())) {
            errorMessage = String.format("Username '%s' is already used", person.getUsername());
            errors.rejectValue("username", "", errorMessage);
        }
        if(!phones.isEmpty()) {
            PhoneNumberDTO phone = phones.get(0);
            if (phoneNumberService.isExist(phone.getNumber())) {
                errorMessage = String.format("Phone number '%s' is already used", phone.getNumber());
                errors.rejectValue("phoneNumbers", "", errorMessage);
            }
        }
        if(!emails.isEmpty()) {
            EmailDTO email = emails.get(0);
            if (emailService.isExist(email.getEmail())) {
                errorMessage = String.format("Email '%s' is already used", email.getEmail());
                errors.rejectValue("emails", "", errorMessage);
            }
        }
    }
}
