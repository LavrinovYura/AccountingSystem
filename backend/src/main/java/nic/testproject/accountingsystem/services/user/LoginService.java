package nic.testproject.accountingsystem.services.user;

import nic.testproject.accountingsystem.exceptions.ResourceNotFoundException;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final PersonRepository personRepository;

    @Autowired
    public LoginService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findPersonByUsername(String username){
        Optional<Person> optionalPerson = personRepository.findByUsername(username);
        if(!optionalPerson.isPresent())
            throw new ResourceNotFoundException();
        return optionalPerson.get();
    }
}
