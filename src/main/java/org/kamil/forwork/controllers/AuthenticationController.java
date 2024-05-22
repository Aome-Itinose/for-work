package org.kamil.forwork.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kamil.forwork.dtos.AuthenticationDTO;
import org.kamil.forwork.util.JWTUtil;
import org.kamil.forwork.util.exceptions.AuthenticationException;
import org.kamil.forwork.util.responses.AuthenticationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class AuthenticationController {

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> performLogin(@RequestBody AuthenticationDTO authenticationDTO){
        String logMessage = String.format("login: " + authenticationDTO.getPassword());
        log.info(logMessage);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getUsername(), authenticationDTO.getPassword());

        if(authProvider.authenticate(authToken).isAuthenticated()) {
            String token = jwtUtil.generateToken(authenticationDTO.getUsername());
            AuthenticationResponse response = new AuthenticationResponse("You are logged.", token, new Date());
            log.info(response.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new AuthenticationException("Username or password is incorrect!");
     }

}
