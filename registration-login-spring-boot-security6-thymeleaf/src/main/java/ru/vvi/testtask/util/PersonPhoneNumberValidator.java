package ru.vvi.testtask.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.models.PersonPhoneNumber;
import ru.vvi.testtask.services.PersonPhoneNumberService;


@Component
public class PersonPhoneNumberValidator implements Validator {

    private final PersonPhoneNumberService personPhoneNumberService;

    @Autowired
    public PersonPhoneNumberValidator(PersonPhoneNumberService personPhoneNumberService) {
        this.personPhoneNumberService = personPhoneNumberService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PersonPhoneNumber personPhoneNumber = (PersonPhoneNumber) o;

        if (personPhoneNumberService.getPhoneNumberByPhoneNumber(personPhoneNumber.getPhoneNumber()).isPresent())
            errors.rejectValue("phoneNumber", "", "Такой телефонный номер уже существует");
    }

}
