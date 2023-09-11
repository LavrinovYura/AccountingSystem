package nic.testproject.accountingsystem.services;

import nic.testproject.accountingsystem.dtos.authorization.RegisterDTO;
import nic.testproject.accountingsystem.dtos.authorization.RegisterResponseDTO;
import nic.testproject.accountingsystem.mappers.PersonMapper;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import nic.testproject.accountingsystem.models.user.RoleType;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import nic.testproject.accountingsystem.repositories.user.RoleRepository;
import nic.testproject.accountingsystem.services.user.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTests {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void register_ValidRegisterDTO_SuccessfullyRegistersPerson() {
        RegisterDTO registerDTO = createRegisterDTO();
        Person person = createPerson();
        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();
        registerResponseDTO.setFirstName("John");
        registerResponseDTO.setSecondName("Doe");
        registerResponseDTO.setMiddleName("Smith");

        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByRoleType(RoleType.USER)).thenReturn(createRole());
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(personMapper.personToRegisterResponseDTO(person)).thenReturn(registerResponseDTO);
        RegisterResponseDTO result = registrationService.register(registerDTO);

        assertNotNull(result);
        assertEquals(StringUtils.capitalize(registerDTO.getFirstName().trim()), result.getFirstName());
        assertEquals(StringUtils.capitalize(registerDTO.getSecondName().trim()), result.getSecondName());
        assertEquals(StringUtils.capitalize(registerDTO.getMiddleName().trim()), result.getMiddleName());

        verify(passwordEncoder, times(1)).encode(registerDTO.getPassword());
        verify(roleRepository, times(1)).findByRoleType(RoleType.USER);
        verify(personRepository, times(1)).save(any(Person.class));
    }


    private RegisterDTO createRegisterDTO() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("testpassword");
        registerDTO.setFirstName("John");
        registerDTO.setSecondName("Doe");
        registerDTO.setMiddleName("Smith");
        return registerDTO;
    }

    private Person createPerson() {
        Person person = new Person();
        person.setId(1L);
        person.setUsername("testuser");
        person.setPassword("encodedPassword");
        person.setFirstName("John");
        person.setSecondName("Doe");
        person.setMiddleName("Smith");
        person.setExpireDate(new Date());
        person.setRoles(Collections.singleton(createRole()));
        return person;
    }

    private Role createRole() {
        Role role = new Role();
        role.setId(1);
        role.setRoleType(RoleType.USER);
        return role;
    }
}
