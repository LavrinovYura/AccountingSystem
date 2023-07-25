package nic.testproject.accountingsystem.controllers;

import nic.testproject.accountingsystem.dto.RequestName;
import nic.testproject.accountingsystem.dto.administration.RequestRole;
import nic.testproject.accountingsystem.dto.administration.UserDTO;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import nic.testproject.accountingsystem.models.user.RoleType;
import nic.testproject.accountingsystem.services.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTests {
    @Mock
    private AdminService adminService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AdminController adminController;

    @Test
    void testGetUsers_ReturnsListOfUsers() {
        // Arrange
        int page = 0;
        int size = 50;
        Pageable pageable = PageRequest.of(page, size);
        List<Person> persons = Collections.singletonList(new Person());
        Page<Person> personPage = new PageImpl<>(persons);
        when(adminService.getUsers(pageable)).thenReturn(personPage);

        UserDTO userDTO = new UserDTO();
        when(modelMapper.map(any(Person.class), eq(UserDTO.class))).thenReturn(userDTO);

        // Act
        ResponseEntity<List<UserDTO>> response = adminController.getUsers(page, size);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonList(userDTO), response.getBody());
    }

    @Test
    void testGetUsers_ReturnsEmptyList_WhenNoUsersFound() {
        // Arrange
        int page = 0;
        int size = 50;
        Pageable pageable = PageRequest.of(page, size);
        Page<Person> emptyPage = new PageImpl<>(Collections.emptyList());
        when(adminService.getUsers(pageable)).thenReturn(emptyPage);

        // Act
        ResponseEntity<List<UserDTO>> response = adminController.getUsers(page, size);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void testGetUsers_ThrowsEntityNotFoundException_WhenNoContractsInDatabase() {
        // Arrange
        int page = 0;
        int size = 50;
        Pageable pageable = PageRequest.of(page, size);
        when(adminService.getUsers(pageable)).thenThrow(EntityNotFoundException.class);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> adminController.getUsers(page, size));
    }

    @Test
    void testDeleteUser_ReturnsNoContent_WhenUserDeletedSuccessfully() {
        // Arrange
        String username = "john_doe";
        RequestName requestName = new RequestName();
        requestName.setName(username);

        // Act
        ResponseEntity<Void> response = adminController.deleteUser(requestName);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteUser_ThrowsResourceNotFoundException_WhenUserNotFound() {
        // Arrange
        String username = "john_doe";
        RequestName requestName = new RequestName();
        requestName.setName(username);

        doThrow(ResourceNotFoundException.class)
                .when(adminService)
                .deleteUser(username);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> adminController.deleteUser(requestName));
    }


    @Test
    void testAddRole_ReturnsNoContent_WhenRoleAddedSuccessfully() {
        // Arrange
        RequestRole requestRole = new RequestRole();
        requestRole.setRoleType(RoleType.ADMIN);
        requestRole.setName("johndoe");

        // Mock the necessary dependencies
        doNothing().when(adminService).addRole(eq(RoleType.ADMIN), eq("johndoe"));

        // Act
        ResponseEntity<UserDTO> response = adminController.addRole(requestRole);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


    @Test
    void testAddRole_ThrowsResourceNotFoundException_WhenPersonNotFound() {
        // Arrange
        RequestRole requestRole = new RequestRole();
        requestRole.setRoleType(RoleType.ADMIN);
        requestRole.setName("johndoe");

        // Mock the necessary dependencies
        doThrow(ResourceNotFoundException.class)
                .when(adminService)
                .addRole(eq(RoleType.ADMIN), eq("johndoe"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> adminController.addRole(requestRole));
    }



    @Test
    void testAddRole_ThrowsRuntimeException_WhenRoleAlreadyExistsForUser() {
        // Arrange
        RequestRole requestRole = new RequestRole();
        requestRole.setRoleType(RoleType.ADMIN);
        requestRole.setName("johndoe");

        // Mock the necessary dependencies
        doThrow(new RuntimeException("This person doesn't have this role "))
                .when(adminService).addRole(eq(RoleType.ADMIN), eq("johndoe"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> adminController.addRole(requestRole));
    }


    @Test
    void testRemoveRole_ReturnsNoContent_WhenRoleRemovedSuccessfully() {
        // Arrange
        RequestRole requestRole = new RequestRole();
        requestRole.setRoleType(RoleType.ADMIN);
        requestRole.setName("johndoe");

        // Mock the necessary dependencies
        doNothing().when(adminService).removeRole(eq(RoleType.ADMIN), eq("johndoe"));

        // Act
        ResponseEntity<UserDTO> response = adminController.removeRole(requestRole);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testRemoveRole_ThrowsResourceNotFoundException_WhenPersonNotFound() {
        // Arrange
        RequestRole requestRole = new RequestRole();
        requestRole.setRoleType(RoleType.ADMIN);
        requestRole.setName("johndoe");

        // Mock the necessary dependencies
        doThrow(ResourceNotFoundException.class)
                .when(adminService)
                .removeRole(eq(RoleType.ADMIN), eq("johndoe"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> adminController.removeRole(requestRole));
    }

    @Test
    void testRemoveRole_ThrowsRuntimeException_WhenPersonDoesNotHaveRole() {
        // Arrange
        RequestRole requestRole = new RequestRole();
        requestRole.setRoleType(RoleType.ADMIN);
        requestRole.setName("johndoe");

        // Mock the necessary dependencies
        doThrow(RuntimeException.class)
                .when(adminService)
                .removeRole(eq(RoleType.ADMIN), eq("johndoe"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> adminController.removeRole(requestRole));
    }


    @Test
    public void getUsersByRole_ReturnsListOfUserDTO() {
        // Arrange
        int page = 0;
        int size = 50;
        Pageable pageable = PageRequest.of(page, size);
        RequestRole requestRole = new RequestRole();
        requestRole.setRoleType(RoleType.ADMIN);
        List<Person> persons = Collections.singletonList(createPerson());
        Page<Person> pageOfPersons = new PageImpl<>(persons, pageable, persons.size());

        when(adminService.getUsersByRole(pageable, RoleType.ADMIN)).thenReturn(pageOfPersons);
        when(modelMapper.map(any(Person.class), eq(UserDTO.class))).thenReturn(createUserDTO());

        // Act
        ResponseEntity<List<UserDTO>> response = adminController.getUsersByRole(page, size, requestRole);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        UserDTO userDTO = response.getBody().get(0);
        assertEquals("John Doe", userDTO.getFullName());
        assertEquals("johndoe", userDTO.getUsername());
        assertNotNull(userDTO.getRoles());
        assertEquals("2023-06-20", userDTO.getExpireDate());
    }

    private Person createPerson() {
        Person person = new Person();
        person.setFirstName("John");
        person.setSecondName("Doe");
        person.setMiddleName("Smith");
        person.setUsername("johndoe");
        person.setPassword("password");
        person.setRoles(Collections.singleton(createRole()));
        person.setExpireDate(new Date(1234567890L));
        return person;
    }

    private Role createRole() {
        Role role = new Role();
        role.setId(1);
        role.setRoleType(RoleType.ADMIN);
        return role;
    }

    private UserDTO createUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName("John Doe");
        userDTO.setUsername("johndoe");
        userDTO.setRoles(Collections.singletonList(createRole()));
        userDTO.setExpireDate("2023-06-20");
        return userDTO;
    }
}

