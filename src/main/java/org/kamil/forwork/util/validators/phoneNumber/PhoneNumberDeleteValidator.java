package org.kamil.forwork.util.validators.phoneNumber;

import lombok.RequiredArgsConstructor;
import org.kamil.forwork.dtos.PhoneNumberDTO;
import org.kamil.forwork.security.PersonSecurityDetails;
import org.kamil.forwork.services.PersonService;
import org.kamil.forwork.services.PhoneNumberService;
import org.kamil.forwork.store.entities.PersonEntity;
import org.kamil.forwork.store.entities.PhoneNumberEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;


@Component
@RequiredArgsConstructor
public class PhoneNumberDeleteValidator implements Validator {

    private final PhoneNumberService phoneNumberService;
    private final PersonService personService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(PhoneNumberDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PhoneNumberDTO phoneNumber = (PhoneNumberDTO) target;
        PersonSecurityDetails personDetails = (PersonSecurityDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        PersonEntity person = personService.findByUsername(personDetails.getUsername());

        String errorMessage;
        if(!phoneNumberService.isExist(phoneNumber.getNumber())){
            errorMessage = String.format("Number '%s' doesn't exist", phoneNumber.getNumber());
            errors.rejectValue("number","", errorMessage);
        }
        if(!checkHavePersonPhoneNumber(person, phoneNumber.getNumber())){
            errorMessage = String.format("You haven't the number '%s'", phoneNumber.getNumber());
            errors.rejectValue("number","", errorMessage);
        }else{
            if(person.getPhoneNumbers().size()==1){
                errorMessage = "You can't delete your last number.";
                errors.rejectValue("number","", errorMessage);
            }
        }

    }

    private boolean checkHavePersonPhoneNumber(PersonEntity person, String number){
        List<PhoneNumberEntity> filteredList = person.getPhoneNumbers().stream().filter(e->e.getNumber().equals(number)).toList();
        return !filteredList.isEmpty();
    }
}
