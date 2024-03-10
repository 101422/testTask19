package ru.vvi.testtask.services;

import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.repositories.PersonRepository;
import ru.vvi.testtask.util.PersonPasswordEncrypt;
import java.sql.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    private final PersonPasswordEncrypt personPasswordEncrypt;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PersonPasswordEncrypt personPasswordEncrypt, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.personPasswordEncrypt = personPasswordEncrypt;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public List<Person> findAllById(int id) {
        return personRepository.findAllById(id);
    }


    public List<Person> findByDateOfBirthGreaterThan(Date dateOfBirth, boolean sortByYear) {
       if (sortByYear)
           return personRepository.findWithSortByDateOfBirthGreaterThan(dateOfBirth, Sort.by("year"));
       else
           return personRepository.findByDateOfBirthGreaterThan(dateOfBirth);
    }
  public List<Person> findWithPaginationAndSortByDateOfBirthGreaterThan(Date dateOfBirth, Integer page, Integer personPerPage, boolean sortByYear) {
      if (sortByYear)
          return personRepository.findWithPaginationByDateOfBirthGreaterThan(dateOfBirth, PageRequest.of(page, personPerPage, Sort.by("year")));
      else
          return personRepository.findWithPaginationByDateOfBirthGreaterThan(dateOfBirth, PageRequest.of(page, personPerPage));
    }

    public List<Person> findByNameLike(String name, boolean sortByYear) {
        if (sortByYear)
            return personRepository.findWithSortByNameLike(name,  Sort.by("year"));
        else
            return personRepository.findByNameLike(name);
    }
    public List<Person> findWithPaginationAndSortByNameLike(String name, Integer page, Integer personPerPage, boolean sortByYear) {
        if (sortByYear)
            return personRepository.findWithPaginationByNameLike(name, PageRequest.of(page, personPerPage, Sort.by("year")));
        else
            return personRepository.findWithPaginationByNameLike(name, PageRequest.of(page, personPerPage));
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = personRepository.findById(id);
        return foundPerson.orElse(null);
    }

    public Person findOneByUsername(String username) {
        Optional<Person> foundPerson = personRepository.findByUsername(username);
        return foundPerson.orElse(null);
    }

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Transactional
    public void save(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        personRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }
}
