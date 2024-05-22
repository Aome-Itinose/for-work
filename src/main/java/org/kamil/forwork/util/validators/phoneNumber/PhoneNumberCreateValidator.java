package org.kamil.forwork.util.validators.phoneNumber;

import lombok.RequiredArgsConstructor;
import org.kamil.forwork.dtos.PhoneNumberDTO;
import org.kamil.forwork.services.PhoneNumberService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PhoneNumberCreateValidator implements Validator {

    private final PhoneNumberService phoneNumberService;
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(PhoneNumberDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PhoneNumberDTO phoneNumber = (PhoneNumberDTO) target;
        if(phoneNumberService.isExist(phoneNumber.getNumber())){
            String errorMessage = String.format("Phone number '%s' is already used", phoneNumber.getNumber());
            errors.rejectValue("number","", errorMessage);
        }
    }
}
