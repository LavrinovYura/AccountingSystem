package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.authorization.LoginResponseDTO;
import nic.testproject.accountingsystem.dto.authorization.LoginDTO;
import nic.testproject.accountingsystem.dto.authorization.RegisterDTO;
import nic.testproject.accountingsystem.dto.authorization.RegisterResponseDTO;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import nic.testproject.accountingsystem.services.security.JWT.JWTGenerator;
import nic.testproject.accountingsystem.services.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PersonRepository personRepository;
    private final JWTGenerator jwtGenerator;
    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PersonRepository personRepository,
                          JWTGenerator jwtGenerator, RegistrationService registrationService, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.personRepository = personRepository;
        this.jwtGenerator = jwtGenerator;
        this.registrationService = registrationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Optional<Person> person = personRepository.findByUsername(loginDTO.getUsername());
        String fullName;

        if (person.isPresent()) {
            fullName = person.get().getFullName();
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new LoginResponseDTO(token, fullName), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterDTO registerDTO) {
        if (personRepository.existsByUsername(registerDTO.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        RegisterResponseDTO response = modelMapper
                .map(registrationService.register(registerDTO), RegisterResponseDTO.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
