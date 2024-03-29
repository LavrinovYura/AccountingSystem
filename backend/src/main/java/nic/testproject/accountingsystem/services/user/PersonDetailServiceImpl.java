package nic.testproject.accountingsystem.services.user;

import nic.testproject.accountingsystem.exceptions.UserNotFoundException;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
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
       Person person = personRepository.findByUsername(username).
               orElseThrow(()-> new UserNotFoundException("User not found"));
        return new User(person.getUsername(), person.getPassword(), mapRolesToAuthorities(person.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleType().toString())).collect(Collectors.toList());
    }
}