package ru.vvi.testtask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.models.PersonPhoneNumber;
import ru.vvi.testtask.repositories.PersonPhoneNumberRepository;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class PersonPhoneNumberService {

    private final PersonPhoneNumberRepository personPhoneNumberRepository;

    @Autowired
    public PersonPhoneNumberService(PersonPhoneNumberRepository personPhoneNumberRepository) {
        this.personPhoneNumberRepository = personPhoneNumberRepository;
    }

    public List<PersonPhoneNumber> findAll() {
        return personPhoneNumberRepository.findAll();
    }

    public List<PersonPhoneNumber> findAllByPerson(Person user) {
        return personPhoneNumberRepository.findAllByPerson(user);
    }

    public PersonPhoneNumber findOne(int id) {
        Optional<PersonPhoneNumber> foundPhoneNumber= personPhoneNumberRepository.findById(id);
        return foundPhoneNumber.orElse(null);
    }

    public PersonPhoneNumber findOneByPhoneNumber(String phoneNumber) {
        Optional<PersonPhoneNumber> foundPhoneNumber = personPhoneNumberRepository.findByPhoneNumber(phoneNumber);
        return foundPhoneNumber.orElse(null);
    }

    public Optional<PersonPhoneNumber> getPhoneNumberByPhoneNumber(String phoneNumber) {
        return personPhoneNumberRepository.findByPhoneNumber(phoneNumber);
    }

    @Transactional
    public void save(PersonPhoneNumber userPhoneNumber) {
        personPhoneNumberRepository.save(userPhoneNumber);
    }

    @Transactional
    public void update(int id, PersonPhoneNumber updatedPersonPhoneNumber) {
        updatedPersonPhoneNumber.setId(id);
        personPhoneNumberRepository.save(updatedPersonPhoneNumber);
    }

    @Transactional
    public void delete(int id) {
        personPhoneNumberRepository.deleteById(id);
    }

    public PersonPhoneNumberRepository getPersonPhoneNumberRepository() {
        return personPhoneNumberRepository;
    }
}
