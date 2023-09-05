package nic.testproject.accountingsystem.services.user;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.authorization.RegisterDTO;
import nic.testproject.accountingsystem.dtos.authorization.RegisterResponseDTO;
import nic.testproject.accountingsystem.mappers.PersonMapper;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import nic.testproject.accountingsystem.models.user.RoleType;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import nic.testproject.accountingsystem.repositories.user.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PersonMapper personMapper;

    @Value("${RegistrationService.user.expiration}")
    private long EXPIRE_DATE;

    public RegisterResponseDTO register(RegisterDTO registerDTO){
        Person person = new Person();

        person.setUsername(registerDTO.getUsername());
        person.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        person.setFirstName(StringUtils.capitalize(registerDTO.getFirstName().trim()));
        person.setSecondName(StringUtils.capitalize(registerDTO.getSecondName().trim()));
        person.setMiddleName(StringUtils.capitalize(registerDTO.getMiddleName().trim()));

        LocalDateTime now = LocalDateTime.now();
        Instant userExpireInstant = now.plusDays(EXPIRE_DATE).atZone(ZoneId.systemDefault()).toInstant();
        person.setExpireDate(Date.from(userExpireInstant));

        Role role = roleRepository.findByRoleType(RoleType.USER);
        person.setRoles(Collections.singleton(role));

        return personMapper.personToRegisterResponseDTO(personRepository.save(person));
    }

}
