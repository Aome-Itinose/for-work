package org.kamil.forwork.util.validators;

import org.kamil.forwork.util.exceptions.AnyCustomException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class ValidatingTools {
    public void getExceptionMessage(BindingResult bindingResult) throws RuntimeException{
        if(bindingResult.hasErrors()){
            StringBuilder message = new StringBuilder();
            for(FieldError fieldError: bindingResult.getFieldErrors()){
                message.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append("; ");
            }
            throw new AnyCustomException(String.valueOf(message));
        }
    }
}
