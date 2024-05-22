package org.kamil.forwork.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kamil.forwork.dtos.PersonDTO;
import org.kamil.forwork.services.PersonService;
import org.kamil.forwork.store.entities.PersonEntity;
import org.kamil.forwork.util.converters.Converters;
import org.kamil.forwork.util.exceptions.AnyCustomException;
import org.kamil.forwork.util.exceptions.user.UserNotCreatedException;
import org.kamil.forwork.util.responses.Response;
import org.kamil.forwork.util.validators.PersonCreateValidator;
import org.kamil.forwork.util.validators.ValidatingTools;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/official")
public class OfficialController {

    private final PersonService personService;

    private final Converters converters;
    private final PersonCreateValidator personCreateValidator;
    private final ValidatingTools validatingTools;

    @PostMapping("/user/add")
    public ResponseEntity<Response> addPerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult){
        String logMessage = String.format("addPerson: " + personDTO.getUsername());
        log.info(logMessage);

        personCreateValidator.validate(personDTO, bindingResult);
        try {
            validatingTools.getExceptionMessage(bindingResult);
        }catch (AnyCustomException anyCustomException){
            throw new UserNotCreatedException(anyCustomException.getMessage());
        }
        PersonEntity person = converters.convertToEntity(personDTO);
        personService.save(person);

        Response response = new Response(String.format("User '%s' added", person.getUsername()));
        log.info(response.toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
