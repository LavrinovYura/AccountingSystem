package nic.testprojet.AccountingSystem.repositories;

import nic.testprojet.AccountingSystem.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String userName);
}
