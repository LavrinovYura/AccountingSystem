package nic.testproject.accountingsystem.services;

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
            throw new EntityNotFoundException("No Contracts in database");
        return page;

    }
    public List<Person> getUsersByRole(Pageable pageable, RoleType type) {
        Role roles = roleRepository.findByRoleType(type).get();
        return personRepository.findAllByRoles(roles);
    }
    public void deleteUser(String name) {
        Optional<Person> optionalPerson = personRepository.findByUsername(name);
        if (!optionalPerson.isPresent())
            throw new ResourceNotFoundException();

        personRepository.delete(optionalPerson.get());
    }

    public void addRole(RoleType roleType, String name) {
        Optional<Person> optionalPerson = personRepository.findByUsername(name);
        Optional<Role> optionalRole = roleRepository.findByRoleType(roleType);
        if (optionalPerson.isPresent() && optionalRole.isPresent()) {
            Person person = optionalPerson.get();
            Role role = optionalRole.get();

            if (person.getRoles().stream().anyMatch(r -> r.equals(role))) {
                throw new RuntimeException("This person doesn't have this role ");
            }
            person.getRoles().add(role);

            personRepository.save(person);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public void removeRole(RoleType roleType, String name) {
        Optional<Person> optionalPerson = personRepository.findByUsername(name);
        Optional<Role> optionalRole = roleRepository.findByRoleType(roleType);
        if (optionalPerson.isPresent() && optionalRole.isPresent()) {
            Person person = optionalPerson.get();
            Role role = optionalRole.get();

            if (person.getRoles().stream().anyMatch(r -> r.equals(role))) {
                throw new RuntimeException("This person doesn't have this role");
            }
            person.getRoles().remove(role);

            personRepository.save(person);
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
