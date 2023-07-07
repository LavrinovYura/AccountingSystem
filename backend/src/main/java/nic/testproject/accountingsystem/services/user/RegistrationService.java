package nic.testproject.accountingsystem.services.user;

import nic.testproject.accountingsystem.dto.authorization.RegisterDTO;
import nic.testproject.accountingsystem.exceptions.ConflictException;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import nic.testproject.accountingsystem.models.user.RoleType;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import nic.testproject.accountingsystem.repositories.user.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;

@Service
@Transactional
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
    public Person register(RegisterDTO registerDTO){
        String username = registerDTO.getUsername();

        if (personRepository.existsByUsername(username))
            throw new ConflictException("Username " + username + " already taken");

        Person person = new Person();

        person.setUsername(registerDTO.getUsername());
        person.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        person.setFirstName(StringUtils.capitalize(registerDTO.getFirstName().trim()));
        person.setSecondName(StringUtils.capitalize(registerDTO.getSecondName().trim()));
        person.setMiddleName(StringUtils.capitalize(registerDTO.getMiddleName().trim()));

        Date date = new Date();
        person.setExpireDate(new Date(date.getTime() + 900000000));

        Role role = roleRepository.findByRoleType(RoleType.USER).get();
        person.setRoles(Collections.singleton(role));

        return personRepository.save(person);
    }

}
