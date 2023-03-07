package nic.testprojet.AccountingSystem.services;

import nic.testprojet.AccountingSystem.dto.RegisterDTO;
import nic.testprojet.AccountingSystem.models.Person;
import nic.testprojet.AccountingSystem.models.Role;
import nic.testprojet.AccountingSystem.repositories.PersonRepository;
import nic.testprojet.AccountingSystem.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;

@Service
public class RegistrationService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public RegistrationService(PersonRepository personRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void register(RegisterDTO registerDTO){
        Person person = new Person();
        person.setUsername(registerDTO.getUsername());
        person.setFullName(registerDTO.getFullName());
        person.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        Date date = new Date();
        person.setExpireDate(String.valueOf(date));
        Role role = roleRepository.findByName("USER").get();
        person.setRoles(Collections.singletonList(role));

        personRepository.save(person);
    }

}
