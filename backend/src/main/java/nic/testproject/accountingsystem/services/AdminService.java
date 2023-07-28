package nic.testproject.accountingsystem.services;

import nic.testproject.accountingsystem.exceptions.ConflictException;
import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import nic.testproject.accountingsystem.models.user.RoleType;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import nic.testproject.accountingsystem.repositories.user.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class AdminService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminService(PersonRepository personRepository, RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
    }

    public Page<Person> getUsers(Pageable pageable) {
        Page<Person> page = personRepository.findAll(pageable);
        if (page.isEmpty())
            throw new EntityNotFoundException("No users in database");
        return page;
    }

    public Page<Person> getUsersByRole(Pageable pageable, String type) {
        Role role = getRoleByType(type);

        Page<Person> page = personRepository.findAllByRoles(role, pageable);

        if (page.isEmpty())
            throw new EntityNotFoundException("There are no users with this role in the database");

        return page;
    }


    public void deleteUser(String name) {
        Optional<Person> optionalPerson = personRepository.findByUsername(name);
        if (!optionalPerson.isPresent())
            throw new ResourceNotFoundException("There is no person with name" + name);

        personRepository.delete(optionalPerson.get());
    }

    public void addRole(String type, String name) {
        Optional<Person> optionalPerson = personRepository.findByUsername(name);
        Role role = getRoleByType(type);

        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();

            if (person.getRoles().contains(role)) {
                throw new ConflictException("This person already has this role.");
            }

            person.getRoles().add(role);
            personRepository.save(person);
        } else {
            throw new ResourceNotFoundException("There is no person with name" + name);
        }
    }

    public void removeRole(String type, String name) {
        Optional<Person> optionalPerson = personRepository.findByUsername(name);
        Role role = getRoleByType(type);

        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();

            if (person.getRoles().stream().noneMatch(r -> r.equals(role))) {
                throw new ConflictException("This person doesn't have this role");
            }
            person.getRoles().remove(role);

            personRepository.save(person);
        } else {
            throw new ResourceNotFoundException("There is no person with name" + name);
        }
    }

    private Role getRoleByType(String type) {
        RoleType roleType;

        try {
            roleType = RoleType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("There is no such role exist");
        }

        return roleRepository.findByRoleType(roleType);
    }

}
