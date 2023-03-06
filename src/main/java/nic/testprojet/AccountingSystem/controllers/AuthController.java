package nic.testprojet.AccountingSystem.controllers;

import nic.testprojet.AccountingSystem.dto.RegisterDTO;
import nic.testprojet.AccountingSystem.models.Person;
import nic.testprojet.AccountingSystem.models.Role;
import nic.testprojet.AccountingSystem.repositories.PersonRepository;
import nic.testprojet.AccountingSystem.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private PersonRepository personRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PersonRepository personRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        if(personRepository.existByUserName(registerDTO.getUsername())){
            return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
        }
        Person person = new Person();
        person.setUsername(registerDTO.getUsername());
        person.setPassword(passwordEncoder.encode((registerDTO.getPassword())));
        Role role = roleRepository.findByName("USER").get();
        person.setRoles(Collections.singletonList(role));

        personRepository.save(person);

        return new ResponseEntity<>("User register success", HttpStatus.OK);
    }
}
