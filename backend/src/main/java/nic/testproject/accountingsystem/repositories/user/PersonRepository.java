package nic.testproject.accountingsystem.repositories.user;

import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);
    Boolean existsByUsername(String username);
    List<Person> findAllByRoles(Role roles);
}
