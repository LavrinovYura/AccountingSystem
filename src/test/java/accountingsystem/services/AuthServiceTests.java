//package accountingsystem.services;
//
//import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
//import nic.testproject.accountingsystem.models.user.Person;
//import nic.testproject.accountingsystem.repositories.user.PersonRepository;
//import nic.testproject.accountingsystem.services.user.AuthService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class AuthServiceTests {
//
//    @Mock
//    private PersonRepository personRepository;
//
//    @InjectMocks
//    private AuthService authService;
//
//    @Test
//    void findPersonByUsername_ExistingUsername_ReturnsPerson() {
//        // Arrange
//        String username = "existingUsername";
//        Person existingPerson = new Person();
//        existingPerson.setUsername(username);
//        Optional<Person> optionalPerson = Optional.of(existingPerson);
//
//        when(personRepository.findByUsername(username)).thenReturn(optionalPerson);
//
//        // Act
//        Person result = authService.findPersonByUsername(username);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(existingPerson, result);
//    }
//
//    @Test
//    void findPersonByUsername_NonExistingUsername_ThrowsResourceNotFoundException() {
//        // Arrange
//        String username = "nonExistingUsername";
//        Optional<Person> optionalPerson = Optional.empty();
//
//        when(personRepository.findByUsername(username)).thenReturn(optionalPerson);
//
//        // Act and Assert
//        assertThrows(ResourceNotFoundException.class, () -> authService.findPersonByUsername(username));
//    }
//}