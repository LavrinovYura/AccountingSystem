package nic.testproject.accountingsystem.repositories.user;

import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.models.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUsername(String username);
    Boolean existsByUsername(String username);
    Page<Person> findAllByRoles(Role role, Pageable pageable);
    List<Person> findByExpireDateBefore(Date date);
}
