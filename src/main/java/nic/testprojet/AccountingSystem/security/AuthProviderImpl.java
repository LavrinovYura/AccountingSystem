package nic.testprojet.AccountingSystem.security;

import nic.testprojet.AccountingSystem.services.PersonDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final PersonDetailServiceImpl personDetailService;

    @Autowired
    public AuthProviderImpl(PersonDetailServiceImpl personDetailService) {
        this.personDetailService = personDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();

        UserDetails personDetails = personDetailService.loadUserByUsername(userName);

        String password = authentication.getCredentials().toString();

        if (!password.equals(personDetails.getPassword()))
        throw new BadCredentialsException("Password dont match");

        return new UsernamePasswordAuthenticationToken(personDetails, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
