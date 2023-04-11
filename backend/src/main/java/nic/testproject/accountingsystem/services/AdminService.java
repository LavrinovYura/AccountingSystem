package nic.testproject.accountingsystem.services;

import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import nic.testproject.accountingsystem.models.user.RoleType;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import nic.testproject.accountingsystem.repositories.user.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
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
        return personRepository.findAll(pageable);
    }
    public List<Person> getUsersByRole(Pageable pageable, RoleType type) {
        Role roles = roleRepository.findByRoleType(type).get();
        return personRepository.findAllByRole(roles);
    }
    public void deleteUser(String name) {
        Optional<Person> optionalPerson = personRepository.findByUsername(name);
        if (optionalPerson.isPresent()) {
            personRepository.delete(optionalPerson.get());
        } else {
            throw new EntityNotFoundException("User with username " + name + " not found");
        }
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
            throw new EntityNotFoundException("User with username " + name + " not found");
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
            throw new EntityNotFoundException("User with username " + name + " not found");
        }
    }
}
