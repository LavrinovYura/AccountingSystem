package nic.testprojet.AccountingSystem.services;

import nic.testprojet.AccountingSystem.models.Person;
import nic.testprojet.AccountingSystem.models.Role;
import nic.testprojet.AccountingSystem.repositories.PersonRepository;
import nic.testprojet.AccountingSystem.security.PersonDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class PersonDetailServiceImpl implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Person person = personRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return new User(person.getUsername(), person.getPassword(), mapRolesToAuthorities(person.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}