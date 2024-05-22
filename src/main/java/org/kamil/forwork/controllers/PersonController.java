package org.kamil.forwork.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kamil.forwork.dtos.*;
import org.kamil.forwork.security.PersonSecurityDetails;
import org.kamil.forwork.services.EmailService;
import org.kamil.forwork.services.PersonService;
import org.kamil.forwork.services.PhoneNumberService;
import org.kamil.forwork.store.entities.EmailEntity;
import org.kamil.forwork.store.entities.PersonEntity;
import org.kamil.forwork.store.entities.PhoneNumberEntity;
import org.kamil.forwork.util.converters.Converters;
import org.kamil.forwork.util.exceptions.AnyCustomException;
import org.kamil.forwork.util.exceptions.SearchException;
import org.kamil.forwork.util.exceptions.TransferMoneyException;
import org.kamil.forwork.util.exceptions.email.EmailNotCreatedException;
import org.kamil.forwork.util.exceptions.email.EmailNotChangedException;
import org.kamil.forwork.util.exceptions.phoneNumber.PhoneNumberNotChangedException;
import org.kamil.forwork.util.exceptions.phoneNumber.PhoneNumberNotCreatedException;
import org.kamil.forwork.util.exceptions.phoneNumber.PhoneNumberNotDeletedException;
import org.kamil.forwork.util.responses.Response;
import org.kamil.forwork.util.validators.*;
import org.kamil.forwork.util.validators.email.EmailChangeValidator;
import org.kamil.forwork.util.validators.email.EmailCreateValidator;
import org.kamil.forwork.util.validators.email.EmailDeleteValidator;
import org.kamil.forwork.util.validators.phoneNumber.PhoneNumberChangeValidator;
import org.kamil.forwork.util.validators.phoneNumber.PhoneNumberCreateValidator;
import org.kamil.forwork.util.validators.phoneNumber.PhoneNumberDeleteValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonController {

    private final PhoneNumberService phoneNumberService;
    private final EmailService emailService;
    private final PersonService personService;

    private final Converters converters;

    private final PhoneNumberCreateValidator phoneNumberCreateValidator;
    private final PhoneNumberChangeValidator phoneNumberChangeValidator;
    private final PhoneNumberDeleteValidator phoneNumberDeleteValidator;

    private final EmailCreateValidator emailCreateValidator;
    private final EmailChangeValidator emailChangeValidator;
    private final EmailDeleteValidator emailDeleteValidator;

    private final TransferValidator transferValidator;

    private final ValidatingTools validatingTools;

    @PostMapping("/transfer")
    public ResponseEntity<Response> transfer(@RequestBody @Valid TransferMoneyDTO transferMoneyDTO, BindingResult bindingResult){

        PersonSecurityDetails personDetails = (PersonSecurityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        transferMoneyDTO.setSenderUsername(personDetails.getUsername());

        transferValidator.validate(transferMoneyDTO, bindingResult);
        try{
            validatingTools.getExceptionMessage(bindingResult);
        }catch (AnyCustomException anyCustomException){
            throw new TransferMoneyException(anyCustomException.getMessage());
        }

        personService.transfer(transferMoneyDTO.getSenderUsername(), transferMoneyDTO.getTargetUsername(), transferMoneyDTO.getAmount());

        Response response = new Response("Transferred");
        log.info(response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<List<PersonWithoutPasswordDTO>> search(@RequestParam Optional<String> fullName,
                                                                 @RequestParam Optional<String> bornAt,
                                                                 @RequestParam Optional<String> phoneNumber,
                                                                 @RequestParam Optional<String> email,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int pageSize) {
        Optional<Date> dateBurnAt = Optional.empty();
        if (bornAt.isPresent())
            dateBurnAt = Optional.of(parseDate(bornAt.get()));
        List<PersonEntity> persons = personService.search(fullName, dateBurnAt, phoneNumber, email, page, pageSize);
        return new ResponseEntity<>(converters.convertToPersonDTOList(persons), HttpStatus.OK);
    }

    @PostMapping("/phone-number/add")
    public ResponseEntity<Response> addPhoneNumber(@RequestBody @Valid PhoneNumberDTO phoneNumberDTO, BindingResult bindingResult){
        phoneNumberCreateValidator.validate(phoneNumberDTO, bindingResult);

        String logMessage = String.format("addPhoneNumber: " + phoneNumberDTO);
        log.info(logMessage);

        try{
            validatingTools.getExceptionMessage(bindingResult);
        }catch (AnyCustomException anyCustomException){
            throw new PhoneNumberNotCreatedException(anyCustomException.getMessage());
        }

        PersonSecurityDetails personSecurityDetails =
                (PersonSecurityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PhoneNumberEntity phoneNumberEntity = converters.convertToPhoneNumberEntity(phoneNumberDTO);

        phoneNumberService.save(personSecurityDetails.getUsername(), phoneNumberEntity);

        Response response = new Response("Phone number added");
        log.info(response.toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PatchMapping("/phone-number/change")
    public ResponseEntity<Response> changePhoneNumber(@RequestBody @Valid PhoneNumberChangeDTO phoneNumberChangeDTO, BindingResult bindingResult){

        PersonSecurityDetails personDetails = (PersonSecurityDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        phoneNumberChangeDTO.setPersonDetails(personDetails);

        String logMessage = String.format("changePhoneNumber: " + phoneNumberChangeDTO);
        log.info(logMessage);

        phoneNumberChangeValidator.validate(phoneNumberChangeDTO, bindingResult);
        try{
            validatingTools.getExceptionMessage(bindingResult);
        }catch (AnyCustomException anyCustomException){
            throw new PhoneNumberNotChangedException(anyCustomException.getMessage());
        }

        phoneNumberService.change(phoneNumberChangeDTO.getChangeableNumber(), phoneNumberChangeDTO.getNewNumber());

        Response response = new Response("Phone number changed");
        log.info(response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/phone-number/delete")
    public ResponseEntity<Response> deletePhoneNumber(@RequestBody @Valid PhoneNumberDTO phoneNumberDTO, BindingResult bindingResult){
        String logMessage = String.format("deletePhoneNumber: " + phoneNumberDTO);
        log.info(logMessage);

        phoneNumberDeleteValidator.validate(phoneNumberDTO, bindingResult);
        try{
            validatingTools.getExceptionMessage(bindingResult);
        }catch (AnyCustomException anyCustomException){
            throw new PhoneNumberNotDeletedException(anyCustomException.getMessage());
        }

        phoneNumberService.delete(phoneNumberDTO.getNumber());

        Response response = new Response("Phone number deleted");
        log.info(response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/email/add")
    public ResponseEntity<Response> addEmail(@RequestBody @Valid EmailDTO emailDTO, BindingResult bindingResult){
        String logMessage = String.format("addEmail: " + emailDTO);
        log.info(logMessage);

        emailCreateValidator.validate(emailDTO, bindingResult);
        try{
            validatingTools.getExceptionMessage(bindingResult);
        }catch (AnyCustomException anyCustomException){
            throw new EmailNotCreatedException(anyCustomException.getMessage());
        }

        PersonSecurityDetails personSecurityDetails =
                (PersonSecurityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EmailEntity emailEntity = converters.convertToEmailEntity(emailDTO);

        emailService.save(personSecurityDetails.getUsername(), emailEntity);

        Response response = new Response("Email added");
        log.info(response.toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PatchMapping("/email/change")
    public ResponseEntity<Response> changeEmail(@RequestBody @Valid EmailChangeDTO emailChangeDTO, BindingResult bindingResult){
        PersonSecurityDetails personDetails = (PersonSecurityDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        emailChangeDTO.setPersonDetails(personDetails);

        String logMessage = String.format("changeEmail: " + emailChangeDTO);
        log.info(logMessage);

        emailChangeValidator.validate(emailChangeDTO, bindingResult);
        try{
            validatingTools.getExceptionMessage(bindingResult);
        }catch (AnyCustomException anyCustomException){
            throw new EmailNotChangedException(anyCustomException.getMessage());
        }

        emailService.change(emailChangeDTO.getChangeableEmail(), emailChangeDTO.getNewEmail());

        Response response = new Response("Email changed");
        log.info(response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/email/delete")
    public ResponseEntity<Response> deleteEmail(@RequestBody @Valid EmailDTO emailDTO, BindingResult bindingResult){
        String logMessage = String.format("deleteEmail: " + emailDTO);
        log.info(logMessage);

        emailDeleteValidator.validate(emailDTO, bindingResult);
        try{
            validatingTools.getExceptionMessage(bindingResult);
        }catch (AnyCustomException anyCustomException){
            throw new PhoneNumberNotDeletedException(anyCustomException.getMessage());
        }

        emailService.delete(emailDTO.getEmail());

        Response response = new Response("Email deleted");
        log.info(response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private Date parseDate(String stringDate) {
        String dateString = "2024-05-19";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date;

        try {
            date = formatter.parse(stringDate);
        } catch (ParseException e) {
            throw new SearchException(e.getMessage());
        }

        return date;
    }
}
