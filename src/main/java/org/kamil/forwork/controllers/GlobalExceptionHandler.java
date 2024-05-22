package org.kamil.forwork.controllers;


import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.kamil.forwork.util.exceptions.*;
import org.kamil.forwork.util.exceptions.email.EmailNotChangedException;
import org.kamil.forwork.util.exceptions.email.EmailNotCreatedException;
import org.kamil.forwork.util.exceptions.email.EmailNotDeletedException;
import org.kamil.forwork.util.exceptions.email.EmailNotFoundException;
import org.kamil.forwork.util.exceptions.phoneNumber.PhoneNumberNotChangedException;
import org.kamil.forwork.util.exceptions.phoneNumber.PhoneNumberNotCreatedException;
import org.kamil.forwork.util.exceptions.phoneNumber.PhoneNumberNotDeletedException;
import org.kamil.forwork.util.exceptions.phoneNumber.PhoneNumberNotFoundException;
import org.kamil.forwork.util.exceptions.user.UserNotCreatedException;
import org.kamil.forwork.util.exceptions.user.UserNotFoundException;
import org.kamil.forwork.util.responses.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, PhoneNumberNotFoundException.class, EmailNotFoundException.class})
    private ResponseEntity<ExceptionResponse> notFoundExceptionHandler(AnyCustomException exception){
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(), new Date());
        log.warn(response.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({EmailNotCreatedException.class, UserNotCreatedException.class, PhoneNumberNotCreatedException.class})
    private ResponseEntity<ExceptionResponse> notCreatedExceptionHandler(AnyCustomException exception){
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(), new Date());
        log.warn(response.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({PhoneNumberNotChangedException.class, EmailNotChangedException.class})
    private ResponseEntity<ExceptionResponse> notEditedExceptionHandler(RuntimeException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), new Date());
        log.warn(response.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({PhoneNumberNotDeletedException.class, EmailNotDeletedException.class})
    private ResponseEntity<ExceptionResponse> notDeletedExceptionHandler(RuntimeException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), new Date());
        log.warn(response.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransferMoneyException.class)
    private ResponseEntity<ExceptionResponse> transferMoneyExceptionHandler(AnyCustomException exception){
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(), new Date());
        log.warn(response.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AuthenticationException.class)
    private ResponseEntity<ExceptionResponse> authExceptionHandler(AnyCustomException exception){
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(), new Date());
        log.warn(response.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({JWTDecodeException.class, IncorrectClaimException.class, InvalidClaimException.class})
    private ResponseEntity<ExceptionResponse> incorrectTokenExceptionHandler (JWTVerificationException ignoredE) {
        ExceptionResponse response = new ExceptionResponse(
                "Token is incorrect.",
                new Date()
        );
        log.warn(response.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(JWTVerificationException.class)
    private ResponseEntity<ExceptionResponse> exceptionHandler(JWTVerificationException e){
        ExceptionResponse response = new ExceptionResponse(
                e.getMessage(),
                new Date()
        );
        log.warn(response.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
