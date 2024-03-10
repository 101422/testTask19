package ru.vvi.testtask.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vvi.testtask.models.Person;
import ru.vvi.testtask.models.PersonEmail;
import ru.vvi.testtask.services.PersonEmailService;


@Component
public class PersonEmailValidator implements Validator {

    private final PersonEmailService personEmailService;

    @Autowired
    public PersonEmailValidator(PersonEmailService personEmailService) {
        this.personEmailService = personEmailService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PersonEmail personEmail = (PersonEmail) o;

        if (personEmailService.getPersonEmailByEmail(personEmail.getEmail()).isPresent())
            errors.rejectValue("email", "", "Такой email уже существует");
    }

}
