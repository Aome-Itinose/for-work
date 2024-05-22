package org.kamil.forwork.util.validators.email;

import lombok.RequiredArgsConstructor;
import org.kamil.forwork.dtos.EmailDTO;
import org.kamil.forwork.services.EmailService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class EmailCreateValidator implements Validator {

    private final EmailService emailService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(EmailDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmailDTO emailDTO = (EmailDTO) target;
        if(emailService.isExist(emailDTO.getEmail())){
            String errorMessage = String.format("Email '%s' is already used", emailDTO.getEmail());
            errors.rejectValue("email", "", errorMessage);
        }
    }
}
