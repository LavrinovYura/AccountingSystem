package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.authorization.LoginDTO;
import nic.testproject.accountingsystem.dto.authorization.LoginResponseDTO;
import nic.testproject.accountingsystem.dto.authorization.RegisterDTO;
import nic.testproject.accountingsystem.dto.authorization.RegisterResponseDTO;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.security.JWT.JWTGenerator;
import nic.testproject.accountingsystem.services.user.LoginService;
import nic.testproject.accountingsystem.services.user.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
    private final ModelMapper modelMapper;
    private final LoginService loginService;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JWTGenerator jwtGenerator, ModelMapper modelMapper,
                          LoginService personService, RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.modelMapper = modelMapper;
        this.loginService = personService;
        this.registrationService = registrationService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Person person = loginService.findPersonByUsername(loginDTO.getUsername());

        String token = jwtGenerator.generateToken(authentication);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(token);
        response.setFirstName(person.getFirstName());
        response.setSecondName(person.getSecondName());
        response.setMiddleName(person.getMiddleName());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterDTO registerDTO) {
        RegisterResponseDTO response = modelMapper
                .map(registrationService.register(registerDTO), RegisterResponseDTO.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
