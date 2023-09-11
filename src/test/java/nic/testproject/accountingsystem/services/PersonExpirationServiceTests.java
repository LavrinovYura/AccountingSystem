package nic.testproject.accountingsystem.services;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import nic.testproject.accountingsystem.services.user.PersonExpirationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonExpirationServiceTests {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonExpirationService personExpirationService;

    @Test
    void deleteExpiredUsers_DeletesExpiredUsers() {
        // Arrange
        List<Person> expiredUsers = new ArrayList<>();
        expiredUsers.add(new Person());
        expiredUsers.add(new Person());

        when(personRepository.findByExpireDateBefore(any(Date.class))).thenReturn(expiredUsers);

        // Act
        personExpirationService.deleteExpiredUsers();

        // Assert
        verify(personRepository, times(1)).deleteAll(expiredUsers);
    }
}