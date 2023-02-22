package nic.testprojet.AccountingSystem.services;

import nic.testprojet.AccountingSystem.models.Person;
import nic.testprojet.AccountingSystem.repositories.PersonRepository;
import nic.testprojet.AccountingSystem.security.PersonDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailServiceImpl implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonDetailsImpl loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Person> user = personRepository.findByUsername(userName);

        if(!user.isPresent())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetailsImpl(user.get());
    }
}