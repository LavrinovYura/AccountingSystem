package nic.testproject.accountingsystem.services;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import nic.testproject.accountingsystem.services.user.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTests {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private LoginService loginService;

    @Test
    void findPersonByUsername_ExistingUsername_ReturnsPerson() {
        // Arrange
        String username = "existingUsername";
        Person existingPerson = new Person();
        existingPerson.setUsername(username);
        Optional<Person> optionalPerson = Optional.of(existingPerson);

        when(personRepository.findByUsername(username)).thenReturn(optionalPerson);

        // Act
        Person result = loginService.findPersonByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(existingPerson, result);
    }

    @Test
    void findPersonByUsername_NonExistingUsername_ThrowsResourceNotFoundException() {
        // Arrange
        String username = "nonExistingUsername";
        Optional<Person> optionalPerson = Optional.empty();

        when(personRepository.findByUsername(username)).thenReturn(optionalPerson);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> loginService.findPersonByUsername(username));
    }
}