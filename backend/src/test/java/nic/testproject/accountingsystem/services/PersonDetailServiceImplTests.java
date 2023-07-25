package nic.testproject.accountingsystem.services;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import nic.testproject.accountingsystem.models.user.RoleType;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import nic.testproject.accountingsystem.services.user.PersonDetailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonDetailServiceImplTests {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonDetailServiceImpl personDetailService;

    @Test
    void loadUserByUsername_ExistingUsername_ReturnsUserDetails() {
        // Arrange
        String username = "existingUsername";
        Person existingPerson = new Person();
        existingPerson.setUsername(username);
        existingPerson.setPassword("password");
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setRoleType(RoleType.USER);
        roles.add(role);
        existingPerson.setRoles(roles);

        when(personRepository.findByUsername(username)).thenReturn(Optional.of(existingPerson));

        // Act
        UserDetails userDetails = personDetailService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(existingPerson.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER")));
    }

    @Test
    void loadUserByUsername_NonExistingUsername_ThrowsUsernameNotFoundException() {
        // Arrange
        String username = "nonExistingUsername";

        when(personRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> personDetailService.loadUserByUsername(username));
    }
}
