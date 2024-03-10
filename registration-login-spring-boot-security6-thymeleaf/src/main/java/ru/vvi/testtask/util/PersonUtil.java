package ru.vvi.testtask.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.models.PersonEmail;
import ru.vvi.testtask.models.PersonPhoneNumber;
import ru.vvi.testtask.services.PersonEmailService;
import ru.vvi.testtask.services.PersonPhoneNumberService;
import ru.vvi.testtask.services.PersonService;
import java.sql.Date;
import java.util.List;
import java.util.Objects;


@Component
public class PersonUtil {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private Person person;

    @Autowired
    private PersonEmail personEmail;

    @Autowired
    private PersonPhoneNumber personPhoneNumber;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonEmailService personEmailService;

    @Autowired
    private PersonPhoneNumberService personPhoneNumberService;

    public PersonUtil(PersonService personService, Person person, PersonEmail personEmail, PersonEmailService personEmailService, PersonPhoneNumberService personPhoneNumberService, PersonPhoneNumber personPhoneNumber) {
        this.personService = personService;
        this.person = person;
        this.personEmail = personEmail;
        this.personPhoneNumber = personPhoneNumber;
        this.personEmailService = personEmailService;
        this.personPhoneNumberService = personPhoneNumberService;
    }
    
    public void savePersonData (String dateOfBirth, String email, String name, String phoneNumber, String username, String password, Double amountOnDeposit) {

        // Поиск повторов
        if (!Objects.isNull(personEmailService.findOneByEmail(email))){
            System.out.println("Пользователь с таким email уже существует");

            return;
        }

        if (!Objects.isNull(personPhoneNumberService.findOneByPhoneNumber(phoneNumber))){
            System.out.println("Пользователь с таким телефонным номером уже существует");

            return;
        }

        String encodedPassword = passwordEncoder.encode(password);

        person.setDateOfBirth(Date.valueOf(dateOfBirth));
       // person.setName(name);
        person.setUsername(username);
        person.setPassword(encodedPassword);
        person.setAmountOnDeposit(amountOnDeposit);



        personEmail.setPerson(person);
        personEmail.setEmail(email);

        personPhoneNumber.setPerson(person);
        personPhoneNumber.setPhoneNumber(phoneNumber);
        //person.setPhoneNumber(phoneNumber);

        personService.save(person);

        personEmailService.save(personEmail);
        personPhoneNumberService.save(personPhoneNumber);

    }

    public Person findPersonByPersonname(String personname) {
        Person foundedPerson =  personService.findOneByUsername(personname);
        return foundedPerson;
    }

    public List findAllEmailsByPerson(Person person) {
        List foundedEmails =  personEmailService.findAllByPerson(person);
        return foundedEmails;
    }

    // Поиск всех email по логину.

    public List findAllEmailsByUsername(String personname) {
        Person foundedPerson =  personService.findOneByUsername(personname);

        List foundedEmails =  personEmailService.findAllByPerson(foundedPerson);
        return foundedEmails;
    }


    // Редактирование email.

    public void editEmail(String oldEmail, String newEmail, String personname) {
        Person foundedPerson =  personService.findOneByUsername(personname);

        PersonEmail foundedPersonEmail = personEmailService.findOneByEmail(oldEmail);

        if (!Objects.isNull(foundedPersonEmail) && !Objects.isNull(foundedPerson) && foundedPerson.getUsername().equals(foundedPersonEmail.getPerson().getUsername())) {
            foundedPersonEmail.setEmail(newEmail);

            personEmailService.save(foundedPersonEmail);
        }
    }

    // Добавление email.

    public void addEmail(String newEmail, String personname) {
        Person foundedPerson =  personService.findOneByUsername(personname);

        personEmail.setPerson(foundedPerson);
        personEmail.setEmail(newEmail);

        personEmailService.save(personEmail);
    }

    // Удаление email.

    public void deleteEmail(PersonEmail deletingEmail) {

        Person foundedPerson =  deletingEmail.getPerson();

        // Запрет удаления последнего email.

        List foundedEmails =  personEmailService.findAllByPerson(foundedPerson);

        if (foundedEmails.size() == 1) {
            System.out.println("Невозможно удалить последний email пользователя");

            return;
        }
        else {
            //PersonEmail foundedPersonEmail = personEmailService.findOneByEmail(deletingEmail);

            personEmailService.delete(deletingEmail.getId());
            //personEmailService.delete(deletingEmail);

        }
    }

    public void deletePhoneNumber(PersonPhoneNumber deletingPhoneNumber) {

        Person foundedPerson =  deletingPhoneNumber.getPerson();

        // Запрет удаления последнего email.

        List foundedPhoneNumbers =  personPhoneNumberService.findAllByPerson(foundedPerson);

        if (foundedPhoneNumbers.size() == 1) {
            System.out.println("Невозможно удалить последний телефонный номер пользователя");

            return;
        }
        else {
            //PersonEmail foundedPersonEmail = personEmailService.findOneByEmail(deletingEmail);

            personPhoneNumberService.delete(deletingPhoneNumber.getId());
            //personEmailService.delete(deletingEmail);

        }
    }

    // Поиск всех телефонных номеров по логину.

    public List findAllPhoneNumbersByUsername(String personname) {
        Person foundedPerson =  personService.findOneByUsername(personname);

        List foundedPhoneNumbers = personPhoneNumberService.findAllByPerson(foundedPerson);
        return foundedPhoneNumbers;
    }

    // Редактирование телефонных номеров.

    public void editPhoneNumber(String oldPhoneNumber, String newPhoneNumber, String personname) {
        Person foundedPerson =  personService.findOneByUsername(personname);

        PersonPhoneNumber foundedPersonPhoneNumber = personPhoneNumberService.findOneByPhoneNumber(oldPhoneNumber);

        if (!Objects.isNull(foundedPersonPhoneNumber) && !Objects.isNull(foundedPerson) && foundedPerson.getUsername().equals(foundedPersonPhoneNumber.getPerson().getUsername())) {
            foundedPersonPhoneNumber.setPhoneNumber(newPhoneNumber);

            personPhoneNumberService.save(foundedPersonPhoneNumber);
        }
    }

    // Добавление телефонных номеров.

    public void addPhoneNumber(String newPhoneNumber, String personname) {
        Person foundedPerson =  personService.findOneByUsername(personname);

        personPhoneNumber.setPerson(foundedPerson);
        personPhoneNumber.setPhoneNumber(newPhoneNumber);

        personPhoneNumberService.save(personPhoneNumber);
    }

    // Поиск пользователя по email.

    public Person findPersonByEmail(String email) {
        PersonEmail foundedPersonEmail = personEmailService.findOneByEmail(email);

        return foundedPersonEmail.getPerson();
    }

    // Поиск пользователя по телефонному номеру.

    public Person findPersonByPhoneNumber(String phoneNumber) {
        PersonPhoneNumber foundedPersonPhoneNumber = personPhoneNumberService.findOneByPhoneNumber(phoneNumber);

        return foundedPersonPhoneNumber.getPerson();
    }

}
