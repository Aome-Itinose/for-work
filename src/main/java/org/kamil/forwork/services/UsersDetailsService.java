package org.kamil.forwork.services;

import lombok.RequiredArgsConstructor;
import org.kamil.forwork.security.PersonSecurityDetails;
import org.kamil.forwork.store.entities.PersonEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersDetailsService implements UserDetailsService {
    private final PersonService personService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PersonEntity person = personService.findByUsername(username);
        return new PersonSecurityDetails(person);
    }
}
