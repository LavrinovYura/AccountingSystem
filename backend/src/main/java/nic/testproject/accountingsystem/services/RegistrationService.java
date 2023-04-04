package nic.testproject.accountingsystem.services;

import nic.testproject.accountingsystem.dto.authorization.RegisterDTO;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import nic.testproject.accountingsystem.models.user.RoleType;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import nic.testproject.accountingsystem.repositories.user.RoleRepository;
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
        Role role = roleRepository.findByRoleType(RoleType.USER).get();
        person.setRoles(Collections.singleton(role));

        personRepository.save(person);
    }

}
