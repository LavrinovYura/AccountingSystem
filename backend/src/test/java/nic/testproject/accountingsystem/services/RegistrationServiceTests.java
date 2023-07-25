package nic.testproject.accountingsystem.services;

import nic.testproject.accountingsystem.dto.authorization.RegisterDTO;
import nic.testproject.accountingsystem.exceptions.ConflictException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTests {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void register_ValidRegisterDTO_SuccessfullyRegistersPerson() {
        // Arrange
        RegisterDTO registerDTO = createRegisterDTO();
        Person person = createPerson();

        when(personRepository.existsByUsername(registerDTO.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByRoleType(RoleType.USER)).thenReturn(Optional.of(createRole()));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        // Act
        Person result = registrationService.register(registerDTO);

        // Assert
        assertNotNull(result);
        assertEquals(person, result);
        assertEquals(registerDTO.getUsername(), result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(StringUtils.capitalize(registerDTO.getFirstName().trim()), result.getFirstName());
        assertEquals(StringUtils.capitalize(registerDTO.getSecondName().trim()), result.getSecondName());
        assertEquals(StringUtils.capitalize(registerDTO.getMiddleName().trim()), result.getMiddleName());
        assertNotNull(result.getExpireDate());
        assertEquals(createRole(), result.getRoles().iterator().next());

        verify(personRepository, times(1)).existsByUsername(registerDTO.getUsername());
        verify(passwordEncoder, times(1)).encode(registerDTO.getPassword());
        verify(roleRepository, times(1)).findByRoleType(RoleType.USER);
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void register_ExistingUsername_ThrowsConflictException() {
        // Arrange
        RegisterDTO registerDTO = createRegisterDTO();

        when(personRepository.existsByUsername(registerDTO.getUsername())).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictException.class, () -> registrationService.register(registerDTO));

        verify(personRepository, times(1)).existsByUsername(registerDTO.getUsername());
        verifyNoMoreInteractions(passwordEncoder, roleRepository, personRepository);
    }

    // Helper methods to create test objects

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
        person.setId(1);
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
