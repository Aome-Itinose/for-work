package org.kamil.forwork.util.validators;

import lombok.RequiredArgsConstructor;
import org.kamil.forwork.dtos.TransferMoneyDTO;
import org.kamil.forwork.services.PersonService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class TransferValidator implements Validator {
    private final PersonService personService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(TransferMoneyDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransferMoneyDTO dto = (TransferMoneyDTO) target;
        if (!personService.isExist(dto.getTargetUsername())){
            String errorMessage = String.format("User with username '%s' not exist", dto.getTargetUsername());
            errors.rejectValue("targetUsername", "", errorMessage);
        }
    }
}
