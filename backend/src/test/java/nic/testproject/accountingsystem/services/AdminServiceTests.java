package nic.testproject.accountingsystem.services;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import nic.testproject.accountingsystem.models.user.RoleType;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import nic.testproject.accountingsystem.repositories.user.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTests {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getUsers_NonEmptyPage_ReturnsPageOfUsers() {
        // Arrange
        List<Person> userList = createTestUserList();
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Person> page = new PageImpl<>(userList, pageable, userList.size());

        when(personRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<Person> result = adminService.getUsers(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(page, result);
        assertEquals(userList, result.getContent());

        verify(personRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void getUsers_EmptyPage_ThrowsEntityNotFoundException() {
        // Arrange
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Person> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(personRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> adminService.getUsers(pageable));

        verify(personRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    public void getUsersByRole_RoleExists_ReturnsPageOfUsers() {
        // Arrange
        RoleType roleType = RoleType.ADMIN;
        Role role = new Role();
        role.setRoleType(roleType);
        Pageable pageable = Pageable.unpaged();
        List<Person> persons = Collections.singletonList(new Person());
        Page<Person> expectedPage = new PageImpl<>(persons, pageable, persons.size());

        when(roleRepository.findByRoleType(roleType)).thenReturn(Optional.of(role));
        when(personRepository.findAllByRoles(role, pageable)).thenReturn(expectedPage);

        // Act
        Page<Person> result = adminService.getUsersByRole(pageable, roleType);

        // Assert
        assertEquals(expectedPage, result);
        assertEquals(persons, result.getContent());

        verify(roleRepository, times(1)).findByRoleType(roleType);
        verify(personRepository, times(1)).findAllByRoles(role, pageable);
        verifyNoMoreInteractions(roleRepository, personRepository);
    }

    @Test
    public void getUsersByRole_RoleDoesNotExist_ThrowsEntityNotFoundException() {
        // Arrange
        RoleType roleType = RoleType.ADMIN;

        when(roleRepository.findByRoleType(roleType)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> adminService.getUsersByRole(Pageable.unpaged(), roleType));

        verify(roleRepository, times(1)).findByRoleType(roleType);
        verifyNoMoreInteractions(roleRepository, personRepository);
    }

    @Test
    public void getUsersByRole_NoUsersWithRole_ThrowsEntityNotFoundException() {
        // Arrange
        RoleType roleType = RoleType.ADMIN;
        Role role = new Role();
        role.setRoleType(roleType);
        Pageable pageable = Pageable.unpaged();
        Page<Person> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(roleRepository.findByRoleType(roleType)).thenReturn(Optional.of(role));
        when(personRepository.findAllByRoles(role, pageable)).thenReturn(emptyPage);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> adminService.getUsersByRole(pageable, roleType));

        verify(roleRepository, times(1)).findByRoleType(roleType);
        verify(personRepository, times(1)).findAllByRoles(role, pageable);
        verifyNoMoreInteractions(roleRepository, personRepository);
    }

    @Test
    public void deleteUser_ExistingUser_DeletesUser() {
        // Arrange
        String username = "existingUser";
        Person existingUser = new Person();
        existingUser.setUsername(username);
        Optional<Person> optionalPerson = Optional.of(existingUser);

        when(personRepository.findByUsername(username)).thenReturn(optionalPerson);

        // Act
        adminService.deleteUser(username);

        // Assert
        verify(personRepository, times(1)).delete(existingUser);
    }

    @Test
    public void deleteUser_NonExistingUser_ThrowsResourceNotFoundException() {
        // Arrange
        String username = "nonExistingUser";

        when(personRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> adminService.deleteUser(username));

        verify(personRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    public void addRole_PersonAndRoleExist_AddsRoleToPerson() {
        // Arrange
        String username = "existingUser";
        RoleType roleType = RoleType.ADMIN;

        Person existingPerson = new Person();
        existingPerson.setUsername(username);

        Role existingRole = new Role();
        existingRole.setRoleType(roleType);

        Optional<Person> optionalPerson = Optional.of(existingPerson);
        Optional<Role> optionalRole = Optional.of(existingRole);

        when(personRepository.findByUsername(username)).thenReturn(optionalPerson);
        when(roleRepository.findByRoleType(roleType)).thenReturn(optionalRole);
        when(personRepository.save(existingPerson)).thenReturn(existingPerson);

        // Act
        assertDoesNotThrow(() -> adminService.addRole(roleType, username));

        // Assert
        assertTrue(existingPerson.getRoles().contains(existingRole));

        verify(personRepository, times(1)).findByUsername(username);
        verify(roleRepository, times(1)).findByRoleType(roleType);
        verify(personRepository, times(1)).save(existingPerson);
    }

    @Test
    public void addRole_PersonDoesNotExist_ThrowsResourceNotFoundException() {
        // Arrange
        String username = "nonExistingUser";
        RoleType roleType = RoleType.ADMIN;

        when(personRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> adminService.addRole(roleType, username));

        verify(personRepository, times(1)).findByUsername(username);
        verify(roleRepository, times(1)).findByRoleType(roleType);
        verifyNoMoreInteractions(personRepository);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void addRole_RoleDoesNotExist_ThrowsResourceNotFoundException() {
        // Arrange
        String username = "existingUser";
        RoleType roleType = RoleType.ADMIN;

        Person existingPerson = new Person();
        existingPerson.setUsername(username);

        Optional<Person> optionalPerson = Optional.of(existingPerson);
        Optional<Role> optionalRole = Optional.empty();

        when(personRepository.findByUsername(username)).thenReturn(optionalPerson);
        when(roleRepository.findByRoleType(roleType)).thenReturn(optionalRole);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> adminService.addRole(roleType, username));

        verify(personRepository, times(1)).findByUsername(username);
        verify(roleRepository, times(1)).findByRoleType(roleType);
        verifyNoMoreInteractions(personRepository);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void addRole_PersonHasRole_ThrowsRuntimeException() {
        // Arrange
        String username = "existingUser";
        RoleType roleType = RoleType.ADMIN;

        Person existingPerson = new Person();
        existingPerson.setUsername(username);
        Role existingRole = new Role();
        existingRole.setRoleType(roleType);
        existingPerson.setRoles(Collections.singleton(existingRole));

        Optional<Person> optionalPerson = Optional.of(existingPerson);
        Optional<Role> optionalRole = Optional.of(existingRole);

        when(personRepository.findByUsername(username)).thenReturn(optionalPerson);
        when(roleRepository.findByRoleType(roleType)).thenReturn(optionalRole);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> adminService.addRole(roleType, username));

        verify(personRepository, times(1)).findByUsername(username);
        verify(roleRepository, times(1)).findByRoleType(roleType);
        verifyNoMoreInteractions(personRepository);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void removeRole_PersonAndRoleExist_RemovesRoleFromPerson() {
        // Arrange
        String username = "existingUser";
        RoleType roleType = RoleType.ADMIN;

        Person existingPerson = new Person();
        existingPerson.setUsername(username);

        Role existingRole = new Role();
        existingRole.setRoleType(roleType);

        Set<Role> roles = new HashSet<>();
        roles.add(existingRole);
        existingPerson.setRoles(roles);

        Optional<Person> optionalPerson = Optional.of(existingPerson);
        Optional<Role> optionalRole = Optional.of(existingRole);

        when(personRepository.findByUsername(username)).thenReturn(optionalPerson);
        when(roleRepository.findByRoleType(roleType)).thenReturn(optionalRole);
        when(personRepository.save(existingPerson)).thenReturn(existingPerson);

        // Act
        assertDoesNotThrow(() -> adminService.removeRole(roleType, username));

        // Assert
        assertFalse(existingPerson.getRoles().contains(existingRole));

        verify(personRepository, times(1)).findByUsername(username);
        verify(roleRepository, times(1)).findByRoleType(roleType);
        verify(personRepository, times(1)).save(existingPerson);
    }

    @Test
    public void removeRole_PersonDoesNotExist_ThrowsResourceNotFoundException() {
        // Arrange
        String username = "nonExistingUser";
        RoleType roleType = RoleType.ADMIN;

        when(personRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> adminService.removeRole(roleType, username));

        verify(personRepository, times(1)).findByUsername(username);
        verify(roleRepository, times(1)).findByRoleType(roleType);
        verifyNoMoreInteractions(personRepository);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void removeRole_RoleDoesNotExist_ThrowsResourceNotFoundException() {
        // Arrange
        String username = "existingUser";
        RoleType roleType = RoleType.ADMIN;

        Person existingPerson = new Person();
        existingPerson.setUsername(username);

        Optional<Person> optionalPerson = Optional.of(existingPerson);
        Optional<Role> optionalRole = Optional.empty();

        when(personRepository.findByUsername(username)).thenReturn(optionalPerson);
        when(roleRepository.findByRoleType(roleType)).thenReturn(optionalRole);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> adminService.removeRole(roleType, username));

        verify(personRepository, times(1)).findByUsername(username);
        verify(roleRepository, times(1)).findByRoleType(roleType);
        verifyNoMoreInteractions(personRepository);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void removeRole_PersonDoesNotHaveRole_ThrowsRuntimeException() {
        // Arrange
        String username = "existingUser";
        RoleType roleType = RoleType.ADMIN;

        Person existingPerson = new Person();
        existingPerson.setUsername(username);

        Role existingRole = new Role();
        existingRole.setRoleType(roleType);

        Optional<Person> optionalPerson = Optional.of(existingPerson);
        Optional<Role> optionalRole = Optional.of(existingRole);

        when(personRepository.findByUsername(username)).thenReturn(optionalPerson);
        when(roleRepository.findByRoleType(roleType)).thenReturn(optionalRole);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> adminService.removeRole(roleType, username));

        verify(personRepository, times(1)).findByUsername(username);
        verify(roleRepository, times(1)).findByRoleType(roleType);
        verifyNoMoreInteractions(personRepository);
        verifyNoMoreInteractions(roleRepository);
    }

    // Helper method to create a test user list
    private List<Person> createTestUserList() {
        List<Person> userList = new ArrayList<>();
        userList.add(createTestUser(1, "John"));
        userList.add(createTestUser(2, "Jane"));
        userList.add(createTestUser(3, "Alice"));
        return userList;
    }

    // Helper method to create a test user
    private Person createTestUser(Integer id, String username) {
        Person user = new Person();
        user.setId(id);
        user.setUsername(username);
        // Set other properties as needed
        return user;
    }
}
