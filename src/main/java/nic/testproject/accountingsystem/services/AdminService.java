package nic.testproject.accountingsystem.services;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.dtos.administration.PersonDTO;
import nic.testproject.accountingsystem.exceptions.ConflictException;
import nic.testproject.accountingsystem.mappers.PersonMapper;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import nic.testproject.accountingsystem.models.user.RoleType;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import nic.testproject.accountingsystem.repositories.user.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PersonMapper personMapper;


    public Set<PersonDTO> getUsers(Pageable pageable) {
        Page<Person> page = personRepository.findAll(pageable);

        if (page.isEmpty()) {
            throw new EntityNotFoundException("No users in database");
        }

        return personMapper.personToDTOSet(page);
    }

    public Set<PersonDTO> getUsersByRole(Pageable pageable, String type) {
        Role role = getRoleByType(type);

        Page<Person> page = personRepository.findAllByRoles(role, pageable);

        if (page.isEmpty()) {
            throw new EntityNotFoundException("There are no users with this role in the database");
        }

        return personMapper.personToDTOSet(page);
    }

    public void deleteUser(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no person with id " + id));

        personRepository.delete(person);
    }

    public void addRole(String type, Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no person with id " + id));

        Role role = getRoleByType(type);

        if (person.getRoles().contains(role)) {
            throw new ConflictException("This person already has this role.");
        }

        person.getRoles().add(role);
        personRepository.save(person);
    }

    public void removeRole(String type, Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no person with id " + id ));
        Role role = getRoleByType(type);

        if (person.getRoles().stream().noneMatch(role::equals)) {
            throw new ConflictException("This person doesn't have this role");
        }

        person.getRoles().remove(role);

        personRepository.save(person);
    }

    private Role getRoleByType(String type) {
        RoleType roleType;

        try {
            roleType = RoleType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("There is no such role exist");
        }

        return roleRepository.findByRoleType(roleType);
    }

}
