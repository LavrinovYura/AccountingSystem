package nic.testproject.accountingsystem.services.user;

import lombok.RequiredArgsConstructor;
import nic.testproject.accountingsystem.models.user.Person;
import nic.testproject.accountingsystem.repositories.user.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonExpirationService {

    private final PersonRepository personRepository;

    @Scheduled(fixedRate = 120000) // каждые 2 минуты
        public void deleteExpiredUsers() {
            Date now = new Date();
            List<Person> expiredUsers = personRepository.findByExpireDateBefore(now);
            personRepository.deleteAll(expiredUsers);
        }
}
