package nic.testproject.accountingsystem.services;

import nic.testproject.accountingsystem.dtos.administration.PersonDTO;
import nic.testproject.accountingsystem.exceptions.ConflictException;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.mappers.PersonMapper;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTests {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getUsers_NonEmptyPage_ReturnsPageOfUsers() {
        Set<PersonDTO> usersSet = createTestUserSet();
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Person> page = new PageImpl<>(Collections.singletonList(new Person()), pageable, usersSet.size());

        when(personRepository.findAll(pageable)).thenReturn(page);
        when(personMapper.personToDTOSet(page)).thenReturn(usersSet);

        Set<PersonDTO> result = adminService.getUsers(pageable);

        assertNotNull(result);
        assertEquals(usersSet, result);

        verify(personRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(personRepository);
        verify(personMapper, times(1)).personToDTOSet(page);
        verifyNoMoreInteractions(personMapper);
    }

    @Test
    void getUsers_EmptyPage_ThrowsEntityNotFoundException() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Person> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(personRepository.findAll(pageable)).thenReturn(emptyPage);

        assertThrows(EntityNotFoundException.class, () -> adminService.getUsers(pageable));

        verify(personRepository, times(1)).findAll(pageable);

        verifyNoMoreInteractions(personRepository);
        verifyNoInteractions(personMapper);
    }

    @Test
    public void getUsersByRole_RoleExists_ReturnsPageOfUsers() {
        RoleType roleType = RoleType.ADMIN;
        Role role = new Role();
        role.setRoleType(roleType);
        Pageable pageable = Pageable.unpaged();
        Set<PersonDTO> personDTOSet = createTestUserSet();
        Page<Person> page = new PageImpl<>( Collections.singletonList(new Person()), pageable, personDTOSet.size());

        when(roleRepository.findByRoleType(roleType)).thenReturn(role);
        when(personRepository.findAllByRoles(role, pageable)).thenReturn(page);
        when(personMapper.personToDTOSet(page)).thenReturn(personDTOSet);

        Set<PersonDTO> result = adminService.getUsersByRole(pageable, String.valueOf(roleType));

        assertEquals(personDTOSet, result);

        verify(roleRepository, times(1)).findByRoleType(roleType);
        verify(personRepository, times(1)).findAllByRoles(role, pageable);
        verifyNoMoreInteractions(roleRepository, personRepository);
    }

    @Test
    public void getUsersByRole_RoleDoesNotExist_ThrowsResourceNotFoundException() {
        String roleType = "NOT EXISTING ROLE";

        assertThrows(EntityNotFoundException.class, () -> adminService.getUsersByRole(Pageable.unpaged(), roleType));

        verifyNoMoreInteractions(roleRepository, personRepository);
        verifyNoInteractions(personMapper);
    }

    @Test
    public void getUsersByRole_NoUsersWithRole_ThrowsEntityNotFoundException() {
        RoleType roleType = RoleType.ADMIN;
        Role role = new Role();
        role.setRoleType(roleType);
        Pageable pageable = Pageable.unpaged();
        Page<Person> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(roleRepository.findByRoleType(roleType)).thenReturn(role);
        when(personRepository.findAllByRoles(role, pageable)).thenReturn(emptyPage);

        assertThrows(EntityNotFoundException.class, () -> adminService.getUsersByRole(pageable, String.valueOf(roleType)));

        verify(roleRepository, times(1)).findByRoleType(roleType);
        verify(personRepository, times(1)).findAllByRoles(role, pageable);
        verifyNoMoreInteractions(roleRepository, personRepository);
        verifyNoInteractions(personMapper);
    }

    @Test
    public void deleteUser_ExistingUser_DeletesUser() {
        Long id = 1L;
        Optional<Person> optionalPerson = Optional.of(new Person());
        Person existingPerson = optionalPerson.get();

        when(personRepository.findById(id)).thenReturn(optionalPerson);
        doNothing().when(personRepository).delete(existingPerson);

        adminService.deleteUser(id);

        verify(personRepository, times(1)).delete(existingPerson);
        verify(personRepository, times(1)).findById(id);
    }

    @Test
    public void deleteUser_NonExistingUser_ThrowsResourceNotFoundException() {
        Long id = 1L;
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adminService.deleteUser(id));

        verify(personRepository, times(1)).findById(id);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    public void addRole_PersonAndRoleExist_AddsRoleToPerson() {

        Long id = 1L;
        RoleType roleType = RoleType.ADMIN;

        Person existingPerson = new Person();
        existingPerson.setId(id);

        Role existingRole = new Role();
        existingRole.setRoleType(roleType);

        Optional<Person> optionalPerson = Optional.of(existingPerson);
        Optional<Role> optionalRole = Optional.of(existingRole);

        when(personRepository.findById(id)).thenReturn(optionalPerson);
        when(roleRepository.findByRoleType(roleType)).thenReturn(optionalRole.get());
        when(personRepository.save(existingPerson)).thenReturn(existingPerson);

        assertDoesNotThrow(() -> adminService.addRole(String.valueOf(roleType),id));
        assertTrue(existingPerson.getRoles().contains(existingRole));

        verify(personRepository, times(1)).findById(id);
        verify(roleRepository, times(1)).findByRoleType(roleType);
        verify(personRepository, times(1)).save(existingPerson);
    }

    @Test
    public void addRole_PersonDoesNotExist_ThrowsResourceNotFoundException() {
        Long id = 1L;
        RoleType roleType = RoleType.ADMIN;

        when(personRepository.findById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> adminService.addRole(String.valueOf(roleType), id));

        verify(personRepository, times(1)).findById(id);
        verifyNoMoreInteractions(personRepository);
        verifyNoInteractions(roleRepository);
    }

    @Test
    public void addRole_RoleDoesNotExist_ThrowsResourceNotFoundException() {
        Long id = 1L;
        String type = "NOT EXISTING ROLE";

        Person existingPerson = new Person();
        existingPerson.setId(id);

        Optional<Person> optionalPerson = Optional.of(existingPerson);

        when(personRepository.findById(id)).thenReturn(optionalPerson);

        assertThrows(EntityNotFoundException.class, () -> adminService.addRole(type, id));

        verify(personRepository, times(1)).findById(id);
        verifyNoMoreInteractions(personRepository);
        verifyNoInteractions(roleRepository);
    }

    @Test
    public void addRole_PersonHasRole_ThrowsRuntimeException() {
        // Arrange
        Long id = 1L;
        RoleType roleType = RoleType.ADMIN;

        Person existingPerson = new Person();
        existingPerson.setId(id);
        Role existingRole = new Role();
        existingRole.setRoleType(roleType);
        existingPerson.setRoles(Collections.singleton(existingRole));

        Optional<Person> optionalPerson = Optional.of(existingPerson);
        Optional<Role> optionalRole = Optional.of(existingRole);

        when(personRepository.findById(id)).thenReturn(optionalPerson);
        when(roleRepository.findByRoleType(roleType)).thenReturn(optionalRole.get());

        assertThrows(ConflictException.class, () -> adminService.addRole(String.valueOf(roleType), id));

        verify(personRepository, times(1)).findById(id);
        verify(roleRepository, times(1)).findByRoleType(roleType);
        verifyNoMoreInteractions(personRepository);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void removeRole_PersonAndRoleExist_RemovesRoleFromPerson() {
        // Arrange
        Long id = 1L;
        RoleType roleType = RoleType.ADMIN;

        Person existingPerson = new Person();
        existingPerson.setId(id);

        Role existingRole = new Role();
        existingRole.setRoleType(roleType);

        Set<Role> roles = new HashSet<>();
        roles.add(existingRole);
        existingPerson.setRoles(roles);

        Optional<Person> optionalPerson = Optional.of(existingPerson);
        Optional<Role> optionalRole = Optional.of(existingRole);

        when(personRepository.findById(id)).thenReturn(optionalPerson);
        when(roleRepository.findByRoleType(roleType)).thenReturn(optionalRole.get());
        when(personRepository.save(existingPerson)).thenReturn(existingPerson);

        assertDoesNotThrow(() -> adminService.removeRole(String.valueOf(roleType), id));

        assertFalse(existingPerson.getRoles().contains(existingRole));

        verify(personRepository, times(1)).findById(id);
        verify(roleRepository, times(1)).findByRoleType(roleType);
        verify(personRepository, times(1)).save(existingPerson);
    }

    @Test
    public void removeRole_PersonDoesNotExist_ThrowsResourceNotFoundException() {
        Long id = 1L;
        RoleType roleType = RoleType.ADMIN;

        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adminService.removeRole(String.valueOf(roleType), id));

        verify(personRepository, times(1)).findById(id);
        verifyNoMoreInteractions(personRepository);
        verifyNoInteractions(roleRepository);
    }

    @Test
    public void removeRole_RoleDoesNotExist_ThrowsResourceNotFoundException() {
        Long id = 1L;
        String roleType = "NOT EXISTING ROLE";

        Person existingPerson = new Person();
        existingPerson.setId(id);

        Optional<Person> optionalPerson = Optional.of(existingPerson);

        when(personRepository.findById(id)).thenReturn(optionalPerson);

        assertThrows(EntityNotFoundException.class, () -> adminService.removeRole(roleType, id));

        verify(personRepository, times(1)).findById(id);
        verifyNoMoreInteractions(personRepository);
        verifyNoInteractions(roleRepository);
    }

    @Test
    public void removeRole_PersonDoesNotHaveRole_ThrowsRuntimeException() {
        Long id = 1L;
        RoleType roleType = RoleType.ADMIN;

        Person existingPerson = new Person();
        existingPerson.setId(id);

        Role existingRole = new Role();
        existingRole.setRoleType(roleType);

        Optional<Person> optionalPerson = Optional.of(existingPerson);
        Optional<Role> optionalRole = Optional.of(existingRole);

        when(personRepository.findById(id)).thenReturn(optionalPerson);
        when(roleRepository.findByRoleType(roleType)).thenReturn(optionalRole.get());

        assertThrows(RuntimeException.class, () -> adminService.removeRole(String.valueOf(roleType), id));

        verify(personRepository, times(1)).findById(id);
        verify(roleRepository, times(1)).findByRoleType(roleType);
        verifyNoMoreInteractions(personRepository);
        verifyNoMoreInteractions(roleRepository);
    }

    private Set<PersonDTO> createTestUserSet() {
        Set<PersonDTO> userList = new HashSet<>();
        userList.add(createTestUser(1L, "John"));
        userList.add(createTestUser(2L, "Jane"));
        userList.add(createTestUser(3L, "Alice"));
        return userList;
    }

    private PersonDTO createTestUser(Long id, String username) {
        PersonDTO user = new PersonDTO();
        user.setId(id);
        user.setUsername(username);

        return user;
    }
}
