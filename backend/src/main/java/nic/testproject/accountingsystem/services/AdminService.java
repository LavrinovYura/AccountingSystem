package nic.testproject.accountingsystem.services;

import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import nic.testproject.accountingsystem.models.user.RoleType;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class AdminService {

    private final PersonRepository personRepository;

    @Autowired
    public AdminService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Page<Person> getUsers(Pageable pageable) {
        return personRepository.findAll(pageable);
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
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            Role role = new Role();
            person.getRoles().add();
            personRepository.delete(optionalPerson.get());
        } else {
            throw new EntityNotFoundException("User with username " + name + " not found");
        }
    }
}
