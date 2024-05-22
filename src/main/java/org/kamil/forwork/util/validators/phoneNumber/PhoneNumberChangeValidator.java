package org.kamil.forwork.util.validators.phoneNumber;

import lombok.RequiredArgsConstructor;
import org.kamil.forwork.dtos.PhoneNumberChangeDTO;
import org.kamil.forwork.security.PersonSecurityDetails;
import org.kamil.forwork.services.PersonService;
import org.kamil.forwork.services.PhoneNumberService;
import org.kamil.forwork.store.entities.PersonEntity;
import org.kamil.forwork.store.entities.PhoneNumberEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;


@Component
@RequiredArgsConstructor
public class PhoneNumberChangeValidator implements Validator {

    private final PhoneNumberService phoneNumberService;
    private final PersonService personService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(PhoneNumberChangeDTO.class);
    }

    public void validate(Object target, Errors errors) {
        PhoneNumberChangeDTO phoneNumber = (PhoneNumberChangeDTO) target;
        PersonSecurityDetails personSecurityDetails = phoneNumber.getPersonDetails();

        String errorMessage;
        if(!phoneNumberService.isExist(phoneNumber.getChangeableNumber())){
            errorMessage = String.format("Number '%s' doesn't exist", phoneNumber.getChangeableNumber());
            errors.rejectValue("changeableNumber","", errorMessage);
        }else{
            //Проверяем, является ли авторизированный пользователь обладателем изменяемого номера
            PersonEntity person = personService.findByUsername(personSecurityDetails.getUsername());
            if(!checkHavePersonPhoneNumber(person, phoneNumber.getChangeableNumber())){
                errorMessage = String.format("You haven't the number '%s'", phoneNumber.getChangeableNumber());
                errors.rejectValue("changeableNumber","", errorMessage);
            }
        }
        if(phoneNumberService.isExist(phoneNumber.getNewNumber())){
            errorMessage = String.format("Number '%s' already used", phoneNumber.getNewNumber());
            errors.rejectValue("newNumber","", errorMessage);
        }

    }

    private boolean checkHavePersonPhoneNumber(PersonEntity person, String number){
        List<PhoneNumberEntity> filteredList = person.getPhoneNumbers().stream().filter(e->e.getNumber().equals(number)).toList();
        return !filteredList.isEmpty();
    }
}
