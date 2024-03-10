package ru.vvi.testtask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.models.PersonEmail;
import ru.vvi.testtask.repositories.PersonEmailRepository;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class PersonEmailService {

    private final PersonEmailRepository personEmailRepository;

    @Autowired
    public PersonEmailService(PersonEmailRepository personEmailRepository) {
        this.personEmailRepository = personEmailRepository;
    }

    public List<PersonEmail> findAll() {
        return personEmailRepository.findAll();
    }

    public List<PersonEmail> findAllByPerson(Person person) {
        return personEmailRepository.findAllByPerson(person);
    }

    public PersonEmail findOne(int id) {
        Optional<PersonEmail> foundEmail= personEmailRepository.findById(id);
        return foundEmail.orElse(null);
    }

    public PersonEmail findOneByEmail(String email) {
        Optional<PersonEmail> foundEmail= personEmailRepository.findByEmail(email);
        return foundEmail.orElse(null);
    }

    public Optional<PersonEmail> getPersonEmailByEmail(String email) {
        return personEmailRepository.findByEmail(email);
    }

    @Transactional
    public void save(PersonEmail userEmail) {
        personEmailRepository.save(userEmail);
    }

    @Transactional
    public void update(int id, PersonEmail updatedPersonEmail) {
        updatedPersonEmail.setId(id);
        personEmailRepository.save(updatedPersonEmail);
    }

    @Transactional
    public void delete(int id) {
        personEmailRepository.deleteById(id);
    }

    public PersonEmailRepository getPersonEmailRepository() {
        return personEmailRepository;
    }
}
